package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.DataDealerCmIdStatus;
import com.world.model.sbms.DataShopScanInDetailDStats;
import com.world.task.sbms.thread.DataShopScanInDetailDStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.SqlUtil;
import com.world.util.StringUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
                String sqlIn = " SELECT " +
                        "t.dealer_name AS dealerName, " +
                        "t.dealer_code AS dealerCode, " +
                        "t.dealer_cm_id AS dealerCmId, " +
                        "t.dealer_cm_name AS dealerCmName, " +
                        "COUNT( t.id ) AS shopInQuantity, " +
                        "COUNT( DISTINCT t.shop_id ) AS shopJoinInQuantity, " +
                        "date_format(t.create_datetime,'%Y-%m-%d') AS shopScanInDetailDate "+
                        "FROM " +
                        "scan_batch_record_detail t " +
                        "WHERE " +
                        "t.scan_type = 3 " +
                        "AND to_days( t.create_datetime ) = to_days( " +
                        "now()) " +
                        "GROUP BY " +
                        "t.dealer_cm_id ";
                //查询店铺数量
                List<Bean> scanBeans = Data.Query("scan_main", sqlIn, null, DataShopScanInDetailDStats.class);
                if (StringUtil.isNotEmpty(scanBeans)) {
                    List<DataShopScanInDetailDStats> inDetailDStats = ObjectConversion.copy(scanBeans, DataShopScanInDetailDStats.class);

                    //获取经销商业务员code
                    List<String> dealerCmIds = new ArrayList<String>();
                    inDetailDStats.forEach(item -> {
                        dealerCmIds.add(item.getDealerCmId());
                    });
                    //list转数组
                    String[] dealerCmIdArr = dealerCmIds.stream().toArray(String[]::new);
                    //拼接in条件sql
                    List<Object> param = new ArrayList<>();
                    String dealerCmId = SqlUtil.getInStrSql(dealerCmIdArr, param);

                    //用扫码的经销商id 去查询店铺表的经销商 获取区域信息赋值
                    String sqlShop = " SELECT " +
                            "t.shop_province_id AS shopProvinceId, " +
                            "t.shop_Province AS shopProvince, " +
                            "t.dealer_name AS dealerName, " +
                            "t.dealer_code AS dealerCode, " +
                            "t.dealer_cm_id AS dealerCmId, " +
                            "t.large_area_code AS largeAreaCode, " +
                            "t.large_area AS largeArea, " +
                            "t.dealer_cm_name AS dealerCmName,  " +
                            "COUNT( t.shop_id ) AS shopSignQuantity " +
                            "FROM " +
                            "es_shop_detail t " +
                            "WHERE " +
                            "t.shop_status IN ( 5, 6, 7 ) " +
                            "AND to_days( t.contract_time ) = to_days( " +
                            "now()) AND t.dealer_cm_id in ("+dealerCmId+") " +
                            "GROUP BY " +
                            "t.dealer_cm_id ";

                    List<Bean> shopBeans = Data.Query("shop_member", sqlShop, param.toArray(), DataShopScanInDetailDStats.class);
                    if (StringUtil.isNotEmpty(shopBeans)) {
                        List<DataShopScanInDetailDStats> inDetailDStatsByScan = ObjectConversion.copy(shopBeans, DataShopScanInDetailDStats.class);
                        inDetailDStats.forEach(item1 -> {
                            inDetailDStatsByScan.forEach(item2 -> {
                                if (item1.getDealerCmId().equals(item2.getDealerCmId())) {
                                    //入库量
                                    item1.setShopSignQuantity(item2.getShopSignQuantity());
                                    item1.setShopProvinceId(item2.getShopProvinceId());
                                    item1.setShopProvince(item2.getShopProvince());
                                    item1.setLargeArea(item2.getLargeArea());
                                    item1.setLargeAreaCode(item2.getLargeAreaCode());
                                }
                            });
                        });
                    }

                    //一次性获取中间表所有数据，判断新增或更新
                    Map<String,String> map = new ConcurrentHashMap<String, String>();
                    String ifSql = " SELECT t1.dealer_cm_id AS dealerCmId,date_format(now(),'%Y-%m-%d') AS thisDate FROM data_scan_in_detail_stats t1 ";
                    List<Bean> dealerCmIdStatuses = Data.Query("sbms_main", ifSql, null, DataDealerCmIdStatus.class);
                    if (StringUtil.isNotEmpty(dealerCmIdStatuses)){
                        List<DataDealerCmIdStatus> paList = ObjectConversion.copy(dealerCmIdStatuses, DataDealerCmIdStatus.class);
                        map = paList.stream().collect(Collectors.toMap(k->k.getDealerCmId()+k.getThisDate(),k->k.getDealerCmId()));
                    }

                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(inDetailDStats.size());
                    //灌数据
                    for (DataShopScanInDetailDStats dataShopScanInDetailDStats : inDetailDStats) {
                        //业务处理线程
                        DataShopScanInDetailDStatsThread dataShopScanInDetailDStatsThread = new DataShopScanInDetailDStatsThread(dataShopScanInDetailDStats, countDownLatch,map);
                        executorService.submit(dataShopScanInDetailDStatsThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                }
            } catch (Exception e) {
                log.error("门店扫码入库详细列表数据同步失败", e);
            } finally {
                workFlag = true;
            }
        }
    }

    public static void main(String[] args) {
        DataShopScanInDetailDStatsWorker dataShopScanInDetailDStatsWorker = new DataShopScanInDetailDStatsWorker("workDataShopScanInDetailDStatsWorker", "门店扫码入库柱状图数据同步 数据同步");
        dataShopScanInDetailDStatsWorker.run();
    }

}
