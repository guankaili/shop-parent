package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopScanOutDetailDStats;
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
 * @date 2020/3/22 13:02
 * @desc 门店扫码 出库 详情列表 定时任务
 **/
public class DataShopScanOutDetailDStatsThread extends Thread {

    private static Logger logger = Logger.getLogger(DataShopScanAStatsThread.class);
    private DataShopScanOutDetailDStats dataShopScanOutDetailDStats;
    private CountDownLatch countDownLatch;

    public DataShopScanOutDetailDStatsThread(DataShopScanOutDetailDStats dataShopScanOutDetailDStats, CountDownLatch countDownLatch) {
        this.dataShopScanOutDetailDStats = dataShopScanOutDetailDStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();

        String sql = "INSERT INTO data_scan_out_detail_stats ( " +
                "province_code, " +
                "province_name, " +
                "dealer_code, " +
                "dealer_name, " +
                "dealer_cm_id, " +
                "large_area_code, " +
                "large_area_name, " +
                "shop_out_quantity, " +
                "shop_sign_quantity, " +
                "shop_join_out_quantity, " +
                "shop_join_out_rate, " +
                "shop_scan_out_detail_date, " +
                "create_date )" +
                "VALUES" +
                "(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        List<Object> param = new ArrayList<>();
        param.add(dataShopScanOutDetailDStats.getShopProvinceId());
        param.add(dataShopScanOutDetailDStats.getShopProvince());
        param.add(dataShopScanOutDetailDStats.getDealerCode());
        param.add(dataShopScanOutDetailDStats.getDealerName());
        param.add(dataShopScanOutDetailDStats.getDealerCmId());
        param.add(dataShopScanOutDetailDStats.getLargeAreaCode());
        param.add(dataShopScanOutDetailDStats.getLargeArea());
        param.add(dataShopScanOutDetailDStats.getShopOutQuantity());
        param.add(dataShopScanOutDetailDStats.getShopSignQuantity());
        param.add(dataShopScanOutDetailDStats.getShopJoinOutQuantity());
        param.add(dataShopScanOutDetailDStats.getShopJoinOutRate());
        param.add(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
        param.add(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        sqls.add(new OneSql(sql, 1, param.toArray(), "sbms_main"));
        txObj.excuteUpdateList(sqls);
        if (txObj.commit()) {
            logger.info("插入成功");
        } else {
            logger.error("插入失败");
        }
    }

}
