package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.WorkDataMainMStats;
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
 * @date 2020/3/20 11:06
 * @desc 数据首页统计 --门店统计，扫码统计
 **/
public class WorkDataMainShopMStatsThread extends Thread {
    private static Logger logger = Logger.getLogger(WorkDataMainShopMStatsThread.class);
    private WorkDataMainMStats workDataMainMStats;
    private CountDownLatch countDownLatch;

    public WorkDataMainShopMStatsThread(WorkDataMainMStats workDataMainMStats, CountDownLatch countDownLatch) {
        this.workDataMainMStats = workDataMainMStats;
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
            param.add(workDataMainMStats.getDealerCode());
            param.add(workDataMainMStats.getDealerName());
            param.add(workDataMainMStats.getDealerCmId());
            param.add(workDataMainMStats.getLargeAreaCode());
            param.add(workDataMainMStats.getLargeArea());
            param.add(workDataMainMStats.getShopQuantity());
            param.add(workDataMainMStats.getActiveShopQuantity());
            param.add(workDataMainMStats.getAddShopQuantity());
            param.add(workDataMainMStats.getScanInQuantity());
            param.add(workDataMainMStats.getScanOutQuantity());
            param.add(workDataMainMStats.getScanJoinShopQuantity());
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
