package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.DealerGoodsStats;
import com.world.task.sbms.thread.DataDealerOgoodsStatsThread;
import com.world.util.ObjectConversion;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author guankaili
 * @version v1.0.0
 * @date 2020/3/19$ 13:21$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/3/19$ 13:21$     guankaili          v1.0.0           Created
 */
public class DataDealerOgoodsStatsWorker extends Worker {
    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataDealerOgoodsStatsWorker(String name, String des) {
        super(name, des);
    }
    @Override
    public void run(){
        log.info("数据模块年出货量统计:【年出货量统计】开始");
        if (workFlag) {
            workFlag = false;
            try {
                //1、获取年出货量统计
                String sql = "SELECT MONTH (t.create_datetime) m,t.dealer_code dealerCode,t.dealer_name dealerName," +
                        "t.brand_code brandCode,t.brand_name brandName,t.area_code provinceCode,t.area_name provinceName," +
                        "sum(t.goods_quantity) goodsNum FROM scan_batch_record_gmodel t " +
                        "WHERE DATE_FORMAT(t.create_datetime,'%Y')=DATE_FORMAT(CURDATE(),'%Y') AND t.scan_type = 1 " +
                        "GROUP BY MONTH (t.create_datetime),t.dealer_code,t.brand_code,t.area_code " +
                        "ORDER BY MONTH (t.create_datetime)";
                List<Bean> list = (List<Bean>) Data.Query("scan_main", sql, null, DealerGoodsStats.class);
                if(!CollectionUtils.isEmpty(list)){
                    List<DealerGoodsStats> dealerGoodsStatsList = ObjectConversion.copy(list, DealerGoodsStats.class);
                    //根据经销商code分组
                    Map<String, List<DealerGoodsStats>> groupByDealerCode = dealerGoodsStatsList.stream().collect(Collectors.groupingBy(DealerGoodsStats::getDealerCode));
                    log.info("任务名称【" + "dataDealerOgoodsStatsWorker" + "】开始执行.此轮需要执行【" + list.size() + "】条数据任务");
                    //1、清除表数据
                    String trancateSql = " truncate table data_dealer_ogoods_stats ";
                    Data.Delete("sbms_main",trancateSql,null);
                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(groupByDealerCode.size());
                    groupByDealerCode.forEach((k,v) -> {
                        //根据品牌分组
                        Map<String, List<DealerGoodsStats>> groupByBrandCode = v.stream().collect(Collectors.groupingBy(DealerGoodsStats::getBrandCode));
                        groupByBrandCode.forEach((k1,v1) -> {
                            //业务处理线程
                            DataDealerOgoodsStatsThread dataDealerOgoodsStatsThread = new DataDealerOgoodsStatsThread(v1,countDownLatch);
                            executorService.submit(dataDealerOgoodsStatsThread);
                        });
                    });

                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                }else{
                    log.info("任务名称【" + "dataDealerOgoodsStatsWorker" + "】此轮没有需要执行的数据.");
                }

            } catch (Exception e) {
                log.error("年出货量统计失败", e);
            } finally {
                workFlag = true;
            }
        }else {
            log.info("数据模块年出货量统计:【年出货量统计】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    public static void main(String[] args) {
        DataDealerOgoodsStatsWorker workHomeStatsWorker = new DataDealerOgoodsStatsWorker("dataDealerOgoodsStatsWorker","年进货量统计");
        workHomeStatsWorker.run();
    }
}
