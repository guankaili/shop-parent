package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.DataDealerCmIdStatus;
import com.world.model.sbms.DataRoleRankStats;
import com.world.task.sbms.thread.DataRoleRankStatsDealerThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/23 18:54
 * @desc 计算经销商的排名情况
 **/
public class DataRoleRankStatsDealerWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataRoleRankStatsDealerWorker(String name, String des) {
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
                        " t.dealer_code AS userSign, " +
                        " t.countryCount AS shopQuantity, " +
                        " (@rownum := @rownum + 1) AS countryRank " +
                        " FROM " +
                        " ( SELECT @rownum := 0 ) sqlvars, " +
                        " ( SELECT large_area_code, dealer_code, COUNT( shop_id ) countryCount FROM es_shop_detail GROUP BY dealer_code ORDER BY count( shop_id ) DESC ) t";
                List<Bean> beans = Data.Query("shop_member", dealerSql, null, DataRoleRankStats.class);
                if (StringUtil.isNotEmpty(beans)){
                    List<DataRoleRankStats> dataRoleRankStatsList = ObjectConversion.copy(beans, DataRoleRankStats.class);

                    //查询经销商name
                    String dealerNameSql = " SELECT t.dealer_code AS userSign,t.dealer_name AS userSignName FROM t_dealer_info t ";
                    List<Bean> nameBeans = Data.Query("qly", dealerNameSql, null, DataRoleRankStats.class);
                    if (StringUtil.isNotEmpty(nameBeans)){
                            List<DataRoleRankStats> names = ObjectConversion.copy(nameBeans, DataRoleRankStats.class);
                            dataRoleRankStatsList.forEach(item1 -> {
                                names.forEach(item2 -> {
                                    if (item1.getUserSign().equals(item2.getUserSign())) {
                                        item1.setUserSignName(item2.getUserSignName());
                                    }
                                });
                            });
                    }

                    //一次性获取中间表所有数据，判断新增或更新
                    Map<String,String> map = new ConcurrentHashMap<String, String>();
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
                        DataRoleRankStatsDealerThread dataRoleRankStatsDealerThread = new DataRoleRankStatsDealerThread(data,countDownLatch,beans.size(),map);
                        executorService.submit(dataRoleRankStatsDealerThread);
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
        DataRoleRankStatsDealerWorker dataRoleRankStatsDealerWorker = new DataRoleRankStatsDealerWorker("dataRoleRankStatsWorker", "门店统计首页统计月统计 数据同步");
        dataRoleRankStatsDealerWorker.run();
    }
}
