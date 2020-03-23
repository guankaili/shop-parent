package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopAddAFromStats;
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
 * @date 2020/3/22 10:47
 * @desc 新增门店统计 数据插入
 **/
public class DataShopAddAStatsThread extends Thread{

    private static Logger logger = Logger.getLogger(DataShopAddAStatsThread.class);
    private DataShopAddAFromStats dataShopAddAFromStats;
    private CountDownLatch countDownLatch;

    public DataShopAddAStatsThread(DataShopAddAFromStats dataShopAddAFromStats, CountDownLatch countDownLatch) {
        this.dataShopAddAFromStats = dataShopAddAFromStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try {
            String sql = "INSERT INTO data_shop_add_stats (" +
                    "dealer_code," +
                    "dealer_name," +
                    "dealer_cm_id," +
                    "large_area_code," +
                    "large_area_name," +
                    "`year`," +
                    "shop_add_quantity_m1," +
                    "shop_add_quantity_m2," +
                    "shop_add_quantity_m3," +
                    "shop_add_quantity_m4," +
                    "shop_add_quantity_m5," +
                    "shop_add_quantity_m6," +
                    "shop_add_quantity_m7," +
                    "shop_add_quantity_m8," +
                    "shop_add_quantity_m9," +
                    "shop_add_quantity_m10," +
                    "shop_add_quantity_m11," +
                    "shop_add_quantity_m12," +
                    "create_date " +
                    ")" +
                    "VALUES" +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            List<Object> param = new ArrayList<>();
            param.add(dataShopAddAFromStats.getDealerCode());
            param.add(dataShopAddAFromStats.getDealerName());
            param.add(dataShopAddAFromStats.getDealerCmId());
            param.add(dataShopAddAFromStats.getLargeAreaCode());
            param.add(dataShopAddAFromStats.getLargeArea());
            param.add(dataShopAddAFromStats.getShopAddYear());
            param.add(dataShopAddAFromStats.getShopAddQuantityM1());
            param.add(dataShopAddAFromStats.getShopAddQuantityM2());
            param.add(dataShopAddAFromStats.getShopAddQuantityM3());
            param.add(dataShopAddAFromStats.getShopAddQuantityM4());
            param.add(dataShopAddAFromStats.getShopAddQuantityM5());
            param.add(dataShopAddAFromStats.getShopAddQuantityM6());
            param.add(dataShopAddAFromStats.getShopAddQuantityM7());
            param.add(dataShopAddAFromStats.getShopAddQuantityM8());
            param.add(dataShopAddAFromStats.getShopAddQuantityM9());
            param.add(dataShopAddAFromStats.getShopAddQuantityM10());
            param.add(dataShopAddAFromStats.getShopAddQuantityM11());
            param.add(dataShopAddAFromStats.getShopAddQuantityM12());
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
