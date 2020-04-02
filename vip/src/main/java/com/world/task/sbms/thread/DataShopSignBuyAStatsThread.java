package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopSignBuyAFromStats;
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
 * @date 2020/3/22 10:47
 * @desc 签约 抢购门店 数据统计
 **/
public class DataShopSignBuyAStatsThread extends Thread {

    private static Logger logger = Logger.getLogger(DataShopAddAStatsThread.class);
    private DataShopSignBuyAFromStats dataShopSignBuyAFromStats;
    private CountDownLatch countDownLatch;
    private Map<String,String> map;

    //传入加工后的实体
    public DataShopSignBuyAStatsThread(DataShopSignBuyAFromStats dataShopSignBuyAFromStats, CountDownLatch countDownLatch, Map<String,String> map) {
        this.dataShopSignBuyAFromStats = dataShopSignBuyAFromStats;
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
            if (map.containsKey(dataShopSignBuyAFromStats.getDealerCmId()+dataShopSignBuyAFromStats.getShopSignBuyYear())) {
                startSql = " UPDATE ";
                endSql = " WHERE dealer_cm_id = '" + dataShopSignBuyAFromStats.getDealerCmId() + "' AND `year` = '"+dataShopSignBuyAFromStats.getShopSignBuyYear()+"'";
            } else {
                //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }
            sql.append(""+startSql+" data_shop_signbuy_stats SET ");
            if (dataShopSignBuyAFromStats.getDealerCode() != null){param.add(dataShopSignBuyAFromStats.getDealerCode());sql.append(" dealer_code = ?, ");}
            if (dataShopSignBuyAFromStats.getDealerName() != null){param.add(dataShopSignBuyAFromStats.getDealerName());sql.append(" dealer_name = ?, ");}
            if (dataShopSignBuyAFromStats.getDealerCmId() != null){param.add(dataShopSignBuyAFromStats.getDealerCmId());sql.append(" dealer_cm_id = ?, ");}
            if (dataShopSignBuyAFromStats.getLargeAreaCode() != null){param.add(dataShopSignBuyAFromStats.getLargeAreaCode());sql.append(" large_area_code = ?, ");}
            if (dataShopSignBuyAFromStats.getLargeArea() != null){param.add(dataShopSignBuyAFromStats.getLargeArea());sql.append(" large_area_name = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignBuyYear()!= null){param.add(dataShopSignBuyAFromStats.getShopSignBuyYear());sql.append(" `year` = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM1() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM1());sql.append(" shop_buy_quantity_m1 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM1() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM1());sql.append(" shop_sign_quantity_m1 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM2() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM2());sql.append(" shop_buy_quantity_m2 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM2() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM2());sql.append(" shop_sign_quantity_m2 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM3() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM3());sql.append(" shop_buy_quantity_m3 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM3() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM3());sql.append(" shop_sign_quantity_m3 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM4() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM4());sql.append(" shop_buy_quantity_m4 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM4() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM4());sql.append(" shop_sign_quantity_m4 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM5() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM5());sql.append(" shop_buy_quantity_m5 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM5() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM5());sql.append(" shop_sign_quantity_m5 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM6() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM6());sql.append(" shop_buy_quantity_m6 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM6() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM6());sql.append(" shop_sign_quantity_m6 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM7() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM7());sql.append(" shop_buy_quantity_m7 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM7() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM7());sql.append(" shop_sign_quantity_m7 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM8() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM8());sql.append(" shop_buy_quantity_m8 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM8() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM8());sql.append(" shop_sign_quantity_m8 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM9() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM9());sql.append(" shop_buy_quantity_m9 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM9() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM9());sql.append(" shop_sign_quantity_m9 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM10() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM10());sql.append(" shop_buy_quantity_m10 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM10() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM10());sql.append(" shop_sign_quantity_m10 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM11() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM11());sql.append(" shop_buy_quantity_m11 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM11() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM11());sql.append(" shop_sign_quantity_m11 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopBuyQuantityM12() != null){param.add(dataShopSignBuyAFromStats.getShopBuyQuantityM12());sql.append(" shop_buy_quantity_m12 = ?, ");}
            if (dataShopSignBuyAFromStats.getShopSignQuantityM2() != null){param.add(dataShopSignBuyAFromStats.getShopSignQuantityM12());sql.append(" shop_sign_quantity_m12 = ?, ");}
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
