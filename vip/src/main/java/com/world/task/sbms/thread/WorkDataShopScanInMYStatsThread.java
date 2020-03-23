package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.WorkDataShopScanInMYFromStats;
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
 * @date 2020/3/21 10:47
 * @desc 门店
 **/
public class WorkDataShopScanInMYStatsThread extends Thread {
    private static Logger logger = Logger.getLogger(WorkDataShopScanAStatsThread.class);
    private WorkDataShopScanInMYFromStats workDataShopScanInMYFromStats;
    private CountDownLatch countDownLatch;

    //传入加工后的实体
    public WorkDataShopScanInMYStatsThread(WorkDataShopScanInMYFromStats workDataShopScanInMYFromStats, CountDownLatch countDownLatch) {
        this.workDataShopScanInMYFromStats = workDataShopScanInMYFromStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try {
            String sql = "INSERT INTO data_scan_in_stats (" +
                    "dealer_code," +
                    "dealer_name," +
                    "dealer_cm_id," +
                    "large_area_code," +
                    "large_area_name," +
                    "`year`," +
                    "shop_in_quantity_m1," +
                    "shop_in_quantity_m2," +
                    "shop_in_quantity_m3," +
                    "shop_in_quantity_m4," +
                    "shop_in_quantity_m5," +
                    "shop_in_quantity_m6," +
                    "shop_in_quantity_m7," +
                    "shop_in_quantity_m8," +
                    "shop_in_quantity_m9," +
                    "shop_in_quantity_m10," +
                    "shop_in_quantity_m11," +
                    "shop_in_quantity_m12," +
                    "create_date " +
                    ")" +
                    "VALUES" +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            List<Object> param = new ArrayList<>();
            param.add(workDataShopScanInMYFromStats.getDealerCode());
            param.add(workDataShopScanInMYFromStats.getDealerName());
            param.add(workDataShopScanInMYFromStats.getDealerCmId());
            param.add(workDataShopScanInMYFromStats.getLargeAreaCode());
            param.add(workDataShopScanInMYFromStats.getLargeAreaName());
            param.add(workDataShopScanInMYFromStats.getScanYear());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM1());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM2());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM3());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM4());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM5());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM6());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM7());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM8());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM9());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM10());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM11());
            param.add(workDataShopScanInMYFromStats.getShopInQuantityM12());
            param.add(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            sqls.add(new OneSql(sql, 1, param.toArray(), "sbms_main"));
            txObj.excuteUpdateList(sqls);
            if (txObj.commit()) {
                logger.info("插入成功");
            } else {
                logger.error("插入失败");
            }

        } catch (Exception e) {
            logger.error("数据新增失败", e);
        } finally {
            countDownLatch.countDown();
        }


    }
}
