package com.world.task.shop.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.shop.EsMemberCouponModel;
import com.world.task.shop.thread.ConponAppendThread;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 已认证门店追加64张赠券
 */
public class ConponAppendWork extends Worker {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /*查询SQL*/
    private String sql = "";


    public ConponAppendWork(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        /*记录核算开始时间*/
        long startTime = System.currentTimeMillis();

        try {
            //查询出所有已认证门店
            sql = " select  member_id as memberId from es_shop_detail  where shop_status >= 5 ";
            log.info("sql = " + sql);
            List<Bean> list = (List<Bean>) Data.Query("shop_member", sql, null, EsMemberCouponModel.class);

            if (null != list && list.size() > 0) {
                ExecutorService executorService = Executors.newFixedThreadPool(1);

                CountDownLatch countDownLatch = new CountDownLatch(list.size());
                /*定义接收变量*/
                EsMemberCouponModel esMemberCouponModel = null;
                for (int i = 0; i < list.size(); i++) {
                    esMemberCouponModel = (EsMemberCouponModel) list.get(i);
                    ConponAppendThread conponAppendThread = new ConponAppendThread(esMemberCouponModel, countDownLatch);
                    executorService.execute(conponAppendThread);
                }
                countDownLatch.await();
                /*关闭线程池*/
                executorService.shutdown();
            } else {
                log.info("抢购门店状态更新REWARDTASK:【抢购门店状态更新】本轮没有需要处理的扫码退货数据 sql = " + sql);
                return;
            }

            long endTime = System.currentTimeMillis();
            log.info("抢购门店状态更新REWARDTASK:【抢购门店状态更新】结束!!!【核算耗时：" + (endTime - startTime) + "】");
        } catch (Exception e) {
            log.info("抢购门店状态更新REWARDERROR:ScanInDealerWork", e);
        }
    }

    public static void main(String[] args) {
        ConponAppendWork scanOutShopWork = new ConponAppendWork("", "");
        for (int i = 0; i < 1; i++) {
            scanOutShopWork.run();
        }
    }
}
