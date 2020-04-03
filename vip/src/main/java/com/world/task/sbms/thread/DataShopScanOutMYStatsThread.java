package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopScanOutMYFromStats;
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
 * @date 2020/3/21 10:47
 * @desc 门店
 **/
public class DataShopScanOutMYStatsThread extends Thread {
    private static Logger logger = Logger.getLogger(DataShopScanAStatsThread.class);
    private DataShopScanOutMYFromStats dataShopScanOutMYFromStats;
    private CountDownLatch countDownLatch;
    private Map<String,String> map;

    //传入加工后的实体
    public DataShopScanOutMYStatsThread(DataShopScanOutMYFromStats dataShopScanOutMYFromStats, CountDownLatch countDownLatch, Map<String,String> map) {
        this.dataShopScanOutMYFromStats = dataShopScanOutMYFromStats;
        this.countDownLatch = countDownLatch;
        this.map=map;
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
            if (map.containsKey(dataShopScanOutMYFromStats.getDealerCmId()+dataShopScanOutMYFromStats.getScanYear())) {
                startSql = " UPDATE ";
                endSql = " WHERE dealer_cm_id = '" + dataShopScanOutMYFromStats.getDealerCmId() + "' ";
            } else {
                //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }

            sql.append(""+startSql+" data_scan_out_stats SET ");
            if (dataShopScanOutMYFromStats.getDealerCode() != null){param.add(dataShopScanOutMYFromStats.getDealerCode());sql.append(" dealer_code = ?, ");}
            if (dataShopScanOutMYFromStats.getDealerName() != null){param.add(dataShopScanOutMYFromStats.getDealerName());sql.append(" dealer_name = ?, ");}
            if (dataShopScanOutMYFromStats.getDealerCmId() != null){param.add(dataShopScanOutMYFromStats.getDealerCmId());sql.append(" dealer_cm_id = ?, ");}
            if (dataShopScanOutMYFromStats.getLargeAreaCode() != null){param.add(dataShopScanOutMYFromStats.getLargeAreaCode());sql.append(" large_area_code = ?, ");}
            if (dataShopScanOutMYFromStats.getScanYear() != null){param.add(dataShopScanOutMYFromStats.getScanYear());sql.append(" `year` = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM1() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM1());sql.append(" shop_out_quantity_m1 = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM2() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM2());sql.append(" shop_out_quantity_m2 = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM3() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM3());sql.append(" shop_out_quantity_m3 = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM4() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM4());sql.append(" shop_out_quantity_m4 = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM5() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM5());sql.append(" shop_out_quantity_m5 = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM6() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM6());sql.append(" shop_out_quantity_m6 = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM7() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM7());sql.append(" shop_out_quantity_m7 = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM8() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM8());sql.append(" shop_out_quantity_m8 = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM9() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM9());sql.append(" shop_out_quantity_m9 = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM10() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM10());sql.append(" shop_out_quantity_m10 = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM11() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM11());sql.append(" shop_out_quantity_m11 = ?, ");}
            if (dataShopScanOutMYFromStats.getShopOutQuantityM12() != null){param.add(dataShopScanOutMYFromStats.getShopOutQuantityM12());sql.append(" shop_out_quantity_m12 = ?, ");}
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
