package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.WorkDataShopScanInMYFromStats;
import com.world.model.sbms.WorkDataShopScanOutMYFromStats;
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
public class WorkDataShopScanOutMYStatsThread extends Thread {
    private static Logger logger = Logger.getLogger(WorkDataShopScanAStatsThread.class);
    private WorkDataShopScanOutMYFromStats workDataShopScanOutMYFromStats;
    private CountDownLatch countDownLatch;

    //传入加工后的实体
    public WorkDataShopScanOutMYStatsThread(WorkDataShopScanOutMYFromStats workDataShopScanOutMYFromStats, CountDownLatch countDownLatch) {
        this.workDataShopScanOutMYFromStats = workDataShopScanOutMYFromStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try {
            String sql = "INSERT INTO data_scan_out_stats (" +
                    "dealer_code," +
                    "dealer_name," +
                    "dealer_cm_id," +
                    "large_area_code," +
                    "large_area_name," +
                    "`year`," +
                    "shop_out_quantity_m1," +
                    "shop_out_quantity_m2," +
                    "shop_out_quantity_m3," +
                    "shop_out_quantity_m4," +
                    "shop_out_quantity_m5," +
                    "shop_out_quantity_m6," +
                    "shop_out_quantity_m7," +
                    "shop_out_quantity_m8," +
                    "shop_out_quantity_m9," +
                    "shop_out_quantity_m10," +
                    "shop_out_quantity_m11," +
                    "shop_out_quantity_m12," +
                    "create_date " +
                    ")" +
                    "VALUES" +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            List<Object> param = new ArrayList<>();
            param.add(workDataShopScanOutMYFromStats.getDealerCode());
            param.add(workDataShopScanOutMYFromStats.getDealerName());
            param.add(workDataShopScanOutMYFromStats.getDealerCmId());
            param.add(workDataShopScanOutMYFromStats.getLargeAreaCode());
            param.add(workDataShopScanOutMYFromStats.getLargeAreaName());
            param.add(workDataShopScanOutMYFromStats.getScanYear());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM1());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM2());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM3());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM4());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM5());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM6());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM7());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM8());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM9());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM10());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM11());
            param.add(workDataShopScanOutMYFromStats.getShopOutQuantityM12());
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
