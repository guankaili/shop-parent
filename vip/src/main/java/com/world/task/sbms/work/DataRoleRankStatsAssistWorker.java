package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.DataRoleRankStats;
import com.world.model.sbms.DataRoleRankStatsAssistRawStats;
import com.world.task.sbms.thread.DataRoleRankStatsAssistStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/26 14:01
 * @desc 生成森麒麟业务员信息 用于辅助排名
 **/
public class DataRoleRankStatsAssistWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    private static final String RELO_ID = "921a5417-a374-44e2-a617-396e77959ff7";

    public DataRoleRankStatsAssistWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        log.info("获取排名信息:辅助获取开始");
        if (workFlag) {
            try {
                workFlag = false;

                //组装森麒麟用户数据，用于查询排行
                //userid 对应 channel_id
                String sqlSql = " SELECT user_id AS userId,dealer_code AS dealerCode FROM cbm_mag_l_user_dealer where user_id is not null ";
                List<Bean> beans = Data.Query("weilai_uaa", sqlSql, null, DataRoleRankStatsAssistRawStats.class);
                if (StringUtil.isNotEmpty(beans)) {
                    List<DataRoleRankStatsAssistRawStats> uaas = ObjectConversion.copy(beans, DataRoleRankStatsAssistRawStats.class);

                    //再区查询uua角色表
                    String uuaSql = " SELECT DISTINCT t1.user_id AS userId,t3.id AS roleId FROM cbm_mag_l_user_dealer t1 " +
                            "LEFT JOIN cbm_mag_l_user_role t2 ON t1.user_id = t2.user_id " +
                            "LEFT JOIN cbm_mag_role t3 ON t2.role_id = t3.id ";
                    List<Bean> uaaBeans = Data.Query("weilai_uaa", uuaSql, null, DataRoleRankStatsAssistRawStats.class);
                    if (StringUtil.isNotEmpty(uaaBeans)) {
                        List<DataRoleRankStatsAssistRawStats> backends = ObjectConversion.copy(uaaBeans, DataRoleRankStatsAssistRawStats.class);
                        if (StringUtil.isNotEmpty(backends)) {
                            uaas.forEach(item1 -> {
                                backends.forEach(item2 -> {
                                    if (item1.getUserId().equals(item2.getUserId())) {
                                        //判断权限
                                        if (item2.getRoleId().equals(RELO_ID)) {
                                            item1.setRoleType(2);
                                        } else {
                                            item1.setRoleType(3);
                                        }

                                        //构建线程池
                                        //当提交的任务数量为1000的时候，会开辟20个线程数

                                        ExecutorService executorService = Executors.newFixedThreadPool(10);
                                        CountDownLatch countDownLatch = new CountDownLatch(uaas.size());

                                        //业务处理线程
                                        for (DataRoleRankStatsAssistRawStats data : uaas) {
                                            DataRoleRankStatsAssistStatsThread dataRoleRankStatsAssistStatsThread = new DataRoleRankStatsAssistStatsThread(data, countDownLatch);
                                            executorService.submit(dataRoleRankStatsAssistStatsThread);
                                        }
                                        try {
                                            countDownLatch.await();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        /*关闭线程池*/
                                        executorService.shutdown();

                                    }
                                });
                            });
                        }
                    }
                }
            } catch (Exception e) {
                log.error("获取排名信息:辅助获取开始", e);
            } finally {
                workFlag = true;
            }
        }
    }

    public static void main(String[] args) {
        DataRoleRankStatsAssistWorker dataRoleRankStatsAssistWorker = new DataRoleRankStatsAssistWorker("dataRoleRankStatsAssistWorker", "生产排名信息辅助");
        dataRoleRankStatsAssistWorker.run();
    }

}
