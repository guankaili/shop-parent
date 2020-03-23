package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopAStats;
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
public class DataShopAStatsThread extends Thread{

    private static Logger logger = Logger.getLogger(DataShopAStatsThread.class);
    private DataShopAStats dataShopAStats;
    private CountDownLatch countDownLatch;

    public DataShopAStatsThread(DataShopAStats dataShopAStats, CountDownLatch countDownLatch) {
        this.dataShopAStats = dataShopAStats;
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
            param.add(dataShopAStats.getDealerCode());
            param.add(dataShopAStats.getDealerName());
            param.add(dataShopAStats.getDealerCmId());
            param.add(dataShopAStats.getLargeAreaCode());
            param.add(dataShopAStats.getLargeArea());
            param.add(dataShopAStats.getShopTotalQuantity());
            param.add(dataShopAStats.getShopPanicQuantity());
            param.add(dataShopAStats.getShopAddQuantity());
            param.add(dataShopAStats.getShopAddPanicQuantity());
            param.add(dataShopAStats.getAreaRank());
            param.add(dataShopAStats.getAreaQuantity());
            param.add(dataShopAStats.getCountryRank());
            param.add(dataShopAStats.getCountryQuantity());
            param.add(dataShopAStats.getCtsARate());
            param.add(dataShopAStats.getCtsRate());
            param.add(dataShopAStats.getCcsARate());
            param.add(dataShopAStats.getCcsRate());
            param.add(dataShopAStats.getCpsRate());
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
