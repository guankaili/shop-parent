package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopScanOutMYFromStats;
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
public class DataShopScanOutMYStatsThread extends Thread {
    private static Logger logger = Logger.getLogger(DataShopScanAStatsThread.class);
    private DataShopScanOutMYFromStats dataShopScanOutMYFromStats;
    private CountDownLatch countDownLatch;

    //传入加工后的实体
    public DataShopScanOutMYStatsThread(DataShopScanOutMYFromStats dataShopScanOutMYFromStats, CountDownLatch countDownLatch) {
        this.dataShopScanOutMYFromStats = dataShopScanOutMYFromStats;
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
            param.add(dataShopScanOutMYFromStats.getDealerCode());
            param.add(dataShopScanOutMYFromStats.getDealerName());
            param.add(dataShopScanOutMYFromStats.getDealerCmId());
            param.add(dataShopScanOutMYFromStats.getLargeAreaCode());
            param.add(dataShopScanOutMYFromStats.getLargeAreaName());
            param.add(dataShopScanOutMYFromStats.getScanYear());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM1());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM2());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM3());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM4());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM5());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM6());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM7());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM8());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM9());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM10());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM11());
            param.add(dataShopScanOutMYFromStats.getShopOutQuantityM12());
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
