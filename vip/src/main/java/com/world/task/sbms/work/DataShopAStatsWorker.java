package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.*;
import com.world.task.sbms.thread.DataShopAStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/22 9:48
 * @desc 门店统计首页数据 签约门店 抢购门店 新增门店 新增抢购门店 大区排名 全国排名
 **/
public class DataShopAStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataShopAStatsWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {

        log.info("门店扫码首页数据任务统计:【门店统计首页数据数据同步】开始");
        if (workFlag) {
            workFlag = false;
            try {

                //获取门店统计的  签约门店 抢购门店 新增门店 新增请购门店
                String sqlShopBase = " SELECT " +
                        "t.dealer_code AS dealerCode, " +
                        "t.dealer_name AS dealerName, " +
                        "t.dealer_cm_id AS dealerCmId, " +
                        "t.large_area_code AS largeAreaCode, " +
                        "t.large_area AS largeArea, " +
                        "t.shop_id AS shopId, " +
                        "t.brand_code AS brandCode, " +
                        "t.brand_name AS brandName, " +
                        "COUNT( t.shop_status = 5 OR t.shop_status = 6 OR t.shop_status = 7 OR NULL ) AS shopTotalQuantity, " +
                        "COUNT( t.shop_status = 6 OR NULL ) AS shopPanicQuantity, " +
                        "COUNT( date_format( t.contract_time, '%Y-%m' ) = DATE_FORMAT( now(), '%Y-%m' ) OR NULL ) AS shopAddQuantity, " +
                        "COUNT( t.shop_status = 6 AND date_format( t.contract_time, '%Y-%m' ) = DATE_FORMAT( now(), '%Y-%m' ) OR NULL ) AS shopAddPanicQuantity, " +
                        "COUNT( t.shop_type = 1 OR NULL ) AS ctsARate, " +
                        "COUNT( t.shop_type = 2 OR NULL ) AS ctsRate, " +
                        "COUNT( t.shop_type = 3 OR NULL ) AS ccsARate, " +
                        "COUNT( t.shop_type = 4 OR NULL ) AS ccsRate, " +
                        "COUNT( t.shop_type = 5 OR NULL ) AS cpsRate " +
                        "FROM " +
                        "es_shop_detail t " +
                        "GROUP BY " +
                        "t.dealer_cm_id ";

                //店铺进本数据查询
                List<Bean> shopBeans = Data.Query("shop_member", sqlShopBase, null, DataShopAStats.class);
                if (StringUtil.isNotEmpty(shopBeans)) {
                    List<DataShopAStats> ShopBaseR = ObjectConversion.copy(shopBeans, DataShopAStats.class);
                    //获取全国排名 和总数
                    String sqlRank = " SELECT " +
                            "t.dealer_code AS dealerCode, " +
                            "t.dealer_name AS dealerName, " +
                            "t.dealer_cm_id AS dealerCmId, " +
                            "t.large_area_code AS largeAreaCode, " +
                            "COUNT(t.shop_id) AS countryRank " +
                            "FROM " +
                            "es_shop_detail t " +
                            "GROUP BY " +
                            "t.dealer_cm_id " +
                            "ORDER BY " +
                            "countryRank DESC ";
                    List<Bean> shopRank = Data.Query("shop_member", sqlRank, null, DataShopAStats.class);
                    if (StringUtil.isNotEmpty(shopRank)) {
                        List<DataShopAStats> scanRankR = ObjectConversion.copy(shopRank, DataShopAStats.class);
                        //基础数据与排名合并
                        ShopBaseR.forEach(item1 -> {
                            scanRankR.forEach(item2 -> {
                                //获取当前的排名
                                int curIndex = scanRankR.indexOf(item2);
                                if(item1.getDealerCmId().equals(item2.getDealerCmId())){
                                    item1.setCountryQuantity((long) scanRankR.size());
                                    item1.setCountryRank((long) curIndex);
                                }
                            });
                        });

                        //获取获取北方排名 和总数
                        String sqlRankByBF = " SELECT " +
                                "t.dealer_code AS dealerCode, " +
                                "t.dealer_name AS dealerName, " +
                                "t.dealer_cm_id AS dealerCmId, " +
                                "t.large_area_code AS largeAreaCode, " +
                                " COUNT(t.shop_id) AS areaRank " +
                                "FROM " +
                                " es_shop_detail t " +
                                "WHERE t.large_area_code = 'N001' "+
                                "GROUP BY " +
                                " t.dealer_cm_id " +
                                "ORDER BY " +
                                " areaRank DESC ";
                        List<Bean> shopRankByBF = Data.Query("shop_member", sqlRankByBF, null, DataShopAStats.class);
                        if (StringUtil.isNotEmpty(shopRankByBF)){
                            List<DataShopAStats> scanRankByBFR = ObjectConversion.copy(shopRankByBF, DataShopAStats.class);
                            ShopBaseR.forEach(item1 -> {
                                scanRankByBFR.forEach(item2 -> {
                                    //获取当前的排名
                                    if(item1.getDealerCmId().equals(item2.getDealerCmId())){
                                        //判断是否是南区的用户
                                        if (item1.getLargeAreaCode().equals(item2.getLargeAreaCode())){
                                            //获取当前的排名
                                            int curIndex2 = scanRankByBFR.indexOf(item2);
                                            item1.setAreaQuantity((long) scanRankByBFR.size());
                                            item1.setAreaRank((long)curIndex2);
                                        }
                                    }
                                });
                            });
                        }


                        //获取南方大区排名 和总数
                        String sqlRankByNF = " SELECT " +
                                "t.dealer_code AS dealerCode, " +
                                "t.dealer_name AS dealerName, " +
                                "t.dealer_cm_id AS dealerCmId, " +
                                "t.large_area_code AS largeAreaCode, " +
                                " COUNT(t.shop_id) AS areaRank " +
                                "FROM " +
                                " es_shop_detail t " +
                                "WHERE t.large_area_code = 'S001' "+
                                "GROUP BY " +
                                " t.dealer_cm_id " +
                                "ORDER BY " +
                                " areaRank DESC ";
                        List<Bean> shopRankByNF = Data.Query("shop_member", sqlRankByNF, null, DataShopAStats.class);
                        if (StringUtil.isNotEmpty(shopRankByNF)){
                            List<DataShopAStats> scanRankByNFR = ObjectConversion.copy(shopRankByNF, DataShopAStats.class);
                            ShopBaseR.forEach(item1 -> {
                                scanRankByNFR.forEach(item2 -> {
                                    //获取当前的排名
                                    if(item1.getDealerCmId().equals(item2.getDealerCmId())){
                                        //判断是否是南区的用户
                                        if (item1.getLargeAreaCode().equals(item2.getLargeAreaCode())){
                                            //获取当前的排名
                                            int curIndex3 = scanRankByNFR.indexOf(item2);
                                            item1.setAreaQuantity((long)scanRankByNFR.size());
                                            item1.setAreaRank((long)curIndex3);
                                        }
                                    }
                                });
                            });
                        }
                    }

                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(ShopBaseR.size());
                    for(DataShopAStats dataShopAStats : ShopBaseR){
                        //业务处理线程
                        DataShopAStatsThread dataShopAStatsThread = new DataShopAStatsThread(dataShopAStats,countDownLatch);
                        executorService.submit(dataShopAStatsThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();

                }
            } catch (Exception e) {
                log.error("门店统计首页数据同步失败", e);
            } finally {
                workFlag = true;
            }
        }

    }

    public static void main(String[] args) {
        DataShopAStatsWorker dataShopAStatsWorker = new DataShopAStatsWorker("WorkDataShopAStatsWorker", "门店统计首页统计月统计 数据同步");
        dataShopAStatsWorker.run();
    }

}
