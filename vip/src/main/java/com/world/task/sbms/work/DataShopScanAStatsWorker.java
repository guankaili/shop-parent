package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.DataShopScanAStats;
import com.world.task.sbms.thread.DataShopScanAStatsThread;
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
 * @date 2020/3/20 20:27
 * @desc 门店扫码主页 入库量 出库量 签约门店数 定时任务
 **/
public class DataShopScanAStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataShopScanAStatsWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        log.info("门店扫码首页数据任务统计:【门店扫码首页数据数据同步】开始");
        if (workFlag) {
            workFlag = false;
            try {
                //获取门店信息
                String sqlShop = " SELECT t.dealer_cm_id AS dealerCmId, " +
                        "t.dealer_code AS dealerCode, " +
                        "t.dealer_name AS dealerName, " +
                        "t.large_area_code AS largeAreaCode, " +
                        "t.large_area AS largeArea, " +
                        "COUNT( t.shop_status = 5 OR " +
                        "t.shop_status = 6 OR t.shop_status = 7 OR NULL ) AS shopSignQuantity " +
                        "FROM es_shop_detail t GROUP BY t.dealer_cm_id ";

                //此处已经查询到了签约门店数
                List<Bean> shopBeans = Data.Query("shop_member", sqlShop, null, DataShopScanAStats.class);
                if (StringUtil.isNotEmpty(shopBeans)) {
                    List<DataShopScanAStats> shopList = ObjectConversion.copy(shopBeans, DataShopScanAStats.class);

                    //扫码进货 入库量 入库门店参与量
                    String sqlIn = " SELECT " +
                            "t.dealer_cm_id AS dealerCmId, " +
                            "COUNT( t.id ) AS shopScanInQuantity, " +
                            "COUNT( DISTINCT t.shop_id ) AS shopJoinInQuantity " +
                            "FROM " +
                            "scan_batch_record_detail t " +
                            "WHERE " +
                            "t.scan_type = '3' " +
                            "GROUP BY " +
                            "t.dealer_cm_id ";
                    List<Bean> ScanBeanIn = Data.Query("scan_main", sqlIn, null, DataShopScanAStats.class);
                    if (StringUtil.isNotEmpty(ScanBeanIn)) {
                        List<DataShopScanAStats> inList = ObjectConversion.copy(ScanBeanIn, DataShopScanAStats.class);
                        //数据拼装
                        shopList.forEach(item1 -> {
                            item1.setShopJoinInRate("0.00%");
                            inList.forEach(item2 -> {
                                if (item1.getDealerCmId().equals(item2.getDealerCmId())) {
                                    item1.setShopScanInQuantity(item2.getShopScanInQuantity());
                                    item1.setShopJoinInQuantity(item2.getShopJoinInQuantity());
                                }
                            });
                        });
                    }

                    //扫码出货 出库量 出库门店参与量
                    String sqlOut = " SELECT " +
                            "t.dealer_cm_id AS dealerCmId, " +
                            "COUNT( t.id ) AS shopScanOutQuantity, " +
                            "COUNT( DISTINCT t.shop_id ) AS shopJoinOutQuantity " +
                            "FROM " +
                            "scan_batch_record_detail t " +
                            "WHERE " +
                            "t.scan_type = '4' " +
                            "GROUP BY " +
                            "t.dealer_cm_id ";
                    List<Bean> ScanBeanOut = Data.Query("scan_main", sqlOut, null, DataShopScanAStats.class);
                    if (StringUtil.isNotEmpty(ScanBeanOut)) {
                        if (StringUtil.isNotEmpty(ScanBeanOut)) {
                            List<DataShopScanAStats> outList = ObjectConversion.copy(ScanBeanOut, DataShopScanAStats.class);
                            //数据拼装
                            shopList.forEach(item1 -> {
                                item1.setShopJoinOutRate("0.00%");
                                outList.forEach(item2 -> {
                                    //获取当前的排名
                                    if (item1.getDealerCmId().equals(item2.getDealerCmId())) {
                                        item1.setShopScanOutQuantity(item2.getShopScanOutQuantity());
                                        item1.setShopJoinOutQuantity(item2.getShopJoinOutQuantity());
                                    }
                                });
                            });
                        }
                    }
                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(shopList.size());
                    for (DataShopScanAStats dataShopScanAStats : shopList) {
                        //业务处理线程
                        DataShopScanAStatsThread dataShopScanAStatsThread = new DataShopScanAStatsThread(dataShopScanAStats, countDownLatch);
                        executorService.submit(dataShopScanAStatsThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                }
            } catch (Exception e) {
                log.error("门店扫码首页数据同步失败", e);
            } finally {
                workFlag = true;
            }
        }
    }

    public static void main(String[] args) {
        DataShopScanAStatsWorker dataShopScanAStatsWorker = new DataShopScanAStatsWorker("workDataShopScanAStatsWorker", "门店统计首页统计月统计 数据同步");
        dataShopScanAStatsWorker.run();
    }
}
