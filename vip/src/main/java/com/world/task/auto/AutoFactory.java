package com.world.task.auto;

import com.world.model.dao.task.TaskFactory;

public class AutoFactory extends TaskFactory {

    public static void start() {


        startAll();



        /**
         * 扫描层级人数
         * SetUserInvitaionNum
         */
        //work(new SetUserInvitaionNum("SetUserInvitaionNum", "扫描层级人数"), 1 * 60 * 1000);
        
        /**
         * 动态奖金(建点,指导,晋升)分配-跑的时候开(cal分配区间)
         * DynamicBonusDisWork
         */
//        work(new DynamicBonusResetCalWork("DynamicBonusResetCalWork", "动态奖金(建点,指导,晋升)重新计算"), 10 * 60 * 1000);
//        work(new DynamicBonusDisWork("DynamicBonusDisWork", "动态奖金(建点,指导,晋升)结算"), 10 * 60 * 1000);
        
        /**
         * 理财-生态参与回馈-分配定时任务（10分钟一次）
         * 理财-生态参与回馈-结算定时任务（10分钟一次）
         */
//        work(new EcoRewardAssignWork("EcoRewardAssignWork", "生态参与回馈分配定时任务"), 20 * 60 * 1000);
//        work(new EcoRewardBillWork("EcoRewardBillWork", "生态参与回馈结算定时任务"), 10 * 60 * 1000);

    }
    
    public static void main(String[] args) {
       
    }
}
