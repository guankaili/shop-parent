package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.WorkDataMainMStats;
import com.world.model.sbms.WorkDataShopAddAFromStats;
import com.world.model.sbms.WorkDataShopScanAStats;
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
public class WorkDataShopAddAStatsThread extends Thread{

    private static Logger logger = Logger.getLogger(WorkDataShopAddAStatsThread.class);
    private WorkDataShopAddAFromStats workDataShopAddAFromStats;
    private CountDownLatch countDownLatch;

    public WorkDataShopAddAStatsThread(WorkDataShopAddAFromStats workDataShopAddAFromStats, CountDownLatch countDownLatch) {
        this.workDataShopAddAFromStats = workDataShopAddAFromStats;
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
            param.add(workDataShopAddAFromStats.getDealerCode());
            param.add(workDataShopAddAFromStats.getDealerName());
            param.add(workDataShopAddAFromStats.getDealerCmId());
            param.add(workDataShopAddAFromStats.getLargeAreaCode());
            param.add(workDataShopAddAFromStats.getLargeArea());
            param.add(workDataShopAddAFromStats.getShopAddYear());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM1());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM2());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM3());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM4());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM5());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM6());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM7());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM8());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM9());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM10());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM11());
            param.add(workDataShopAddAFromStats.getShopAddQuantityM12());
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
