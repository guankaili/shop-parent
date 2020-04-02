package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataDealerCmIdStatus;
import com.world.model.sbms.DataMainMStats;
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
 * @date 2020/3/20 11:06
 * @desc 数据首页统计 --门店统计，扫码统计
 **/
public class DataMainShopMStatsThread extends Thread {
    private static Logger logger = Logger.getLogger(DataMainShopMStatsThread.class);
    private DataMainMStats dataMainMStats;
    private CountDownLatch countDownLatch;
    private Map<String,String> map;

    public DataMainShopMStatsThread(DataMainMStats dataMainMStats, CountDownLatch countDownLatch, Map<String,String> map) {
        this.dataMainMStats = dataMainMStats;
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
            if (map.containsKey(dataMainMStats.getDealerCmId())) {
                startSql = " UPDATE ";
                endSql = " WHERE dealer_cm_id = '" + dataMainMStats.getDealerCmId() + "' ";
            } else {
                //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }
            sql.append(""+startSql+" data_home_shop_stats SET ");
            if (dataMainMStats.getDealerCode() != null){param.add(dataMainMStats.getDealerCode());sql.append(" dealer_code = ?, ");}
            if (dataMainMStats.getDealerName() != null){param.add(dataMainMStats.getDealerName());sql.append(" dealer_name = ?, ");}
            if (dataMainMStats.getDealerCmId() != null){param.add(dataMainMStats.getDealerCmId());sql.append(" dealer_cm_id = ?, ");}
            if (dataMainMStats.getLargeAreaCode() != null){param.add(dataMainMStats.getLargeAreaCode());sql.append(" large_area_code = ?, ");}
            if (dataMainMStats.getLargeArea() != null){param.add(dataMainMStats.getLargeArea());sql.append(" large_area_name = ?, ");}
            param.add(dataMainMStats.getShopQuantity());sql.append(" shop_quantity = ?, ");
            param.add(dataMainMStats.getActiveShopQuantity());sql.append(" active_shop_quantity = ?, ");
            param.add(dataMainMStats.getAddShopQuantity());sql.append(" add_shop_quantity = ?, ");
            param.add(dataMainMStats.getScanInQuantity());sql.append(" scan_in_quantity = ?, ");
            param.add(dataMainMStats.getScanOutQuantity());sql.append(" scan_out_quantity = ?, ");
            param.add(dataMainMStats.getScanJoinShopQuantity());sql.append(" scan_join_shop_quantity = ?, ");
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
            logger.error("数据首页统计 --门店统计，扫码统计 数据新增失败", e);
        } finally {
            countDownLatch.countDown();
        }

    }
}
