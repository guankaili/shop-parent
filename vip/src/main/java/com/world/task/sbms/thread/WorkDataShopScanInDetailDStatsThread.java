package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.WorkDataShopScanAStats;
import com.world.model.sbms.WorkDataShopScanInDetailDStats;
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
 * @date 2020/3/22 13:01
 * @desc 门店扫码 入库 详情列表 定时任务
 **/
public class WorkDataShopScanInDetailDStatsThread extends Thread {

    private static Logger logger = Logger.getLogger(WorkDataShopScanAStatsThread.class);
    private WorkDataShopScanInDetailDStats workDataShopScanInDetailDStats;
    private CountDownLatch countDownLatch;

    public WorkDataShopScanInDetailDStatsThread(WorkDataShopScanInDetailDStats workDataShopScanInDetailDStats, CountDownLatch countDownLatch) {
        this.workDataShopScanInDetailDStats = workDataShopScanInDetailDStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try {
            String sql = "INSERT INTO data_scan_in_detail_stats ( " +
                    "province_code, " +
                    "province_name, " +
                    "dealer_code, " +
                    "dealer_name, " +
                    "dealer_cm_id, " +
                    "large_area_code, " +
                    "large_area_name, " +
                    "shop_in_quantity, " +
                    "shop_sign_quantity, " +
                    "shop_join_in_quantity, " +
                    "shop_join_in_rate, " +
                    "shop_scan_in_detail_date, " +
                    "create_date )" +
                    "VALUES" +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            List<Object> param = new ArrayList<>();
            param.add(workDataShopScanInDetailDStats.getShopProvinceId());
            param.add(workDataShopScanInDetailDStats.getShopProvince());
            param.add(workDataShopScanInDetailDStats.getDealerCode());
            param.add(workDataShopScanInDetailDStats.getDealerName());
            param.add(workDataShopScanInDetailDStats.getDealerCmId());
            param.add(workDataShopScanInDetailDStats.getLargeAreaCode());
            param.add(workDataShopScanInDetailDStats.getLargeArea());
            param.add(workDataShopScanInDetailDStats.getShopInQuantity());
            param.add(workDataShopScanInDetailDStats.getShopSignQuantity());
            param.add(workDataShopScanInDetailDStats.getShopJoinInQuantity());
            param.add(workDataShopScanInDetailDStats.getShopJoinInRate());
            param.add(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
            param.add(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
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
