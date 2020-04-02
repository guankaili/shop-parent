package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.DataDealerCmIdStatus;
import com.world.model.sbms.DataMainMStats;
import com.world.task.sbms.thread.DataMainShopMStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/20 9:27
 * @desc 门店统计 - 计算本月的 门店统计月统计 门店扫码统计月统计
 **/
public class DataMainShopMStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataMainShopMStatsWorker(String name, String des) {
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
                        "COUNT( t.id ) AS shopQuantity, " +
                        "COUNT( t.shop_status = 6  OR NULL) AS activeShopQuantity," +
                        "COUNT( date_format(t.contract_time, '%Y-%m') = DATE_FORMAT(now(), '%Y-%m')  OR NULL) AS addShopQuantity " +
                        "FROM es_shop_detail t " +
                        "WHERE " +
                        "  (t.shop_status = '5' OR t.shop_status = '6' OR t.shop_status = '7' ) " +
                        "GROUP BY t.dealer_cm_id";

                List<Bean> beans = Data.Query("shop_member", sqlShop, null, DataMainMStats.class);
                if (StringUtil.isNotEmpty(beans)) {
                    List<DataMainMStats> dataMainMStatsList = ObjectConversion.copy(beans, DataMainMStats.class);

                    //获取业务员id
                    List<String> dealerCmId = new ArrayList<>();
                    dataMainMStatsList.forEach(item -> {
                        dealerCmId.add(item.getDealerCmId());
                    });
                    //用业务员id--查询扫码表的统计
                    String sqlScan = " SELECT t.dealer_cm_id AS dealerCmId, " +
                            "COUNT(t.scan_type = 3 OR NULL) AS scanInQuantity, " +
                            "COUNT(t.scan_type = 4 OR NULL) AS scanOutQuantity, " +
                            "COUNT(DISTINCT t.shop_id ) AS scanJoinShopQuantity " +
                            "FROM scan_batch_record_detail t " +
                            "GROUP BY t.dealer_cm_id  ";
                    List<Bean> scans = Data.Query("scan_main", sqlScan, null, DataMainMStats.class);
                    List<DataMainMStats> scanList = ObjectConversion.copy(scans, DataMainMStats.class);

                    //查询结果数据拼装
                    if (!CollectionUtils.isEmpty(scanList)) {
                        List<DataMainMStats> nquantityMCopy = ObjectConversion.copy(scanList, DataMainMStats.class);
                        dataMainMStatsList.forEach(item1 -> {
                            nquantityMCopy.forEach(item2 -> {
                                if (item1.getDealerCmId().equals(item2.getDealerCmId())) {
                                    item1.setScanInQuantity(item2.getScanInQuantity());
                                    item1.setScanOutQuantity(item2.getScanOutQuantity());
                                    item1.setScanJoinShopQuantity(item2.getScanJoinShopQuantity());
                                }
                            });
                        });
                    }

                    //一次性获取中间表所有数据，判断新增或更新
                    Map<String,String> map = new HashMap<>();
                    String ifSql = " SELECT t1.dealer_cm_id AS dealerCmId FROM data_home_shop_stats t1  ";
                    List<Bean> dealerCmIdStatuses = Data.Query("sbms_main", ifSql, null,DataDealerCmIdStatus.class);
                    if (StringUtil.isNotEmpty(dealerCmIdStatuses)){
                        List<DataDealerCmIdStatus> list = ObjectConversion.copy(dealerCmIdStatuses, DataDealerCmIdStatus.class);
                        map = list.stream().collect(Collectors.toMap(DataDealerCmIdStatus::getDealerCmId,DataDealerCmIdStatus::getDealerCmId));
                    }
                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(dataMainMStatsList.size());
                    for(DataMainMStats dataMainMStats : dataMainMStatsList){
                        //业务处理线程
                        DataMainShopMStatsThread dataMainShopMStatsThread = new DataMainShopMStatsThread(dataMainMStats,countDownLatch,map);
                        executorService.submit(dataMainShopMStatsThread);
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
        DataMainShopMStatsWorker dataMainShopMStatsWorker = new DataMainShopMStatsWorker("workDataMainShopMStatsWorker", "门店统计月统计、门店扫码统计月统计 数据同步");
        dataMainShopMStatsWorker.run();
    }
}
