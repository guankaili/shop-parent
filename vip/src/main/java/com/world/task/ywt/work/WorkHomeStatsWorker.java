package com.world.task.ywt.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.ywt.WorkHomeStats;
import com.world.util.ObjectConversion;
import com.world.util.SqlUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guankaili
 * @version v1.0.0
 * @date 2020/3/19$ 13:21$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/3/19$ 13:21$     guankaili          v1.0.0           Created
 */
public class WorkHomeStatsWorker extends Worker {
    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public WorkHomeStatsWorker(String name, String des) {
        super(name, des);
    }
    @Override
    public void run(){
        log.info("工作模块门店任务统计:【门店任务数据同步】开始");
        if (workFlag) {
            workFlag = false;
            try {
                //1、获取签约门店信息及门店任务数==月度
                String sql = "SELECT t2.shop_task_m signShopTaskQuantityM,t1.dealer_code dealerCode,t1.dealer_name dealerName," +
                        "t1.dealer_cm_id dealerCmId,t1.shop_id shopId,t1.shop_type shopType,t1.large_area_code largeAreaCode,t1.large_area largeAreaName," +
                        "t1.shop_province provinceName,t1.shop_province_id provinceCode,t1.shop_city cityName," +
                        "t1.shop_city_id cityCode,t1.shop_county areaName,t1.shop_county_id areaCode,t1.brand_code brandCode," +
                        "t1.brand_name brandName FROM es_shop_detail t1 LEFT JOIN es_shop_type t2 ON t2.shop_type=t1.shop_type " +
                        "WHERE t1.shop_status IN (5,6) " +
                        "AND DATE_FORMAT(t1.contract_time,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')";
                List<Bean> list = (List<Bean>) Data.Query("shop_member", sql, null, WorkHomeStats.class);
                if(!CollectionUtils.isEmpty(list)){
                    List<WorkHomeStats> workHomeStats = ObjectConversion.copy(list,WorkHomeStats.class);
                    //获取签约门店数==月度
                    Integer signShopQuantityM = list.size();
                    //获取shopId集合
                    List<Integer> shopIds = new ArrayList<>();
                    workHomeStats.forEach(item -> {
                        item.setSignShopQuantityM(signShopQuantityM);
                        shopIds.add(item.getShopId());
                    });
                    //list转数组
                    Integer[] shopIdArr = shopIds.stream().toArray(Integer[]::new);
                    //拼接in条件sql
                    List<Object> param = new ArrayList<>();
                    String shopId = SqlUtil.getInSql(shopIdArr, param);
                    //2、查询签约门店任务完成数量-月度
                    String mcSql = "SELECT t.shop_code shopId,count(1) signShopTaskCquantityM FROM scan_batch_record_detail t " +
                            "WHERE t.scan_type = 4 AND DATE_FORMAT(t.create_datetime,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m') " +
                            "AND t.shop_code in ("+shopId+") GROUP BY t.shop_code";
                    List<Bean> cquantityMs = (List<Bean>)Data.Query("scan_main",mcSql,param.toArray(),WorkHomeStats.class);
                    if(!CollectionUtils.isEmpty(cquantityMs)){
                        List<WorkHomeStats> cquantityMCopy = ObjectConversion.copy(list,WorkHomeStats.class);
                        workHomeStats.forEach(item1 -> {
                            cquantityMCopy.forEach(item2 -> {
                                if(item1.getShopId().equals(item2.getShopId())){
                                    item1.setSignShopTaskCquantityM(item2.getSignShopTaskCquantityM());
                                }
                            });
                        });
                    }
                    //3、查询签约门店任务完成数量-月度
                    String ncSql = "SELECT t.shop_code shopId,count(1) signShopTaskNquantityM FROM scan_batch_record_detail t " +
                            "WHERE t.scan_type = 3 AND DATE_FORMAT(t.create_datetime,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m') " +
                            "AND t.shop_code in ("+shopId+") GROUP BY t.shop_code";
                    List<Bean> nquantityMs = (List<Bean>)Data.Query("scan_main",ncSql,param.toArray(),WorkHomeStats.class);
                    if(!CollectionUtils.isEmpty(nquantityMs)){
                        List<WorkHomeStats> nquantityMCopy = ObjectConversion.copy(list,WorkHomeStats.class);
                        workHomeStats.forEach(item1 -> {
                            nquantityMCopy.forEach(item2 -> {
                                if(item1.getShopId().equals(item2.getShopId())){
                                    item1.setSignShopTaskNquantityM(item2.getSignShopTaskNquantityM());
                                }
                            });
                        });
                    }
                    System.out.println(workHomeStats);
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
        WorkHomeStatsWorker workHomeStatsWorker = new WorkHomeStatsWorker("workHomeStatsWorker","门店任务数据同步");
        workHomeStatsWorker.run();
    }
}
