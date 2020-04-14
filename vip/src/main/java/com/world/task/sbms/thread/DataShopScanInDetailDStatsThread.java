package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopScanInDetailDStats;
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
 * @date 2020/3/22 13:01
 * @desc 门店扫码 入库 详情列表 定时任务
 **/
public class DataShopScanInDetailDStatsThread extends Thread {

    private static Logger logger = Logger.getLogger(DataShopScanAStatsThread.class);
    private DataShopScanInDetailDStats dataShopScanInDetailDStats;
    private CountDownLatch countDownLatch;
    private Map<String,String> map;

    public DataShopScanInDetailDStatsThread(DataShopScanInDetailDStats dataShopScanInDetailDStats, CountDownLatch countDownLatch, Map<String,String> map) {
        this.dataShopScanInDetailDStats = dataShopScanInDetailDStats;
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
            if (map.containsKey(dataShopScanInDetailDStats.getDealerCmId()+dataShopScanInDetailDStats.getShopScanInDetailDate())) {
                startSql = " UPDATE ";
                endSql = " WHERE dealer_cm_id = '" + dataShopScanInDetailDStats.getDealerCmId() + "' AND to_days(shop_scan_in_detail_date) = to_days(now())  ";
            } else {
                //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }

            sql.append(""+startSql+" data_scan_in_detail_stats SET ");
            if (dataShopScanInDetailDStats.getDealerCode() != null){param.add(dataShopScanInDetailDStats.getDealerCode());sql.append(" dealer_code = ?, ");}
            if (dataShopScanInDetailDStats.getDealerName() != null){param.add(dataShopScanInDetailDStats.getDealerName());sql.append(" dealer_name = ?, ");}
            if (dataShopScanInDetailDStats.getDealerCmId() != null){param.add(dataShopScanInDetailDStats.getDealerCmId());sql.append(" dealer_cm_id = ?, ");}
            if (dataShopScanInDetailDStats.getLargeAreaCode() != null){param.add(dataShopScanInDetailDStats.getLargeAreaCode());sql.append(" large_area_code = ?, ");}
            if (dataShopScanInDetailDStats.getLargeArea() != null){param.add(dataShopScanInDetailDStats.getLargeArea());sql.append(" large_area_name = ?, ");}
            if (dataShopScanInDetailDStats.getShopProvinceId() != null){param.add(dataShopScanInDetailDStats.getShopProvinceId());sql.append(" province_code = ?, ");}
            if (dataShopScanInDetailDStats.getShopProvince() != null){param.add(dataShopScanInDetailDStats.getShopProvince());sql.append(" province_name = ?, ");}
            param.add(dataShopScanInDetailDStats.getShopInQuantity());sql.append(" shop_in_quantity = ?, ");
            param.add(dataShopScanInDetailDStats.getShopSignQuantity());sql.append(" shop_sign_quantity = ?, ");
            param.add(dataShopScanInDetailDStats.getShopJoinInQuantity());sql.append(" shop_join_in_quantity = ?, ");
            if (dataShopScanInDetailDStats.getShopJoinInRate() != null){param.add(dataShopScanInDetailDStats.getShopJoinInRate());sql.append(" shop_join_in_rate = ?, ");}
            param.add(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));sql.append(" shop_scan_in_detail_date = ?, ");
            param.add(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));sql.append(" create_date = ? ");
            sql.append(" "+endSql+" ");
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
