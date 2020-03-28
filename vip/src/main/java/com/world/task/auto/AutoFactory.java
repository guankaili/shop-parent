package com.world.task.auto;

import com.world.model.dao.task.TaskFactory;
import com.world.task.sbms.work.*;

public class AutoFactory extends TaskFactory {

    public static void start() {


        startAll();

        //数据 -- 首页
        work(new DataMainShopMStatsWorker("DataMainShopMStatsWorker", "数据模块首页,门店统计、扫码统计"), 1 * 60 * 1000);

        //门店统计 -- 首页
        work(new DataShopAStatsWorker("DataShopAStatsWorker", "门店统计首页,首页头部数据、门店类型饼状图、门店统计详情页"), 1 * 60 * 1000);

        //门店统计 -- 签约、抢购门店柱状图
        work(new DataShopSignBuyAStatsWorker("DataShopSignBuyAStatsWorker", "门店统计,签约、抢购门店柱状图"), 1 * 60 * 1000);

        //门店统计 -- 新增门店柱状图
        work(new DataShopAddAStatsWorker("DataShopAddAStatsWorker", "门店统计,新增门店柱状图"), 1 * 60 * 1000);

        //门店扫码 -- 首页
        work(new DataShopScanAStatsWorker("DataShopScanAStatsWorker", "门店扫码，首页数据"), 1 * 60 * 1000);

        //门店扫码 -- 扫码入库柱状图
        work(new DataShopScanInMYStatsWorker("DataShopScanInMYStatsWorker", "门店扫码，扫码入库柱状图"), 1 * 60 * 1000);

        //门店扫码 -- 扫码出库柱状图
        work(new DataShopScanOutMYStatsWorker("DataShopScanOutMYStatsWorker", "门店扫码，扫码出库柱状图"), 1 * 60 * 1000);

        //扫码统计 -- 扫码入库 -- 详情列表
        work(new DataShopScanInDetailDStatsWorker("DataShopScanInDetailDStatsWorker", "扫码入库详细列表"), 1 * 60 * 1000);

        //扫码统计 -- 扫码出库 -- 详细列表
        work(new DataShopScanOutDetailDStatsWorker("DataShopScanOutDetailDStatsWorker", "扫码出库详细列表"), 1 * 60 * 1000);

        //门店排行 -- 拉取用户权限数据
        work(new DataRoleRankStatsAssistWorker("DataRoleRankStatsAssistWorker", "拉取用户权限数据"), 1 * 60 * 1000);

        //门店排行 -- 经销商排行计算
        work(new DataRoleRankStatsDealerWorker("DataRoleRankStatsDealerWorker", "门店排行,计算经销商的排行"), 1 * 60 * 1000);

        //门店排行 -- 业务员排行计算
        work(new DataRoleRankStatsSalesWorker("DataRoleRankStatsSalesWorker", "门店排行，计算森麒麟业务员的排行"), 1 * 60 * 1000);

        //数据模块 -- 年进货量统计
        work(new DataDealerIgoodsStatsWorker("DataDealerIgoodsStatsWorker", "数据模块，年进货量统计"), 1 * 60 * 1000);

        //数据模块 -- 年出货量统计
        work(new DataDealerOgoodsStatsWorker("DataDealerOgoodsStatsWorker", "数据模块，年出货量统计"), 1 * 60 * 1000);

        //数据模块 -- 年退货量统计
        work(new DataDealerRgoodsStatsWorker("DataDealerRgoodsStatsWorker", "数据模块，年出退量统计"), 1 * 60 * 1000);

        //数据模块 -- 门店月度任务量统计
        work(new WorkHomeMStatsWorker("WorkHomeMStatsWorker", "数据模块，门店月度任务量统计"), 1 * 60 * 1000);

        //数据模块 -- 门店年度任务量统计
        work(new WorkHomeYStatsWorker("WorkHomeYStatsWorker", "数据模块，门店年度任务量统计"), 1 * 60 * 1000);


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
