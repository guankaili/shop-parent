package com.world.task.shop.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.shop.EsShopModel;
import com.world.task.shop.thread.PanicBuyingShopThread;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 抢购门店状态定时任务
 */
public class PanicBuyingShopWork extends Worker {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /*查询SQL*/
    private String sql = "";

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public PanicBuyingShopWork(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        /*记录核算开始时间*/
        long startTime = System.currentTimeMillis();
        /*现在时间获取*/
        log.info("抢购门店状态更新REWARDINFO:【抢购门店状态更新】开始");

        if (workFlag) {
            workFlag = false;
            try {
                //统计两个月之内发生过扫码入库的门店id
                sql = "select shop_id  as shopId from scan_batch_record group by shop_id HAVING max(create_datetime) > date_sub(NOW(), interval 2 MONTH) ";
                log.info("sql = " + sql);
                List<Bean> list = (List<Bean>) Data.Query("scan_main", sql, null, EsShopModel.class);

                if (null != list && list.size() > 0) {
                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                    log.info("抢购门店状态更新报警REWARDTASK:【抢购门店状态更新】定时任务可以继续执行,本次抢购门店状态更新总数【" + list.size() + "】");
                    CountDownLatch countDownLatch = new CountDownLatch(list.size());
                    /*定义接收变量*/
                    EsShopModel esShopModel = null;
                    for (int i = 0; i < list.size(); i++) {
                        esShopModel = (EsShopModel) list.get(i);
                        PanicBuyingShopThread panicBuyingShopThread = new PanicBuyingShopThread(esShopModel, countDownLatch);
                        executorService.execute(panicBuyingShopThread);
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
            } finally {
                workFlag = true;
            }
        } else {
            log.info("抢购门店状态更新REWARDTASK:【抢购门店状态更新】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    public static void main(String[] args) {
        PanicBuyingShopWork scanOutShopWork = new PanicBuyingShopWork("", "");
        for (int i = 0; i < 1; i++) {
            scanOutShopWork.run();
        }
    }
}
