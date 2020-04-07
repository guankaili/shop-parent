package com.world.task.shop.thread;

import com.alibaba.fastjson.JSON;
import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.shop.EsShopModel;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PanicBuyingShopThread extends Thread {
    private static Logger log = Logger.getLogger(PanicBuyingShopThread.class);
    /**
     * sql语句
     */
    private String sql = "";

    private CountDownLatch countDownLatch;
    private EsShopModel esShopModel;

    public PanicBuyingShopThread(EsShopModel esShopModel, CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.esShopModel = esShopModel;
    }

    @Override
    public void run() {
        log.info("PanicBuyingShopThread...run...EsShopModel = " + JSON.toJSONString(esShopModel));
        //获取主键
        Integer shopId = esShopModel.getShopId();

        try {
            //开启事务处理
            List<OneSql> sqls = new ArrayList<OneSql>();
            TransactionObject txObj = new TransactionObject();
            long startTime = System.currentTimeMillis();

            //将数据更新为已过期状态

            sql = "update es_shop_detail set shop_status = 6 where shop_status in (5,7) and shop_id = '" + shopId + "'";
            sqls.add(new OneSql(sql, 1, null, "shop_member"));
            log.info("sql:" + sql);
            //执行事务
            txObj.excuteUpdateList(sqls);
            if (txObj.commit()) {
                log.info("抢购门店状态更新REWARDINFO:【抢购门店状态更新】处理成功，门店ID【" + shopId + "】");
            } else {
                log.info("抢购门店状态更新REWARDERROR:【抢购门店状态更新】处理失败，门店ID【" + shopId + "】");
            }

            long endTime = System.currentTimeMillis();
            log.info("抢购门店状态更新REWARDInfo:【抢购门店状态更新】门店ID【" + shopId + "】处理耗时【" + (endTime - startTime) + "】");
        } catch (Exception e) {
            log.info("抢购门店状态更新REWARDERROR:ConponStatusThread", e);
        } finally {
            countDownLatch.countDown();
        }
    }


}
