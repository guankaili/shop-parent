package com.world.task.auto;

import com.world.model.dao.task.TaskFactory;
import com.world.task.sbms.work.DataDealerIgoodsStatsWorker;
import com.world.task.sbms.work.DataDealerOgoodsStatsWorker;
import com.world.task.sbms.work.DataDealerRgoodsStatsWorker;
import com.world.task.sbms.work.DataMainShopMStatsWorker;
import com.world.task.sbms.work.DataRoleRankStatsAssistWorker;
import com.world.task.sbms.work.DataRoleRankStatsDealerWorker;
import com.world.task.sbms.work.DataRoleRankStatsSalesWorker;
import com.world.task.sbms.work.DataShopAStatsWorker;
import com.world.task.sbms.work.DataShopAddAStatsWorker;
import com.world.task.sbms.work.DataShopScanAStatsWorker;
import com.world.task.sbms.work.DataShopScanInDetailDStatsWorker;
import com.world.task.sbms.work.DataShopScanInMYStatsWorker;
import com.world.task.sbms.work.DataShopScanOutDetailDStatsWorker;
import com.world.task.sbms.work.DataShopScanOutMYStatsWorker;
import com.world.task.sbms.work.DataShopSignBuyAStatsWorker;
import com.world.task.sbms.work.WorkHomeMStatsWorker;
import com.world.task.sbms.work.WorkHomeYStatsWorker;
import com.world.task.shop.work.ConponStatusWork;
import com.world.task.shop.work.PanicBuyingShopWork;
import com.world.task.shop.work.ScanInShopWork;
import com.world.task.shop.work.ScanOutShopWork;

public class AutoFactory extends TaskFactory {

    public static void start() {


        startAll();

        //数据 -- 首页
        work(new DataMainShopMStatsWorker("DataMainShopMStatsWorker", "数据模块首页,门店统计、扫码统计"), 30 * 60 * 1000);

        //门店统计 -- 首页
        work(new DataShopAStatsWorker("DataShopAStatsWorker", "门店统计首页,首页头部数据、门店类型饼状图、门店统计详情页"), 30 * 60 * 1000);

        //门店统计 -- 签约、抢购门店柱状图
        work(new DataShopSignBuyAStatsWorker("DataShopSignBuyAStatsWorker", "门店统计,签约、抢购门店柱状图"), 30 * 60 * 1000);

        //门店统计 -- 新增门店柱状图
        work(new DataShopAddAStatsWorker("DataShopAddAStatsWorker", "门店统计,新增门店柱状图"), 30 * 60 * 1000);

        //门店扫码 -- 首页
        work(new DataShopScanAStatsWorker("DataShopScanAStatsWorker", "门店扫码，首页数据"), 30 * 60 * 1000);

        //门店扫码 -- 扫码入库柱状图
        work(new DataShopScanInMYStatsWorker("DataShopScanInMYStatsWorker", "门店扫码，扫码入库柱状图"), 30 * 60 * 1000);

        //门店扫码 -- 扫码出库柱状图
        work(new DataShopScanOutMYStatsWorker("DataShopScanOutMYStatsWorker", "门店扫码，扫码出库柱状图"), 30 * 60 * 1000);

        //扫码统计 -- 扫码入库 -- 详情列表
        work(new DataShopScanInDetailDStatsWorker("DataShopScanInDetailDStatsWorker", "扫码入库详细列表"), 30 * 60 * 1000);

        //扫码统计 -- 扫码出库 -- 详细列表
        work(new DataShopScanOutDetailDStatsWorker("DataShopScanOutDetailDStatsWorker", "扫码出库详细列表"), 30 * 60 * 1000);

        //门店排行 -- 拉取用户权限数据
        work(new DataRoleRankStatsAssistWorker("DataRoleRankStatsAssistWorker", "拉取用户权限数据"), 30 * 60 * 1000);

        //门店排行 -- 经销商排行计算
        work(new DataRoleRankStatsDealerWorker("DataRoleRankStatsDealerWorker", "门店排行,计算经销商的排行"), 30 * 60 * 1000);

        //门店排行 -- 业务员排行计算
        work(new DataRoleRankStatsSalesWorker("DataRoleRankStatsSalesWorker", "门店排行，计算森麒麟业务员的排行"), 30 * 60 * 1000);

        //数据模块 -- 年进货量统计
        work(new DataDealerIgoodsStatsWorker("DataDealerIgoodsStatsWorker", "数据模块，年进货量统计"), 30 * 60 * 1000);

        //数据模块 -- 年出货量统计
        work(new DataDealerOgoodsStatsWorker("DataDealerOgoodsStatsWorker", "数据模块，年出货量统计"), 30 * 60 * 1000);

        //数据模块 -- 年退货量统计
        work(new DataDealerRgoodsStatsWorker("DataDealerRgoodsStatsWorker", "数据模块，年出退量统计"), 30 * 60 * 1000);

        //数据模块 -- 门店月度任务量统计
        work(new WorkHomeMStatsWorker("WorkHomeMStatsWorker", "数据模块，门店月度任务量统计"), 30 * 60 * 1000);

        //数据模块 -- 门店年度任务量统计
        work(new WorkHomeYStatsWorker("WorkHomeYStatsWorker", "数据模块，门店年度任务量统计"), 30 * 60 * 1000);

        //门店优惠券 -- 根据过期时间失效掉优惠券
        work(new ConponStatusWork("ConponStatusWork", "门店优惠券，根据过期时间失效掉优惠券"), 1 * 60 * 1000);

        //门店状态  -- 两个月发生抢购行为变为抢购门店
        work(new PanicBuyingShopWork("PanicBuyingShopWork", "抢购门店状态，两个月发生抢购行为变为抢购门店"), 1 * 60 * 1000);


        /**
         * 预展示扫码入库计算
         * 预展示扫码退货计算
         */
        work(new ScanInShopWork("ScanInShopWork", "预展示扫码入库计算"), 3 * 1000);
        work(new ScanOutShopWork("ScanOutShopWork", "预展示扫码退货计算"), 3 * 1000);

    }

    public static void main(String[] args) {


    }
}
