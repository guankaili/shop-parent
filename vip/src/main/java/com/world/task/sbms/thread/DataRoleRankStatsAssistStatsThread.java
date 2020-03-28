package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataRoleRankStatsAssistRawStats;
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
 * @date 2020/3/26 16:18
 * @desc
 **/
public class DataRoleRankStatsAssistStatsThread extends Thread {

    private static Logger logger = Logger.getLogger(DataRoleRankStatsAssistStatsThread.class);
    private DataRoleRankStatsAssistRawStats dataRoleRankStatsAssistRawStats;
    private CountDownLatch countDownLatch;

    public DataRoleRankStatsAssistStatsThread(DataRoleRankStatsAssistRawStats dataRoleRankStatsAssistRawStats, CountDownLatch countDownLatch) {
        this.dataRoleRankStatsAssistRawStats = dataRoleRankStatsAssistRawStats;
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
            List<Object> list = txObj.excuteQuery(new OneSql("SELECT t1.id FROM data_role_rank_assist_stats t1 " +
                    "WHERE t1.user_id = '"+ dataRoleRankStatsAssistRawStats.getUserId()+"' AND t1.dealer_code = '"+dataRoleRankStatsAssistRawStats.getDealerCode()+"' ", 1, null, "sbms_main"));
            //更新操作
            if (StringUtil.isNotEmpty(list)) {
                startSql = " UPDATE ";
                endSql = " WHERE user_id = '" + dataRoleRankStatsAssistRawStats.getUserId() + "' and dealer_code = '"+dataRoleRankStatsAssistRawStats.getDealerCode()+"' ";
            } else {
                //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }

            sql.append(""+startSql+" data_role_rank_assist_stats SET ");
            if (dataRoleRankStatsAssistRawStats.getRoleType() != null){param.add(dataRoleRankStatsAssistRawStats.getRoleType());sql.append(" role_type = ?, ");}
            if (dataRoleRankStatsAssistRawStats.getUserId() != null){param.add(dataRoleRankStatsAssistRawStats.getUserId());sql.append(" user_id = ?, ");}
            if (dataRoleRankStatsAssistRawStats.getDealerCode() != null){param.add(dataRoleRankStatsAssistRawStats.getDealerCode());sql.append(" dealer_code = ?, ");}
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
            logger.error("数据首页统计 --门店统计，扫码统计 数据新增失败", e);
        } finally {
            countDownLatch.countDown();
        }

    }
}
