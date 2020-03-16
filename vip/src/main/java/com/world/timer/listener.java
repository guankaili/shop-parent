package com.world.timer;

import javax.servlet.ServletContextEvent;

import com.world.model.dao.task.TaskListener;
import com.world.task.auto.AutoFactory;

public class listener extends TaskListener {
    //销毁er{
    //时间间隔(一天)
    @SuppressWarnings("unused")
	private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    public void contextDestroyed(ServletContextEvent event) {
        log.info("主动关闭miner服务器》》》》》》》》》");
        super.contextDestroyed(event);
    }

    //初始化监听器
    public void contextInitialized(ServletContextEvent event) {
        AutoFactory.start();
        log.info("定时器容器已启动");
        log.info("已经添加任务");
        log.info("启动定时器");

    }
}
