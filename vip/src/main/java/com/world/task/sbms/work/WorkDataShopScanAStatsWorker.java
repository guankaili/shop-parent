package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.WorkDataShopScanAStats;
import com.world.task.sbms.thread.WorkDataShopScanAStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;
import org.springframework.util.CollectionUtils;
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
public class WorkDataShopScanAStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public WorkDataShopScanAStatsWorker(String name, String des) {
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
                        "t.shop_status = 6 OR t.shop_status = 7 OR NULL ) AS shopQuantity " +
                        "FROM es_shop_detail t GROUP BY t.dealer_cm_id ";

                //此处已经查询到了签约门店数
                List<Bean> shopBeans = Data.Query("shop_member", sqlShop, null, WorkDataShopScanAStats.class);
                if (StringUtil.isNotEmpty(shopBeans)) {
                    List<WorkDataShopScanAStats> shopList = ObjectConversion.copy(shopBeans, WorkDataShopScanAStats.class);

                    //获取扫码信息
                    String sqlScan = " SELECT t.dealer_cm_id," +
                            "COUNT(t.scan_type = 3 OR NULL) AS scanInQuantity, " +
                            "COUNT(t.scan_type = 4 OR NULL) AS scanOutQuantity, " +
                            "COUNT(DISTINCT t.shop_id OR NULL) AS scanJoinShopQuantity, " +
                            "COUNT(DISTINCT t.scan_type = 3 OR NULL) AS shopJoinInQuantity, " +
                            "COUNT(DISTINCT t.scan_type = 4 OR NULL) AS shopJoinOutQuantity " +
                            "FROM scan_batch_record_detail t GROUP BY t.dealer_cm_id ";

                    //按进出库

                    //查询扫码
                    List<Bean> ScanBeans = Data.Query("scan_main", sqlScan, null, WorkDataShopScanAStats.class);

                    //查询结果数据拼装
                    if (!CollectionUtils.isEmpty(ScanBeans)) {
                        List<WorkDataShopScanAStats> scanList = ObjectConversion.copy(shopBeans, WorkDataShopScanAStats.class);
                        shopList.forEach(item1 -> {
                            scanList.forEach(item2 -> {
                                if (item1.getDealerCmId().equals(item2.getDealerCmId())) {
                                    //入库量
                                    item1.setShopScanInQuantity(item2.getShopScanInQuantity());
                                    //出库量
                                    item1.setShopScanOutQuantity(item2.getShopScanOutQuantity());
                                    //入库门店参与数
                                    item1.setShopJoinInQuantity(item2.getShopJoinInQuantity());
                                    //出库门店参与数
                                    item1.setShopJoinOutQuantity(item2.getShopJoinOutQuantity());

                                    //此处来计算   入库门店参与率   出库门店参与率
                                    item1.setShopJoinInRate(accuracy(item2.getShopJoinInQuantity(), item1.getShopSignQuantity(), 1));
                                    item1.setShopJoinOutRate(accuracy(item2.getShopJoinOutQuantity(), item1.getShopSignQuantity(), 1));
                                }
                            });
                        });
                        //构建线程池
                        //当提交的任务数量为1000的时候，会开辟20个线程数
                        ExecutorService executorService = Executors.newFixedThreadPool(10);
                        CountDownLatch countDownLatch = new CountDownLatch(shopList.size());
                        for (WorkDataShopScanAStats workDataShopScanAStats : shopList) {
                            //业务处理线程
                            WorkDataShopScanAStatsThread workDataShopScanAStatsThread = new WorkDataShopScanAStatsThread(workDataShopScanAStats, countDownLatch);
                            executorService.submit(workDataShopScanAStatsThread);
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

    public static void main(String[] args) {
        WorkDataShopScanAStatsWorker workDataShopScanAStatsWorker = new WorkDataShopScanAStatsWorker("workDataShopScanAStatsWorker", "门店统计首页统计月统计 数据同步");
        workDataShopScanAStatsWorker.run();
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
}
