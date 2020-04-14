package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopScanOutDetailDStats;
import com.world.util.StringUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private Map<String, String> map;

    public DataShopScanOutDetailDStatsThread(DataShopScanOutDetailDStats dataShopScanOutDetailDStats, CountDownLatch countDownLatch, Map<String, String> map) {
        this.dataShopScanOutDetailDStats = dataShopScanOutDetailDStats;
        this.countDownLatch = countDownLatch;
        this.map = map;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try {

            StringBuffer sql = new StringBuffer();

            //用于更新的sql
            String startSql = "";
            String endSql = "";

            //基本数据
            List<Object> param = new ArrayList<>();

            //更新操作
            if (map.containsKey(dataShopScanOutDetailDStats.getDealerCmId() + dataShopScanOutDetailDStats.getShopScanOutDetailDate())) {
                startSql = " UPDATE ";
                endSql = " WHERE dealer_cm_id = '" + dataShopScanOutDetailDStats.getDealerCmId() + "'  AND to_days(shop_scan_out_detail_date) = to_days(now()) ";
            } else {
                //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }

            sql.append("" + startSql + " data_scan_out_detail_stats SET ");
            if (dataShopScanOutDetailDStats.getDealerCode() != null) { param.add(dataShopScanOutDetailDStats.getDealerCode());sql.append(" dealer_code = ?, "); }
            if (dataShopScanOutDetailDStats.getDealerName() != null) { param.add(dataShopScanOutDetailDStats.getDealerName());sql.append(" dealer_name = ?, "); }
            if (dataShopScanOutDetailDStats.getDealerCmId() != null) { param.add(dataShopScanOutDetailDStats.getDealerCmId());sql.append(" dealer_cm_id = ?, "); }
            if (dataShopScanOutDetailDStats.getLargeAreaCode() != null) { param.add(dataShopScanOutDetailDStats.getLargeAreaCode());sql.append(" large_area_code = ?, "); }
            if (dataShopScanOutDetailDStats.getLargeArea() != null) { param.add(dataShopScanOutDetailDStats.getLargeArea());sql.append(" large_area_name = ?, "); }
            if (dataShopScanOutDetailDStats.getShopProvinceId() != null) { param.add(dataShopScanOutDetailDStats.getShopProvinceId());sql.append(" province_code = ?, "); }
            if (dataShopScanOutDetailDStats.getShopProvince() != null) { param.add(dataShopScanOutDetailDStats.getShopProvince());sql.append(" province_name = ?, "); }
            param.add(dataShopScanOutDetailDStats.getShopOutQuantity());sql.append(" shop_out_quantity = ?, ");
            param.add(dataShopScanOutDetailDStats.getShopSignQuantity());sql.append(" shop_sign_quantity = ?, ");
            param.add(dataShopScanOutDetailDStats.getShopJoinOutQuantity());sql.append(" shop_join_out_quantity = ?, ");
            if (dataShopScanOutDetailDStats.getShopJoinOutRate() != null) { param.add(dataShopScanOutDetailDStats.getShopJoinOutRate());sql.append(" shop_join_out_rate = ?, "); }
            param.add(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
            sql.append(" shop_scan_out_detail_date = ?, ");
            param.add(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            sql.append(" create_date = ? ");
            sql.append(" " + endSql + " ");
            sqls.add(new OneSql(sql.toString(), 1, param.toArray(), "sbms_main"));
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
