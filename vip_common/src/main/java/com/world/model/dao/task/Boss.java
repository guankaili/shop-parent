package com.world.model.dao.task;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class Boss{
	public static Logger log = Logger.getLogger(Boss.class.getName());
	private Worker worker;
	//private Thread department;
	public ScheduledExecutorService  service;
	
	public ScheduledFuture<?>  scheduledFuture;

	public void initDepartment(){
		//department = new Thread(this);
	}
	
	//开始工作
	public void start(String name){
//		if(department == null){
//			initDepartment();
//		}
//		department.start();
//		department.setName(name);
		
		if(!worker.autoReplace){
			if(worker.timer == null){
				worker.timer = new Timer(worker.getName() + "-timer");
			}
			worker.timer.schedule(worker , 0 , worker.workFrequency);
		}else{
			if(service == null){
				service = Executors.newSingleThreadScheduledExecutor();
			}
			scheduledFuture = service.scheduleAtFixedRate(new Runnable() {
				public void run() {
					try {
						if(!worker.stop){
							//log.info("线程池启动====================================================");
							worker.run();
						}else{
							log.info("已关闭的线程池====================================================");
						}
					} catch (Exception e) {
						log.error(e.toString(), e);
					}
				}
			}, 0 , worker.workFrequency , TimeUnit.MILLISECONDS);
		}
	}
	
	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public void run() {
		
	}

}
