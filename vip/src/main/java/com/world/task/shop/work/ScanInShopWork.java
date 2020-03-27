package com.world.task.shop.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.shop.ScanBatchRecordDetailModel;
import com.world.task.shop.thread.ScanInShopThread;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class ScanInShopWork extends Worker {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /*查询SQL*/
    private String sql = "";

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public ScanInShopWork(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        /*记录核算开始时间*/
        long startTime = System.currentTimeMillis();
        /*现在时间获取*/
        log.info("扫码入库报警REWARDINFO:【扫码入库处理】开始");

        if (workFlag) {
            workFlag = false;
            try {
                /**
                 *
                 */
                sql = "select * from scan_batch_record_detail_bak where scan_type = 3 limit 1 ";
                log.info("sql = " + sql);
                List<Bean> scanBatchRecordDetailModelList = (List<Bean>) Data.Query("scan_main", sql, null, ScanBatchRecordDetailModel.class);

                if (null != scanBatchRecordDetailModelList && scanBatchRecordDetailModelList.size() > 0) {
                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                    log.info("扫码入库报警REWARDTASK:【扫码入库处理】定时任务可以继续执行,本次扫码入库处理总数【" + scanBatchRecordDetailModelList.size() + "】");
                    CountDownLatch countDownLatch = new CountDownLatch(scanBatchRecordDetailModelList.size());
                    /*定义接收变量*/
                    ScanBatchRecordDetailModel scanBatchRecordDetailModel = null;
                    for (int i = 0; i < scanBatchRecordDetailModelList.size(); i++) {
                        scanBatchRecordDetailModel = (ScanBatchRecordDetailModel) scanBatchRecordDetailModelList.get(i);
                        ScanInShopThread scanInShopThread = new ScanInShopThread(scanBatchRecordDetailModel, countDownLatch);
                        executorService.execute(scanInShopThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                } else {
                    log.info("扫码入库报警REWARDTASK:【扫码入库处理】本轮没有需要处理的扫码入库数据 sql = " + sql);
                    return;
                }

                long endTime = System.currentTimeMillis();
                log.info("扫码入库报警REWARDTASK:【扫码入库处理】结束!!!【核算耗时：" + (endTime - startTime) + "】");
            } catch (Exception e) {
                log.info("扫码入库报警REWARDERROR:ScanInDealerWork", e);
            } finally {
                workFlag = true;
            }
        } else {
            log.info("扫码入库报警REWARDTASK:【扫码入库处理】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    public static void main(String[] args) {
        ScanInShopWork scanInShopWork = new ScanInShopWork("", "");
        for (int i = 0; i < 10; i++) {
            scanInShopWork.run();
        }
    }
}
