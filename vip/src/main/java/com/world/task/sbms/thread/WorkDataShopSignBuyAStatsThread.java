package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.WorkDataShopAddAFromStats;
import com.world.model.sbms.WorkDataShopScanOutMYFromStats;
import com.world.model.sbms.WorkDataShopSignBuyAFromStats;
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
 * @desc 签约 抢购门店 数据统计
 **/
public class WorkDataShopSignBuyAStatsThread extends Thread {

    private static Logger logger = Logger.getLogger(WorkDataShopAddAStatsThread.class);
    private WorkDataShopSignBuyAFromStats workDataShopSignBuyAFromStats;
    private CountDownLatch countDownLatch;

    //传入加工后的实体
    public WorkDataShopSignBuyAStatsThread(WorkDataShopSignBuyAFromStats workDataShopSignBuyAFromStats, CountDownLatch countDownLatch) {
        this.workDataShopSignBuyAFromStats = workDataShopSignBuyAFromStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try {
            String sql = "INSERT INTO data_shop_signbuy_stats (" +
                    "dealer_code, " +
                    "dealer_name, " +
                    "dealer_cm_id, " +
                    "large_area_code, " +
                    "large_area_name, " +
                    "`year`, " +
                    "shop_buy_quantity_m1, " +
                    "shop_sign_quantity_m1, " +
                    "shop_buy_quantity_m2, " +
                    "shop_sign_quantity_m2, " +
                    "shop_buy_quantity_m3, " +
                    "shop_sign_quantity_m3, " +
                    "shop_buy_quantity_m4, " +
                    "shop_sign_quantity_m4, " +
                    "shop_buy_quantity_m5, " +
                    "shop_sign_quantity_m5, " +
                    "shop_buy_quantity_m6, " +
                    "shop_sign_quantity_m6, " +
                    "shop_buy_quantity_m7, " +
                    "shop_sign_quantity_m7, " +
                    "shop_buy_quantity_m8, " +
                    "shop_sign_quantity_m8, " +
                    "shop_buy_quantity_m9, " +
                    "shop_sign_quantity_m9, " +
                    "shop_buy_quantity_m10, " +
                    "shop_sign_quantity_m10, " +
                    "shop_buy_quantity_m11, " +
                    "shop_sign_quantity_m11, " +
                    "shop_buy_quantity_m12, " +
                    "shop_sign_quantity_m12, " +
                    "create_date " +
                    ") " +
                    "VALUES " +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

            List<Object> param = new ArrayList<>();
            param.add(workDataShopSignBuyAFromStats.getDealerCode());
            param.add(workDataShopSignBuyAFromStats.getDealerName());
            param.add(workDataShopSignBuyAFromStats.getDealerCmId());
            param.add(workDataShopSignBuyAFromStats.getLargeAreaCode());
            param.add(workDataShopSignBuyAFromStats.getLargeArea());
            param.add(workDataShopSignBuyAFromStats.getShopSignBuyYear());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM1());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM1());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM2());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM2());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM3());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM3());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM4());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM4());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM5());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM5());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM6());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM6());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM7());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM7());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM8());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM8());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM9());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM9());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM10());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM10());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM11());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM11());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM12());
            param.add(workDataShopSignBuyAFromStats.getShopBuyQuantityM12());
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
