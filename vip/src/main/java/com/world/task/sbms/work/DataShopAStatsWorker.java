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
                        "t.shop_province_id AS shopProvinceId, " +
                        "t.shop_Province AS shopProvince, " +
                        "t.brand_code AS brandCode, " +
                        "t.brand_name AS brandName, " +
                        "COUNT( t.id ) AS shopTotalQuantity, " +   //签约门店
                        "COUNT( t.shop_status = 6 OR NULL ) AS shopPanicQuantity, " +  //抢购门店
                        "COUNT( date_format( t.contract_time, '%Y-%m' ) = DATE_FORMAT( now(), '%Y-%m' ) OR NULL ) AS shopAddQuantity, " +   //新增门店
                        "COUNT( t.shop_status = 6 AND date_format( t.contract_time, '%Y-%m' ) = DATE_FORMAT( now(), '%Y-%m' ) OR NULL ) AS shopAddPanicQuantity, " +  //新增抢购门店
                        "COUNT( t.shop_type = 1 OR NULL ) AS ctsARate, " +
                        "COUNT( t.shop_type = 2 OR NULL ) AS ctsRate, " +
                        "COUNT( t.shop_type = 3 OR NULL ) AS ccsARate, " +
                        "COUNT( t.shop_type = 4 OR NULL ) AS ccsRate, " +
                        "COUNT( t.shop_type = 5 OR NULL ) AS cpsRate " +
                        "FROM " +
                        "es_shop_detail t " +
                        "WHERE (t.shop_status = 5 OR t.shop_status = 6 OR t.shop_status = 7) "+
                        "GROUP BY " +
                        "t.dealer_cm_id ";

                //店铺进本数据查询
                List<Bean> shopBeans = Data.Query("shop_member", sqlShopBase, null, DataShopAStats.class);
                if (StringUtil.isNotEmpty(shopBeans)) {
                    List<DataShopAStats> ShopBaseR = ObjectConversion.copy(shopBeans, DataShopAStats.class);

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
