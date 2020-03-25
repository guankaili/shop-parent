package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopScanInMYFromStats;
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
 * @date 2020/3/21 10:47
 * @desc 门店
 **/
public class DataShopScanInMYStatsThread extends Thread {
    private static Logger logger = Logger.getLogger(DataShopScanAStatsThread.class);
    private DataShopScanInMYFromStats dataShopScanInMYFromStats;
    private CountDownLatch countDownLatch;

    //传入加工后的实体
    public DataShopScanInMYStatsThread(DataShopScanInMYFromStats dataShopScanInMYFromStats, CountDownLatch countDownLatch) {
        this.dataShopScanInMYFromStats = dataShopScanInMYFromStats;
        this.countDownLatch = countDownLatch;
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

            //查询是否存在这个 业务员的id
            List<Object> list = txObj.excuteQuery(new OneSql("SELECT t1.id FROM data_scan_in_stats t1 " +
                    "WHERE t1.dealer_cm_id = '" + dataShopScanInMYFromStats.getDealerCmId() + "' and t1.`year` = '"+
                    dataShopScanInMYFromStats.getScanYear()+"' ", 1, null, "sbms_main"));
            //更新操作
            if (StringUtil.isNotEmpty(list)) {
                startSql = " UPDATE ";
                endSql = " WHERE dealer_cm_id = '" + dataShopScanInMYFromStats.getDealerCmId() + "' ";
            } else {
                //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }

            sql.append(""+startSql+" data_scan_in_stats SET ");
            if (dataShopScanInMYFromStats.getDealerCode() != null){param.add(dataShopScanInMYFromStats.getDealerCode());sql.append(" dealer_code = ?, ");}
            if (dataShopScanInMYFromStats.getDealerName() != null){param.add(dataShopScanInMYFromStats.getDealerName());sql.append(" dealer_name = ?, ");}
            if (dataShopScanInMYFromStats.getDealerCmId() != null){param.add(dataShopScanInMYFromStats.getDealerCmId());sql.append(" dealer_cm_id = ?, ");}
            if (dataShopScanInMYFromStats.getLargeAreaCode() != null){param.add(dataShopScanInMYFromStats.getLargeAreaCode());sql.append(" large_area_code = ?, ");}
            if (dataShopScanInMYFromStats.getScanYear() != null){param.add(dataShopScanInMYFromStats.getScanYear());sql.append(" `year` = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM1() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM1());sql.append(" shop_in_quantity_m1 = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM2() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM2());sql.append(" shop_in_quantity_m2 = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM3() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM3());sql.append(" shop_in_quantity_m3 = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM4() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM4());sql.append(" shop_in_quantity_m4 = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM5() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM5());sql.append(" shop_in_quantity_m5 = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM6() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM6());sql.append(" shop_in_quantity_m6 = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM7() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM7());sql.append(" shop_in_quantity_m7 = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM8() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM8());sql.append(" shop_in_quantity_m8 = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM9() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM9());sql.append(" shop_in_quantity_m9 = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM10() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM10());sql.append(" shop_in_quantity_m10 = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM11() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM11());sql.append(" shop_in_quantity_m11 = ?, ");}
            if (dataShopScanInMYFromStats.getShopInQuantityM12() != null){param.add(dataShopScanInMYFromStats.getShopInQuantityM12());sql.append(" shop_in_quantity_m12 = ?, ");}
            sql.append(" create_date = '"+DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss")+"' ");
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
