package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopScanAStats;
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
public class DataShopScanAStatsThread extends Thread {
    private static Logger logger = Logger.getLogger(DataShopScanAStatsThread.class);
    private DataShopScanAStats dataShopScanAStats;
    private CountDownLatch countDownLatch;

    public DataShopScanAStatsThread(DataShopScanAStats dataShopScanAStats, CountDownLatch countDownLatch) {
        this.dataShopScanAStats = dataShopScanAStats;
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
            param.add(dataShopScanAStats.getDealerCode());
            param.add(dataShopScanAStats.getDealerName());
            param.add(dataShopScanAStats.getDealerCmId());
            param.add(dataShopScanAStats.getLargeAreaCode());
            param.add(dataShopScanAStats.getLargeArea());
            param.add(dataShopScanAStats.getShopScanInQuantity());
            param.add(dataShopScanAStats.getShopSignQuantity());
            param.add(dataShopScanAStats.getShopJoinInQuantity());
            param.add(dataShopScanAStats.getShopJoinInRate());
            param.add(dataShopScanAStats.getShopScanOutQuantity());
            param.add(dataShopScanAStats.getShopJoinOutQuantity());
            param.add(dataShopScanAStats.getShopJoinOutRate());
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
