package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.WorkDataMainMStats;
import com.world.model.sbms.WorkHomeMStats;
import com.world.task.sbms.thread.WorkDataMainShopMStatsThread;
import com.world.task.sbms.thread.WorkHomeMStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/20 9:27
 * @desc 门店统计 - 计算本月的 门店统计月统计 门店扫码统计月统计
 **/
public class WorkDataMainShopMStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public WorkDataMainShopMStatsWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        log.info("工作模块数据首页:【门店统计月统计、门店扫码统计月统计】开始");
        if (workFlag) {
            try {
                workFlag = false;
                //获取门店统计数据
                String sqlShop = "SELECT t.dealer_cm_id AS dealerCmId," +
                        "t.dealer_code AS dealerCode," +
                        "t.dealer_name AS dealerName," +
                        "t.large_area_code AS largeAreaCode, " +
                        "t.large_area AS largeArea, " +
                        "COUNT( t.shop_status = 5 OR t.shop_status = 6 OR t.shop_status = 7 OR NULL ) AS shopQuantity, " +
                        "COUNT( t.shop_status = 6 OR NULL ) AS activeShopQuantity," +
                        "COUNT( date_format(t.contract_time, '%Y-%m') = DATE_FORMAT(now(), '%Y-%m') OR NULL ) AS addShopQuantity " +
                        "FROM es_shop_detail t GROUP BY t.dealer_cm_id";

                List<Bean> beans = Data.Query("shop_member", sqlShop, null, WorkDataMainMStats.class);
                if (StringUtil.isNotEmpty(beans)) {
                    List<WorkDataMainMStats> workDataMainMStatsList = ObjectConversion.copy(beans, WorkDataMainMStats.class);

                    //获取业务员id
                    List<String> dealerCmId = new ArrayList<>();
                    workDataMainMStatsList.forEach(item -> {
                        dealerCmId.add(item.getDealerCmId());
                    });
                    //用业务员id--查询扫码表的统计
                    String sqlScan = " SELECT t.dealer_cm_id AS dealerCmId," +
                            "COUNT(t.scan_type = 3 AND date_format(t.create_datetime, '%Y-%m') = DATE_FORMAT(now(), '%Y-%m') OR NULL) AS scanInQuantity," +
                            "COUNT(t.scan_type = 4 AND date_format(t.create_datetime, '%Y-%m') = DATE_FORMAT(now(), '%Y-%m') OR NULL) AS scanOutQuantity," +
                            "COUNT(DISTINCT t.shop_id OR NULL) AS scanJoinShopQuantity " +
                            "FROM scan_batch_record_detail t GROUP BY t.dealer_cm_id  ";
                    List<Bean> scans = Data.Query("scan_main", sqlScan, null, WorkDataMainMStats.class);
                    List<WorkDataMainMStats> scanList = ObjectConversion.copy(scans, WorkDataMainMStats.class);

                    //查询结果数据拼装
                    if (!CollectionUtils.isEmpty(scanList)) {
                        List<WorkDataMainMStats> nquantityMCopy = ObjectConversion.copy(scanList, WorkDataMainMStats.class);
                        workDataMainMStatsList.forEach(item1 -> {
                            nquantityMCopy.forEach(item2 -> {
                                if (item1.getDealerCmId().equals(item2.getDealerCmId())) {
                                    item1.setScanInQuantity(item2.getScanInQuantity());
                                    item1.setScanOutQuantity(item2.getScanOutQuantity());
                                    item1.setScanJoinShopQuantity(item2.getScanJoinShopQuantity());
                                }
                            });
                        });
                    }
                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(workDataMainMStatsList.size());
                    for(WorkDataMainMStats workDataMainMStats : workDataMainMStatsList){
                        //业务处理线程
                        WorkDataMainShopMStatsThread workDataMainShopMStatsThread = new WorkDataMainShopMStatsThread(workDataMainMStats,countDownLatch);
                        executorService.submit(workDataMainShopMStatsThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                } else {
                    log.info("工作模块数据首页:【门店统计月统计、门店扫码统计月统计】上一轮任务尚未结束，本轮不需要运行");
                }
            } catch (Exception e) {
                log.error("门店任务数据同步失败", e);
            } finally {
                workFlag = true;
            }
        }
    }

    public static void main(String[] args) {
        WorkDataMainShopMStatsWorker workDataMainShopMStatsWorker = new WorkDataMainShopMStatsWorker("workDataMainShopMStatsWorker", "门店统计月统计、门店扫码统计月统计 数据同步");
        workDataMainShopMStatsWorker.run();
    }
}
