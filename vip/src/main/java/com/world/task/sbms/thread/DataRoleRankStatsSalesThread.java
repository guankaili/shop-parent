package com.world.task.sbms.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataRoleRankStats;
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
 * @date 2020/3/27 10:36
 * @desc
 **/
public class DataRoleRankStatsSalesThread  extends Thread {

    private static Logger logger = Logger.getLogger(DataRoleRankStatsSalesThread.class);
    private DataRoleRankStats dataRoleRankStats;
    private CountDownLatch countDownLatch;
    private int size;
    private Map<String,String> map;

    public DataRoleRankStatsSalesThread(DataRoleRankStats dataRoleRankStats, CountDownLatch countDownLatch, int size, Map<String,String> map) {
        this.dataRoleRankStats = dataRoleRankStats;
        this.countDownLatch = countDownLatch;
        this.size = size;
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
            if (map.containsKey(dataRoleRankStats.getUserSign())) {
                startSql = " UPDATE ";
                endSql = " WHERE user_sign = '" + dataRoleRankStats.getUserSign() + "' ";
            } else {
                //插入操作
                startSql = " INSERT ";
                endSql = "  ";
            }

            sql.append(""+startSql+" data_role_rank_stats SET ");
            sql.append(" country_quantity = '"+size+"', ");
            sql.append(" role_type = '"+dataRoleRankStats.getRoleType()+"', ");
            if (dataRoleRankStats.getCountryRank() != null){
                if (dataRoleRankStats.getRoleType()==2){
                    param.add(dataRoleRankStats.getCountryRank());
                }else {
                    param.add(0);
                }
                sql.append(" country_rank = ?, ");
            }
            if (dataRoleRankStats.getShopQuantity() != null){
                if (dataRoleRankStats.getRoleType()==2) {
                    param.add(dataRoleRankStats.getShopQuantity());
                }else {
                    param.add(0);
                }
                sql.append(" shop_quantity = ?, ");
            }
            if (dataRoleRankStats.getUserSign() != null){param.add(dataRoleRankStats.getUserSign());sql.append(" user_sign = ?, ");}
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
