package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.shop.OrderItemsModel;
import com.world.task.sbms.thread.DataActivityThread;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 活动数据抽取
 */
public class DataActivityWorker extends Worker {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /*查询SQL*/
    private String sql = "";

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataActivityWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        /*记录核算开始时间*/
        long startTime = System.currentTimeMillis();
        /*现在时间获取*/
        log.info("活动数据抽取报警REWARDINFO:【活动数据抽取】开始");

        if (workFlag) {
            workFlag = false;
            try {
                /**
                 * 数据抽取条件
                 */
                sql = "SELECT fa.*, fb.payment_time FROM es_order_items fa, es_order fb "
                    + "WHERE fa.order_sn = fb.sn and fa.activity_deal_flag = 0 and fb.pay_status = 'PAY_YES' limit 1000 ";
                log.info("查询到的原始数据！！！！ sql = " + sql);
                List<Bean> orderItemsModelList = (List<Bean>) Data.Query("shop_trade", sql, null, OrderItemsModel.class);

                if (null != orderItemsModelList && orderItemsModelList.size() > 0) {
                    ExecutorService executorService = Executors.newFixedThreadPool(6);
                    log.info("活动数据抽取报警REWARDTASK:【活动数据抽取处理】定时任务可以继续执行,本次扫码活动数据处理总数【" + orderItemsModelList.size() + "】");
                    CountDownLatch countDownLatch = new CountDownLatch(orderItemsModelList.size());

                    /*定义接收变量*/
                    OrderItemsModel orderItemsModel = null;
                    for (int i = 0; i < orderItemsModelList.size(); i++) {
                        orderItemsModel = (OrderItemsModel) orderItemsModelList.get(i);
                        DataActivityThread dataActivityThread = new DataActivityThread(orderItemsModel, countDownLatch);
                        executorService.execute(dataActivityThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                } else {
                    log.info("活动数据抽取报警REWARDTASK:【活动数据抽取处理】本轮没有需要处理的活动数据 sql = " + sql);
                    return;
                }

                long endTime = System.currentTimeMillis();
                log.info("活动数据报警REWARDTASK:【活动数据处理】结束!!!【处理耗时：" + (endTime - startTime) + "】");
            } catch (Exception e) {
                log.info("活动数据处理报警REWARDERROR:DataActivityWorker", e);
            } finally {
                workFlag = true;
            }
        } else {
            log.info("活动数据报警REWARDTASK:【活动数据处理】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    public static void main(String[] args) {
        DataActivityWorker scanInShopWork = new DataActivityWorker("", "");
        scanInShopWork.run();
    }
}
