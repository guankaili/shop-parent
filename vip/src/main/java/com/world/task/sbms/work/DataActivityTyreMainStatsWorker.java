package com.world.task.sbms.work;

import com.world.model.dao.task.Worker;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/4/10 10:51
 * @desc 活动 主页数据 按活动类型区分
 **/
public class DataActivityTyreMainStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataActivityTyreMainStatsWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        log.info("活动:【拉取主页数据开始】开始");
        if (workFlag) {
            try {
                workFlag = false;


            } catch (Exception e) {
                log.error("活动，拉取主页数据出错", e);
            } finally {
                workFlag = true;
            }
        } else {
            log.info("活动:【拉取主页数据】上一轮任务尚未结束，本轮不需要运行");
        }
    }
}