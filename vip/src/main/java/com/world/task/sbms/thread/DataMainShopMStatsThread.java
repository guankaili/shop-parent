package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataMainMStats;
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
 * @date 2020/3/20 11:06
 * @desc 数据首页统计 --门店统计，扫码统计
 **/
public class DataMainShopMStatsThread extends Thread {
    private static Logger logger = Logger.getLogger(DataMainShopMStatsThread.class);
    private DataMainMStats dataMainMStats;
    private CountDownLatch countDownLatch;

    public DataMainShopMStatsThread(DataMainMStats dataMainMStats, CountDownLatch countDownLatch) {
        this.dataMainMStats = dataMainMStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try {
            String sql = "INSERT INTO data_home_shop_stats (" +
                    "dealer_code," +
                    "dealer_name," +
                    "dealer_cm_id," +
                    "large_area_code," +
                    "large_area_name," +
                    "shop_quantity," +
                    "active_shop_quantity," +
                    "add_shop_quantity," +
                    "scan_in_quantity," +
                    "scan_out_quantity," +
                    "scan_join_shop_quantity," +
                    "create_date " +
                    ")" +
                    "VALUES" +
                    "(?,?,?,?,?,?,?,?,?,?,?,?)";
            List<Object> param = new ArrayList<>();
            param.add(dataMainMStats.getDealerCode());
            param.add(dataMainMStats.getDealerName());
            param.add(dataMainMStats.getDealerCmId());
            param.add(dataMainMStats.getLargeAreaCode());
            param.add(dataMainMStats.getLargeArea());
            param.add(dataMainMStats.getShopQuantity());
            param.add(dataMainMStats.getActiveShopQuantity());
            param.add(dataMainMStats.getAddShopQuantity());
            param.add(dataMainMStats.getScanInQuantity());
            param.add(dataMainMStats.getScanOutQuantity());
            param.add(dataMainMStats.getScanJoinShopQuantity());
            param.add(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            sqls.add(new OneSql(sql, 1, param.toArray(), "sbms_main"));
            txObj.excuteUpdateList(sqls);

            if (txObj.commit()) {
                logger.info("插入成功");
            } else {
                logger.error("插入失败");
            }
        } catch (Exception e) {
            logger.error("数据首页统计 --门店统计，扫码统计 数据新增失败", e);
        } finally {
            countDownLatch.countDown();
        }

    }
}
