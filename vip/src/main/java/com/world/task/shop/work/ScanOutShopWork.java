package com.world.task.shop.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.shop.ScanBatchRecordDetailModel;
import com.world.task.shop.thread.ScanInShopThread;
import com.world.task.shop.thread.ScanOutShopThread;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class ScanOutShopWork extends Worker {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /*查询SQL*/
    private String sql = "";

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public ScanOutShopWork(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        /*记录核算开始时间*/
        long startTime = System.currentTimeMillis();
        /*现在时间获取*/
        log.info("扫码退货报警REWARDINFO:【扫码退货处理】开始");

        if (workFlag) {
            workFlag = false;
            try {
                /**
                 *-- `check_state` int(11) DEFAULT '0' COMMENT '轮胎状态：
                 * -- 1-正常;2-此编码不属于当前的经销商;3-此编码没有对应的经销商;
                 * --  4-该趾口编码属于门店上级经销商,但门店不存在该规格待收货订单或者无待收货产品；
                 * -- 5-条码不属于当前的经销商,门店待收货订单中包含条码',
                 * --   `flow_state` int(11) DEFAULT '0' COMMENT '订单状态：1-正常;2-门店入库退货;3-门店未入库退货;'   不需要
                 * --   `scan_type` int(11) DEFAULT NULL COMMENT '扫码种类：1-经销商退货;2-经销商退货扫码;3-门店入库;4-门店退货',
                 */
                sql = "select * from scan_batch_record_detail_bak where scan_type = 2 limit 1 ";
                log.info("sql = " + sql);
                List<Bean> scanBatchRecordDetailModelList = (List<Bean>) Data.Query("scan_main", sql, null, ScanBatchRecordDetailModel.class);

                if (null != scanBatchRecordDetailModelList && scanBatchRecordDetailModelList.size() > 0) {
                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                    log.info("扫码退货报警REWARDTASK:【扫码退货处理】定时任务可以继续执行,本次扫码退货处理总数【" + scanBatchRecordDetailModelList.size() + "】");
                    CountDownLatch countDownLatch = new CountDownLatch(scanBatchRecordDetailModelList.size());
                    /*定义接收变量*/
                    ScanBatchRecordDetailModel scanBatchRecordDetailModel = null;
                    for (int i = 0; i < scanBatchRecordDetailModelList.size(); i++) {
                        scanBatchRecordDetailModel = (ScanBatchRecordDetailModel) scanBatchRecordDetailModelList.get(i);
                        ScanOutShopThread scanOutShopThread = new ScanOutShopThread(scanBatchRecordDetailModel, countDownLatch);
                        executorService.execute(scanOutShopThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                } else {
                    log.info("扫码退货报警REWARDTASK:【扫码退货处理】本轮没有需要处理的扫码退货数据 sql = " + sql);
                    return;
                }

                long endTime = System.currentTimeMillis();
                log.info("扫码退货报警REWARDTASK:【扫码退货处理】结束!!!【核算耗时：" + (endTime - startTime) + "】");
            } catch (Exception e) {
                log.info("扫码退货报警REWARDERROR:ScanInDealerWork", e);
            } finally {
                workFlag = true;
            }
        } else {
            log.info("扫码退货报警REWARDTASK:【扫码退货处理】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    public static void main(String[] args) {
        ScanOutShopWork scanOutShopWork = new ScanOutShopWork("", "");
        for (int i = 0; i < 1; i++) {
            scanOutShopWork.run();
        }
    }
}
