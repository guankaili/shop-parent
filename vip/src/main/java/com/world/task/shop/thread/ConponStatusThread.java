package com.world.task.shop.thread;

import com.alibaba.fastjson.JSON;
import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.shop.EsMemberCouponModel;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ConponStatusThread extends Thread {
    private static Logger log = Logger.getLogger(ConponStatusThread.class);
    /**
     * sql语句
     */
    private String sql = "";

    private CountDownLatch countDownLatch;
    private EsMemberCouponModel esMemberCouponModel;

    public ConponStatusThread(EsMemberCouponModel esMemberCouponModel, CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.esMemberCouponModel = esMemberCouponModel;
    }

    @Override
    public void run() {
        log.info("ConponStatusThread...run...EsMemberCouponModel = " + JSON.toJSONString(esMemberCouponModel));
        //获取主键
        Integer id = esMemberCouponModel.getMcId();

        try {
            //开启事务处理
            List<OneSql> sqls = new ArrayList<OneSql>();
            TransactionObject txObj = new TransactionObject();
            long startTime = System.currentTimeMillis();

            //将数据更新为已过期状态

            sql = "update es_member_coupon set used_status = 2 where mc_id = '" + id + "'";
            sqls.add(new OneSql(sql, 1, null, "shop_trade"));
            log.info("sql:" + sql);
            //执行事务
            txObj.excuteUpdateList(sqls);
            if (txObj.commit()) {
                log.info("优惠券状态更新REWARDINFO:【优惠券状态更新】处理成功，主键ID【" + id + "】");
            } else {
                log.info("优惠券状态更新REWARDERROR:【优惠券状态更新】处理失败，主键ID【" + id + "】");
            }

            long endTime = System.currentTimeMillis();
            log.info("优惠券状态更新REWARDInfo:【优惠券状态更新】主键ID【" + id + "】处理耗时【" + (endTime - startTime) + "】");
        } catch (Exception e) {
            log.info("优惠券状态更新REWARDERROR:ConponStatusThread", e);
        } finally {
            countDownLatch.countDown();
        }
    }


}
