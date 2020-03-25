package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopAddAFromStats;
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
 * @date 2020/3/22 10:47
 * @desc 新增门店统计 数据插入
 **/
public class DataShopAddAStatsThread extends Thread {

    private static Logger logger = Logger.getLogger(DataShopAddAStatsThread.class);
    private DataShopAddAFromStats dataShopAddAFromStats;
    private CountDownLatch countDownLatch;

    public DataShopAddAStatsThread(DataShopAddAFromStats dataShopAddAFromStats, CountDownLatch countDownLatch) {
        this.dataShopAddAFromStats = dataShopAddAFromStats;
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
            List<Object> list = txObj.excuteQuery(new OneSql("SELECT t1.id FROM data_shop_add_stats t1 " +
                    "WHERE t1.dealer_cm_id = '" + dataShopAddAFromStats.getDealerCmId() + "' and t1.`year` = '"+dataShopAddAFromStats.getShopAddYear()+"' ", 1, null, "sbms_main"));
            //更新操作
            if (StringUtil.isNotEmpty(list)) {
                startSql = " UPDATE ";
                endSql = " WHERE dealer_cm_id = '" + dataShopAddAFromStats.getDealerCmId() + "' AND `year` = '"+dataShopAddAFromStats.getShopAddYear()+"' ";
            } else {
            //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }
            sql.append(""+startSql+" data_shop_add_stats SET ");
            if (dataShopAddAFromStats.getDealerCode() != null){param.add(dataShopAddAFromStats.getDealerCode());sql.append(" dealer_code = ?, ");}
            if (dataShopAddAFromStats.getDealerName() != null){param.add(dataShopAddAFromStats.getDealerName());sql.append(" dealer_name = ?, ");}
            if (dataShopAddAFromStats.getDealerCmId() != null){param.add(dataShopAddAFromStats.getDealerCmId());sql.append(" dealer_cm_id = ?, ");}
            if (dataShopAddAFromStats.getLargeAreaCode() != null){param.add(dataShopAddAFromStats.getLargeAreaCode());sql.append(" large_area_code = ?, ");}
            if (dataShopAddAFromStats.getLargeArea() != null){param.add(dataShopAddAFromStats.getLargeArea());sql.append(" large_area_name = ?, ");}
            if (dataShopAddAFromStats.getShopAddYear() != null){param.add(dataShopAddAFromStats.getShopAddYear());sql.append(" `year` = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM1() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM1());sql.append(" shop_add_quantity_m1 = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM2() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM2());sql.append(" shop_add_quantity_m2 = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM3() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM3());sql.append(" shop_add_quantity_m3 = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM4() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM4());sql.append(" shop_add_quantity_m4 = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM5() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM5());sql.append(" shop_add_quantity_m5 = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM6() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM6());sql.append(" shop_add_quantity_m6 = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM7() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM7());sql.append(" shop_add_quantity_m7 = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM8() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM8());sql.append(" shop_add_quantity_m8 = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM9() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM9());sql.append(" shop_add_quantity_m9 = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM10() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM10());sql.append(" shop_add_quantity_m10 = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM11() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM11());sql.append(" shop_add_quantity_m11 = ?, ");}
            if (dataShopAddAFromStats.getShopAddQuantityM12() != null){param.add(dataShopAddAFromStats.getShopAddQuantityM12());sql.append(" shop_add_quantity_m12 = ?, ");}
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
