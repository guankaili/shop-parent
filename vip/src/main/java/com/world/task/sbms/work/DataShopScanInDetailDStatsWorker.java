package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.DataShopScanInDetailDStats;
import com.world.task.sbms.thread.DataShopScanInDetailDStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/22 12:59
 * @desc 门店扫码 入库 详细列表定时任务
 **/
public class DataShopScanInDetailDStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataShopScanInDetailDStatsWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {

        log.info("门店扫码数据任务统计:【门店扫码入库详细列表数据数据同步】开始");
        if (workFlag) {
            workFlag = false;
            try {
                //用业务员id查询店铺表
                //用业务员id--查询扫码表的统计
                String sqlShop = " SELECT " +
                        "t.shop_province_id AS shopProvinceId, " +
                        "t.shop_Province AS shopProvince, " +
                        "t.dealer_name AS dealerName, " +
                        "t.dealer_code AS dealerCode, " +
                        "t.dealer_cm_id AS dealerCmId, " +
                        "t.large_area_code AS largeAreaCode, " +
                        "t.large_area AS largeArea, " +
                        "t.dealer_cm_name AS dealerCmName, " +
                        "COUNT( t.shop_id ) AS shopSignQuantity " +
                        "FROM " +
                        "es_shop_detail t " +
                        "WHERE " +
                        "t.shop_status IN ( 5, 6, 7 ) " +
                        "AND to_days( t.contract_time ) = to_days( " +
                        "now()) " +
                        "GROUP BY " +
                        "t.dealer_cm_id ";

                //查询店铺数量
                List<Bean> shopBeans = Data.Query("shop_member", sqlShop, null, DataShopScanInDetailDStats.class);
                if (StringUtil.isNotEmpty(shopBeans)) {
                    List<DataShopScanInDetailDStats> inDetailDStats = ObjectConversion.copy(shopBeans, DataShopScanInDetailDStats.class);
                    String sqlIn = " SELECT " +
                            "t.dealer_name AS dealerName, " +
                            "t.dealer_code AS dealerCode, " +
                            "t.dealer_cm_id AS dealerCmId, " +
                            "t.dealer_cm_name AS dealerCmName, " +
                            "COUNT( t.id ) AS shopInQuantity, " +
                            "COUNT( DISTINCT t.shop_id ) AS shopJoinInQuantity " +
                            "FROM " +
                            "scan_batch_record_detail t " +
                            "WHERE " +
                            "t.scan_type = 3 " +
                            "AND to_days( t.create_datetime ) = to_days( " +
                            "now()) " +
                            "GROUP BY " +
                            "t.dealer_cm_id ";
                    List<Bean> scanBeans = Data.Query("scan_main", sqlIn, null, DataShopScanInDetailDStats.class);
                    if (StringUtil.isNotEmpty(scanBeans)) {
                        List<DataShopScanInDetailDStats> inDetailDStatsTow = ObjectConversion.copy(scanBeans, DataShopScanInDetailDStats.class);
                        inDetailDStats.forEach(item1 -> {
                            item1.setShopJoinInRate("0.00%");
                            inDetailDStatsTow.forEach(item2 -> {
                                if (item1.getDealerCmId().equals(item2.getDealerCmId())) {
                                    //入库量
                                    item1.setShopInQuantity(item2.getShopInQuantity());
                                    //参与量
                                    item1.setShopJoinInQuantity(item2.getShopJoinInQuantity());
                                    //计算参与率
                                    item1.setShopJoinInRate(accuracy(item1.getShopJoinInQuantity(), item1.getShopSignQuantity(), 2));
                                }
                            });
                        });
                        //构建线程池
                        //当提交的任务数量为1000的时候，会开辟20个线程数
                        ExecutorService executorService = Executors.newFixedThreadPool(10);
                        CountDownLatch countDownLatch = new CountDownLatch(inDetailDStats.size());
                        for (DataShopScanInDetailDStats dataShopScanInDetailDStats : inDetailDStats) {
                            //业务处理线程
                            DataShopScanInDetailDStatsThread dataShopScanInDetailDStatsThread = new DataShopScanInDetailDStatsThread(dataShopScanInDetailDStats, countDownLatch);
                            executorService.submit(dataShopScanInDetailDStatsThread);
                        }
                        countDownLatch.await();
                        /*关闭线程池*/
                        executorService.shutdown();
                    }
                }

            } catch (Exception e) {
                log.error("门店扫码入库详细列表数据同步失败", e);
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
        DataShopScanInDetailDStatsWorker dataShopScanInDetailDStatsWorker = new DataShopScanInDetailDStatsWorker("workDataShopScanInDetailDStatsWorker", "门店扫码入库柱状图数据同步 数据同步");
        dataShopScanInDetailDStatsWorker.run();
    }

}
