package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.WorkDataMainMStats;
import com.world.model.sbms.WorkDataShopScanAStats;
import com.world.model.sbms.WorkHomeMStats;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/21 8:36
 * @desc 扫码统计主页
 **/
public class WorkDataShopScanAStatsThread extends Thread {
    private static Logger logger = Logger.getLogger(WorkDataShopScanAStatsThread.class);
    private WorkDataShopScanAStats workDataShopScanAStats;
    private CountDownLatch countDownLatch;

    public WorkDataShopScanAStatsThread(WorkDataShopScanAStats workDataShopScanAStats, CountDownLatch countDownLatch) {
        this.workDataShopScanAStats = workDataShopScanAStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try{
        String sql = "INSERT INTO data_scan_home_stats (" +
                "dealer_code," +
                "dealer_name," +
                "dealer_cm_id," +
                "large_area_code," +
                "large_area_name," +
                "shop_scan_in_quantity," +
                "shop_sign_quantity," +
                "shop_join_in_quantity," +
                "shop_join_in_rate," +
                "shop_scan_out_quantity," +
                "shop_join_out_quantity," +
                "shop_join_out_rate," +
                "create_date " +
                ")" +
                "VALUES" +
                "(?,?,?,?,?,?,?,?,?,?,?,?,?)";

            List<Object> param = new ArrayList<>();
            param.add(workDataShopScanAStats.getDealerCode());
            param.add(workDataShopScanAStats.getDealerName());
            param.add(workDataShopScanAStats.getDealerCmId());
            param.add(workDataShopScanAStats.getLargeAreaCode());
            param.add(workDataShopScanAStats.getLargeArea());
            param.add(workDataShopScanAStats.getShopScanInQuantity());
            param.add(workDataShopScanAStats.getShopSignQuantity());
            param.add(workDataShopScanAStats.getShopJoinInQuantity());
            param.add(workDataShopScanAStats.getShopJoinInRate());
            param.add(workDataShopScanAStats.getShopScanOutQuantity());
            param.add(workDataShopScanAStats.getShopJoinOutQuantity());
            param.add(workDataShopScanAStats.getShopJoinOutRate());
            param.add(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            sqls.add(new OneSql(sql, 1, param.toArray(), "sbms_main"));
            txObj.excuteUpdateList(sqls);
            if (txObj.commit()) {
                logger.info("插入成功");
            } else {
                logger.error("插入失败");
            }

        }catch (Exception e) {
            logger.error("数据新增失败",e);
        } finally {
            countDownLatch.countDown();
        }

    }
}
