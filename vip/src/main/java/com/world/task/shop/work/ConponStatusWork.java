package com.world.task.shop.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.shop.EsMemberCouponModel;
import com.world.task.shop.thread.ConponStatusThread;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class ConponStatusWork extends Worker {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /*查询SQL*/
    private String sql = "";

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public ConponStatusWork(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        /*记录核算开始时间*/
        long startTime = System.currentTimeMillis();
        /*现在时间获取*/
        log.info("优惠券状态更新定时任务REWARDINFO:【优惠券状态更新定时任务】开始");

        if (workFlag) {
            workFlag = false;
            try {
                //去查询 es_member_coupon表中    used_status：使用状态;0未使用，3-使用中(冻结)，1已使用,2是已过期；4-未激活；5-退货失效；6-退货(只有赠券占用)；
                //查询优惠券中未使用状态和未激活状态并且结束时间小于等于当前时间
                sql = "select  mc_id as mcId  from  es_member_coupon where used_status in (0,4) and end_time <= now()";
                log.info("sql = " + sql);
                List<Bean> list = (List<Bean>) Data.Query("shop_trade", sql, null, EsMemberCouponModel.class);

                if (null != list && list.size() > 0) {
                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                    log.info("优惠券状态更新报警REWARDTASK:【优惠券状态更新】定时任务可以继续执行,本次优惠券状态更新总数【" + list.size() + "】");
                    CountDownLatch countDownLatch = new CountDownLatch(list.size());
                    /*定义接收变量*/
                    EsMemberCouponModel esMemberCouponModel = null;
                    for (int i = 0; i < list.size(); i++) {
                        esMemberCouponModel = (EsMemberCouponModel) list.get(i);
                        ConponStatusThread conponStatusThread = new ConponStatusThread(esMemberCouponModel, countDownLatch);
                        executorService.execute(conponStatusThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                } else {
                    log.info("优惠券状态更新REWARDTASK:【优惠券状态更新】本轮没有需要处理的扫码退货数据 sql = " + sql);
                    return;
                }

                long endTime = System.currentTimeMillis();
                log.info("优惠券状态更新REWARDTASK:【优惠券状态更新】结束!!!【核算耗时：" + (endTime - startTime) + "】");
            } catch (Exception e) {
                log.info("优惠券状态更新REWARDERROR:ScanInDealerWork", e);
            } finally {
                workFlag = true;
            }
        } else {
            log.info("优惠券状态更新REWARDTASK:【优惠券状态更新】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    public static void main(String[] args) {
        ConponStatusWork scanOutShopWork = new ConponStatusWork("", "");
        for (int i = 0; i < 1; i++) {
            scanOutShopWork.run();
        }
    }
}
