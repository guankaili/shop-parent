package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopScanAStats;
import com.world.util.StringUtil;
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
 * @date 2020/3/21 8:36
 * @desc 扫码统计主页
 **/
public class DataShopScanAStatsThread extends Thread {
    private static Logger logger = Logger.getLogger(DataShopScanAStatsThread.class);
    private DataShopScanAStats dataShopScanAStats;
    private CountDownLatch countDownLatch;

    public DataShopScanAStatsThread(DataShopScanAStats dataShopScanAStats, CountDownLatch countDownLatch) {
        this.dataShopScanAStats = dataShopScanAStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try{

            StringBuffer sql = new StringBuffer();

            //用于更新的sql
            String startSql = "";
            String endSql = "";

            //基本数据
            List<Object> param = new ArrayList<>();

            //查询是否存在这个 业务员的id
            List<Object> list = txObj.excuteQuery(new OneSql("SELECT t1.id FROM data_scan_home_stats t1 " +
                    "WHERE t1.dealer_cm_id = '" + dataShopScanAStats.getDealerCmId() + "' ", 1, null, "sbms_main"));
            //更新操作
            if (StringUtil.isNotEmpty(list)) {
                startSql = " UPDATE ";
                endSql = " WHERE dealer_cm_id = '" + dataShopScanAStats.getDealerCmId() + "' ";
            } else {
                //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }
            sql.append(""+startSql+" data_scan_home_stats SET ");
            if (dataShopScanAStats.getDealerCode() != null){param.add(dataShopScanAStats.getDealerCode());sql.append(" dealer_code = ?, ");}
            if (dataShopScanAStats.getDealerName() != null){param.add(dataShopScanAStats.getDealerName());sql.append(" dealer_name = ?, ");}
            if (dataShopScanAStats.getDealerCmId() != null){param.add(dataShopScanAStats.getDealerCmId());sql.append(" dealer_cm_id = ?, ");}
            if (dataShopScanAStats.getLargeAreaCode() != null){param.add(dataShopScanAStats.getLargeAreaCode());sql.append(" large_area_code = ?, ");}
            if (dataShopScanAStats.getLargeArea() != null){param.add(dataShopScanAStats.getLargeArea());sql.append(" large_area_name = ?, ");}
            param.add(dataShopScanAStats.getShopScanInQuantity());sql.append(" shop_scan_in_quantity = ?, ");
            param.add(dataShopScanAStats.getShopSignQuantity());sql.append(" shop_sign_quantity = ?, ");
            param.add(dataShopScanAStats.getShopJoinInQuantity());sql.append(" shop_join_in_quantity = ?, ");
            if (dataShopScanAStats.getShopJoinInRate() != null){param.add(dataShopScanAStats.getShopJoinInRate());sql.append(" shop_join_in_rate = ?, ");}
            param.add(dataShopScanAStats.getShopScanOutQuantity());sql.append(" shop_scan_out_quantity = ?, ");
            param.add(dataShopScanAStats.getShopJoinOutQuantity());sql.append(" shop_join_out_quantity = ?, ");
            if (dataShopScanAStats.getShopJoinOutRate() != null){param.add(dataShopScanAStats.getShopJoinOutRate());sql.append(" shop_join_out_rate = ?, ");}
            sql.append(" create_date = '"+DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss")+"' ");
            sql.append(" "+endSql+" ");
            sqls.add(new OneSql(sql.toString(), 1, param.toArray(), "sbms_main"));
            txObj.excuteUpdateList(sqls);
            if (txObj.commit()) {
                logger.info("插入成功");
            } else {
                logger.error("插入失败");
            }
        }catch (Exception e) {
            logger.error("数据新增失败",e);
        } finally {
            countDownLatch.countDown();
        }

    }
}
