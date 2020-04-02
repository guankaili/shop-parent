package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopAStats;
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
 * @date 2020/3/22 13:03
 * @desc 门店统计首页 数据插入
 **/
public class DataShopAStatsThread extends Thread{

    private static Logger logger = Logger.getLogger(DataShopAStatsThread.class);
    private DataShopAStats dataShopAStats;
    private CountDownLatch countDownLatch;
    private Map<String,String> map;

    public DataShopAStatsThread(DataShopAStats dataShopAStats, CountDownLatch countDownLatch, Map<String,String> map) {
        this.dataShopAStats = dataShopAStats;
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
            if (map.containsKey(dataShopAStats.getDealerCmId())) {
                startSql = " UPDATE ";
                endSql = " WHERE dealer_cm_id = '" + dataShopAStats.getDealerCmId() + "' ";
            } else {
                //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }
            sql.append(""+startSql+" data_shop_detail_stats SET ");
            if (dataShopAStats.getDealerCode() != null){param.add(dataShopAStats.getDealerCode());sql.append(" dealer_code = ?, ");}
            if (dataShopAStats.getDealerName() != null){param.add(dataShopAStats.getDealerName());sql.append(" dealer_name = ?, ");}
            if (dataShopAStats.getDealerCmId() != null){param.add(dataShopAStats.getDealerCmId());sql.append(" dealer_cm_id = ?, ");}
            if (dataShopAStats.getLargeAreaCode() != null){param.add(dataShopAStats.getLargeAreaCode());sql.append(" large_area_code = ?, ");}
            if (dataShopAStats.getLargeArea() != null){param.add(dataShopAStats.getLargeArea());sql.append(" large_area_name = ?, ");}
            if (dataShopAStats.getShopProvinceId() != null){param.add(dataShopAStats.getShopProvinceId());sql.append(" province_code = ?, ");}
            if (dataShopAStats.getShopProvince() != null){param.add(dataShopAStats.getShopProvince());sql.append(" province_name = ?, ");}
            if (dataShopAStats.getShopTotalQuantity() != null){param.add(dataShopAStats.getShopTotalQuantity());sql.append(" shop_total_quantity = ?, ");}
            if (dataShopAStats.getShopPanicQuantity() != null){param.add(dataShopAStats.getShopPanicQuantity());sql.append(" shop_panic_quantity = ?, ");}
            if (dataShopAStats.getShopAddQuantity() != null){param.add(dataShopAStats.getShopAddQuantity());sql.append(" shop_add_quantity = ?, ");}
            if (dataShopAStats.getShopAddPanicQuantity() != null){param.add(dataShopAStats.getShopAddPanicQuantity());sql.append(" shop_add_panic_quantity = ?, ");}
            param.add(dataShopAStats.getCtsARate());sql.append(" cts_a_rate = ?, ");
            param.add(dataShopAStats.getCtsRate());sql.append(" cts_rate = ?, ");
            param.add(dataShopAStats.getCcsARate());sql.append(" ccs_a_rate = ?, ");
            param.add(dataShopAStats.getCcsRate());sql.append(" ccs_rate = ?, ");
            param.add(dataShopAStats.getCpsRate());sql.append(" cps_rate = ?, ");
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
