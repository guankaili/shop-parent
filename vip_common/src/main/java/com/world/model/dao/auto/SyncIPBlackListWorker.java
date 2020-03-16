package com.world.model.dao.auto;

import com.world.model.dao.task.Worker;
import com.world.web.defense.IpDefense;

public class SyncIPBlackListWorker extends Worker {

	private static final long serialVersionUID = -6510435749241398141L;

	public SyncIPBlackListWorker(String name, String des) {
		super(name, des);
	}

	@Override
	public void run() {
		// 同步黑白名单,5s同步一次
		IpDefense.syncFromDisk();
	}

}
