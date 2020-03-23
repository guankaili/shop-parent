package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.WorkDataShopAStats;
import com.world.model.sbms.WorkDataShopAddAFromStats;
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
 * @date 2020/3/22 13:03
 * @desc 门店统计首页 数据插入
 **/
public class WorkDataShopAStatsThread extends Thread{

    private static Logger logger = Logger.getLogger(WorkDataShopAStatsThread.class);
    private WorkDataShopAStats workDataShopAStats;
    private CountDownLatch countDownLatch;

    public WorkDataShopAStatsThread(WorkDataShopAStats workDataShopAStats, CountDownLatch countDownLatch) {
        this.workDataShopAStats = workDataShopAStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try {
            String sql = "INSERT INTO data_shop_detail_stats (" +
                    "dealer_code, " +
                    "dealer_name, " +
                    "dealer_cm_id, " +
                    "large_area_code, " +
                    "large_area_name, " +
                    "shop_total_quantity, " +
                    "shop_panic_quantity, " +
                    "shop_add_quantity, " +
                    "shop_add_panic_quantity, " +
                    "area_rank, " +
                    "area_quantity, " +
                    "country_rank, " +
                    "country_quantity, " +
                    "cts_a_rate, " +
                    "cts_rate, " +
                    "ccs_a_rate, " +
                    "ccs_rate, " +
                    "cps_rate, " +
                    "create_date " +
                    ") " +
                    "VALUES " +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

            List<Object> param = new ArrayList<>();
            param.add(workDataShopAStats.getDealerCode());
            param.add(workDataShopAStats.getDealerName());
            param.add(workDataShopAStats.getDealerCmId());
            param.add(workDataShopAStats.getLargeAreaCode());
            param.add(workDataShopAStats.getLargeArea());
            param.add(workDataShopAStats.getShopTotalQuantity());
            param.add(workDataShopAStats.getShopPanicQuantity());
            param.add(workDataShopAStats.getShopAddQuantity());
            param.add(workDataShopAStats.getShopAddPanicQuantity());
            param.add(workDataShopAStats.getAreaRank());
            param.add(workDataShopAStats.getAreaQuantity());
            param.add(workDataShopAStats.getCountryRank());
            param.add(workDataShopAStats.getCountryQuantity());
            param.add(workDataShopAStats.getCtsARate());
            param.add(workDataShopAStats.getCtsRate());
            param.add(workDataShopAStats.getCcsARate());
            param.add(workDataShopAStats.getCcsRate());
            param.add(workDataShopAStats.getCpsRate());
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
