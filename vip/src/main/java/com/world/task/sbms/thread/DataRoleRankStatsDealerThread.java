package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataMainMStats;
import com.world.model.sbms.DataRoleRankStats;
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
 * @date 2020/3/27 8:28
 * @desc
 **/
public class DataRoleRankStatsDealerThread extends Thread {

    private static Logger logger = Logger.getLogger(DataMainShopMStatsThread.class);
    private DataRoleRankStats dataRoleRankStats;
    private CountDownLatch countDownLatch;
    private int size;

    public DataRoleRankStatsDealerThread(DataRoleRankStats dataRoleRankStats, CountDownLatch countDownLatch, int size) {
        this.dataRoleRankStats = dataRoleRankStats;
        this.countDownLatch = countDownLatch;
        this.size = size;
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
            List<Object> list = txObj.excuteQuery(new OneSql("SELECT t1.id FROM data_role_rank_stats t1 " +
                    "WHERE t1.user_sign = '" + dataRoleRankStats.getUserSign() + "'  ", 1, null, "sbms_main"));
            //更新操作
            if (StringUtil.isNotEmpty(list)) {
                startSql = " UPDATE ";
                endSql = " WHERE user_sign = '" + dataRoleRankStats.getUserSign() + "' ";
            } else {
                //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }
            sql.append(""+startSql+" data_role_rank_stats SET ");
            sql.append(" country_quantity = '"+size+"', ");
            sql.append(" role_type = '1', ");
            if (dataRoleRankStats.getCountryRank() != null){param.add(dataRoleRankStats.getCountryRank());sql.append(" country_rank = ?, ");}
            if (dataRoleRankStats.getShopQuantity() != null){param.add(dataRoleRankStats.getShopQuantity());sql.append(" shop_quantity = ?, ");}
            if (dataRoleRankStats.getUserSign() != null){param.add(dataRoleRankStats.getUserSign());sql.append(" user_sign = ?, ");}
            if (dataRoleRankStats.getUserSignName() != null){param.add(dataRoleRankStats.getUserSignName());sql.append(" user_sign_name = ?, ");}
            sql.append(" create_date = '"+ DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss")+"' ");
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
