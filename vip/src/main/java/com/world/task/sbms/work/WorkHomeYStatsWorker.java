package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.WorkHomeYStats;
import com.world.task.sbms.thread.WorkHomeYStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.SqlUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author guankaili
 * @version v1.0.0
 * @date 2020/3/19$ 13:21$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/3/19$ 13:21$     guankaili          v1.0.0           Created
 */
public class WorkHomeYStatsWorker extends Worker {
    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public WorkHomeYStatsWorker(String name, String des) {
        super(name, des);
    }
    @Override
    public void run(){
        log.info("工作模块门店任务统计:【门店任务数据同步】开始");
        if (workFlag) {
            workFlag = false;
            try {
                //1、获取签约门店信息及门店任务数==月度
                String sql = "SELECT (t2.shop_task_m*(TIMESTAMPDIFF(MONTH,DATE_FORMAT(t1.contract_time,'%Y-%m-%d'),DATE_FORMAT(now(),'%Y-%m-%d'))+1)) signShopTaskQuantityY," +
                        "t1.dealer_code dealerCode,t1.dealer_name dealerName," +
                        "t1.dealer_cm_id dealerCmId,t1.shop_id shopId,t1.shop_type shopType,t1.large_area_code largeAreaCode,t1.large_area largeAreaName," +
                        "t1.shop_province provinceName,t1.shop_province_id provinceCode,t1.shop_city cityName," +
                        "t1.shop_city_id cityCode,t1.shop_county areaName,t1.shop_county_id areaCode,t1.brand_code brandCode," +
                        "t1.brand_name brandName FROM es_shop_detail t1 LEFT JOIN es_shop_type t2 ON t2.shop_type=t1.shop_type " +
                        "WHERE t1.shop_status IN (5,6) " +
                        "AND DATE_FORMAT(t1.contract_time,'%Y')=DATE_FORMAT(CURDATE(),'%Y')";
                List<Bean> list = (List<Bean>) Data.Query("shop_member", sql, null, WorkHomeYStats.class);
                if(!CollectionUtils.isEmpty(list)){
                    List<WorkHomeYStats> workHomeStats = ObjectConversion.copy(list, WorkHomeYStats.class);
                    //获取签约门店数==月度
                    Integer signShopQuantityY = list.size();
                    //获取shopId集合
                    List<Integer> shopIds = new ArrayList<>();
                    workHomeStats.forEach(item -> {
                        item.setSignShopQuantityY(signShopQuantityY);
                        shopIds.add(item.getShopId());
                    });
                    //list转数组
                    Integer[] shopIdArr = shopIds.stream().toArray(Integer[]::new);
                    //拼接in条件sql
                    List<Object> param = new ArrayList<>();
                    String shopId = SqlUtil.getInSql(shopIdArr, param);
                    //2、查询签约门店任务完成数量-月度
                    String mcSql = "SELECT count(1) signShopTaskCquantityY , t.shop_id shopId FROM scan_batch_record_detail t " +
                            "WHERE t.scan_type = 4 AND DATE_FORMAT(t.create_datetime,'%Y')=DATE_FORMAT(CURDATE(),'%Y') " +
                            "AND t.shop_id in ("+shopId+") GROUP BY t.shop_id";
                    List<Bean> cquantityMs = Data.Query("scan_main",mcSql,param.toArray(), WorkHomeYStats.class);
                    if(!CollectionUtils.isEmpty(cquantityMs)){
                        List<WorkHomeYStats> cquantityYCopy = ObjectConversion.copy(cquantityMs, WorkHomeYStats.class);
                        workHomeStats.forEach(item1 -> {
                            cquantityYCopy.forEach(item2 -> {
                                if(item1.getShopId().equals(item2.getShopId())){
                                    Long signShopTaskCquantityY = item2.getSignShopTaskCquantityY() != null ? item2.getSignShopTaskCquantityY() : 0;
                                    item1.setSignShopTaskCquantityY(signShopTaskCquantityY);
                                }
                            });
                        });
                    }
                    //3、查询签约门店任务完成数量-月度
                    String ncSql = "SELECT t.shop_id shopId,count(1) signShopTaskNquantityY FROM scan_batch_record_detail t " +
                            "WHERE t.scan_type = 3 AND DATE_FORMAT(t.create_datetime,'%Y')=DATE_FORMAT(CURDATE(),'%Y') " +
                            "AND t.shop_id in ("+shopId+") GROUP BY t.shop_id";
                    List<Bean> nquantityMs = (List<Bean>)Data.Query("scan_main",ncSql,param.toArray(), WorkHomeYStats.class);
                    if(!CollectionUtils.isEmpty(nquantityMs)){
                        List<WorkHomeYStats> nquantityYCopy = ObjectConversion.copy(nquantityMs, WorkHomeYStats.class);
                        workHomeStats.forEach(item1 -> {
                            nquantityYCopy.forEach(item2 -> {
                                if(item1.getShopId().equals(item2.getShopId())){
                                    Long signShopTaskNquantityY = item2.getSignShopTaskNquantityY() != null ? item2.getSignShopTaskNquantityY() : 0;
                                    item1.setSignShopTaskNquantityY(signShopTaskNquantityY);
                                }
                            });
                        });
                    }
                    log.info("任务名称【WorkHomeYStatsWorker】开始执行.此轮需要执行【" + workHomeStats.size() + "】条数据任务");
                    //1、清除表数据
                    String trancateSql = " truncate table work_home_y_stats ";
                    Data.Delete("sbms_main",trancateSql,null);
                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(workHomeStats.size());
                    for(WorkHomeYStats workHomeStat : workHomeStats){
                        //业务处理线程
                        WorkHomeYStatsThread workHomeYStatsThread = new WorkHomeYStatsThread(workHomeStat,countDownLatch);
                        executorService.submit(workHomeYStatsThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                }
            } catch (Exception e) {
                log.error("门店任务数据同步失败", e);
            } finally {
                workFlag = true;
            }
        }else {
            log.info("工作模块门店任务统计:【门店任务数据同步】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    public static void main(String[] args) {
        WorkHomeYStatsWorker workHomeStatsWorker = new WorkHomeYStatsWorker("workHomeStatsWorker","门店任务数据同步");
        workHomeStatsWorker.run();
    }
}
