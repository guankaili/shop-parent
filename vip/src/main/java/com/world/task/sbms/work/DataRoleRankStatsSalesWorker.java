package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.DataDealerCmIdStatus;
import com.world.model.sbms.DataRoleRankStats;
import com.world.task.sbms.thread.DataRoleRankStatsDealerThread;
import com.world.task.sbms.thread.DataRoleRankStatsSalesThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/23 18:54
 * @desc 用于计算森麒麟业务员的排名情况
 **/
public class DataRoleRankStatsSalesWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataRoleRankStatsSalesWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        log.info("门店统计首页:【获取门店排名情况】开始");
        if (workFlag) {
            try {
                workFlag = false;
                //获取所有经销商的排名情况
                String dealerSql = "SELECT " +
                        "  t.role_type AS roleType, " +
                        " t.userId AS userSign, " +
                        " t.countryCount AS `shopQuantity`, " +
                        " ( @rownum := @rownum + 1 ) AS countryRank  " +
                        " FROM " +
                        " ( SELECT @rownum := 0 ) sqlvars, " +
                        " ( " +
                        " SELECT " +
                        " t1.role_type, " +
                        " t1.user_id AS userId, " +
                        " SUM( t2.shop_quantity ) countryCount  " +
                        " FROM " +
                        " data_role_rank_assist_stats t1 " +
                        " LEFT JOIN data_role_rank_stats t2 ON t1.dealer_code = t2.user_sign  " +
                        " WHERE t2.role_type = 1 " +
                        " GROUP BY " +
                        " t1.user_id " +
                        " ORDER BY " +
                        "   SUM( t2.shop_quantity ) DESC  " +
                        " ) t";
                List<Bean> beans = Data.Query("sbms_main", dealerSql, null, DataRoleRankStats.class);
                if (StringUtil.isNotEmpty(beans)){
                    List<DataRoleRankStats> dataRoleRankStatsList = ObjectConversion.copy(beans, DataRoleRankStats.class);

                    //一次性获取中间表所有数据，判断新增或更新
                    Map<String,String> map = new HashMap<>();
                    String ifSql = " SELECT t1.user_sign AS userSign FROM data_role_rank_stats t1  ";
                    List<Bean> dealerCmIdStatuses = Data.Query("sbms_main", ifSql, null, DataDealerCmIdStatus.class);
                    if (StringUtil.isNotEmpty(dealerCmIdStatuses)){
                        List<DataDealerCmIdStatus> list = ObjectConversion.copy(dealerCmIdStatuses, DataDealerCmIdStatus.class);
                        map = list.stream().collect(Collectors.toMap(DataDealerCmIdStatus::getUserSign,DataDealerCmIdStatus::getUserSign));
                    }

                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(dataRoleRankStatsList.size());
                    for(DataRoleRankStats data : dataRoleRankStatsList){
                        //业务处理线程
                        DataRoleRankStatsSalesThread dataRoleRankStatsSalesThread = new DataRoleRankStatsSalesThread(data,countDownLatch,beans.size(),map);
                        executorService.submit(dataRoleRankStatsSalesThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                }
            } catch (Exception e) {
                log.error("门店任务数据同步失败", e);
            } finally {
                workFlag = true;
            }
        }
    }

    public static void main(String[] args) {
        DataRoleRankStatsSalesWorker dataRoleRankStatsDealerWorker = new DataRoleRankStatsSalesWorker("dataRoleRankStatsWorker", "门店统计首页统计月统计 数据同步");
        dataRoleRankStatsDealerWorker.run();
    }
}
