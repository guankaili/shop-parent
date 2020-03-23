package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.WorkDataShopScanOutDetailDStats;
import com.world.task.sbms.thread.WorkDataShopScanOutDetailDStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/22 12:59
 * @desc 门店扫码 出库 详细列表定时任务
 **/
public class WorkDataShopScanOutDetailDStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public WorkDataShopScanOutDetailDStatsWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {

        log.info("门店扫码数据任务统计:【门店扫码出库扫码详细数据数据同步】开始");
        if (workFlag) {
            workFlag = false;
            try {
                String sqlOut = " SELECT" +
                        "t.province_code AS provinceCode, " +
                        "t.province_name AS provinceName, " +
                        "t.dealer_name AS dealerName, " +
                        "t.dealer_code AS dealerCode, " +
                        "t.dealer_cm_id AS dealerCmId, " +
                        "t.dealer_cm_name AS dealerCmName, " +
                        "COUNT( t.id ) AS shopOutQuantity, " +
                        "COUNT( DISTINCT t.shop_id ) AS shopJoinOutQuantity " +
                        "FROM " +
                        "scan_batch_record_detail t " +
                        "WHERE " +
                        "t.scan_type = 4 " +
                        "AND to_days( t.create_datetime ) = to_days( " +
                        "now()) " +
                        "GROUP BY " +
                        "t.dealer_cm_id ";
                List<Bean> shopBeans = Data.Query("scan_main", sqlOut, null, WorkDataShopScanOutDetailDStats.class);
                if (StringUtil.isNotEmpty(shopBeans)) {
                    List<WorkDataShopScanOutDetailDStats> outDetailDStats = ObjectConversion.copy(shopBeans, WorkDataShopScanOutDetailDStats.class);

                    //获取业务员id
                    List<String> dealerCmId = new ArrayList<>();
                    outDetailDStats.forEach(item -> {
                        dealerCmId.add(item.getDealerCmId());
                    });

                    //用业务员id查询店铺表
                    //用业务员id--查询扫码表的统计
                    String sqlShop = " SELECT" +
                            "t.province_code AS provinceCode, " +
                            "t.province_name AS provinceName, " +
                            "t.dealer_name AS dealerName, " +
                            "t.dealer_code AS dealerCode, " +
                            "t.dealer_cm_id AS dealerCmId, " +
                            "t.dealer_cm_name AS dealerCmName, " +
                            "COUNT( t.shop_id ) AS shopSignQuantity " +
                            "FROM " +
                            "es_shop_detail t " +
                            "WHERE " +
                            "t.shop_status IN ( 5, 6, 7 ) " +
                            "AND to_days( t.contract_time ) = to_days( " +
                            "now()) " +
                            "AND t.dealer_cm_id IN (" + dealerCmId + ") " +
                            "GROUP BY " +
                            "t.dealer_cm_id ";

                    //查询店铺数量
                    List<Bean> ScanBeans = Data.Query("shop_member", sqlShop, null, WorkDataShopScanOutDetailDStats.class);
                    if (StringUtil.isNotEmpty(ScanBeans)) {
                        List<WorkDataShopScanOutDetailDStats> outDetailDStatsTow = ObjectConversion.copy(shopBeans, WorkDataShopScanOutDetailDStats.class);

                        outDetailDStats.forEach(item1 -> {
                            outDetailDStatsTow.forEach(item2 -> {
                                if (item1.getDealerCmId().equals(item2.getDealerCmId())) {
                                    //入库量
                                    item1.setShopSignQuantity(item2.getShopSignQuantity());
                                    //计算参与率
                                    item1.setShopJoinOutRate(accuracy(item2.getShopJoinOutQuantity(), item2.getShopSignQuantity(), 3));
                                }
                            });
                        });
                        //构建线程池
                        //当提交的任务数量为1000的时候，会开辟20个线程数
                        ExecutorService executorService = Executors.newFixedThreadPool(10);
                        CountDownLatch countDownLatch = new CountDownLatch(outDetailDStats.size());
                        for (WorkDataShopScanOutDetailDStats workDataShopScanOutDetailDStats : outDetailDStats) {
                            //业务处理线程
                            WorkDataShopScanOutDetailDStatsThread workDataShopScanOutDetailDStatsThread = new WorkDataShopScanOutDetailDStatsThread(workDataShopScanOutDetailDStats, countDownLatch);
                            executorService.submit(workDataShopScanOutDetailDStatsThread);
                        }
                        countDownLatch.await();
                        /*关闭线程池*/
                        executorService.shutdown();
                    }
                }

            } catch (Exception e) {
                log.error("门店扫码首页数据同步失败", e);
            } finally {
                workFlag = true;
            }
        }
    }

    //用来计算概率的方法
    public static String accuracy(double num, double total, int scale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return df.format(accuracy_num) + "%";
    }

    public static void main(String[] args) {
        WorkDataShopScanOutDetailDStatsWorker workDataShopScanOutDetailDStatsWorker = new WorkDataShopScanOutDetailDStatsWorker("workDataShopScanOutDetailDStatsWorker", "门店扫码入库柱状图数据同步 数据同步");
        workDataShopScanOutDetailDStatsWorker.run();
    }

}
