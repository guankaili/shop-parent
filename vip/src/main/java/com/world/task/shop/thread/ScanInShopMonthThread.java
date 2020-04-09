package com.world.task.shop.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.world.data.mysql.Bean;
import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.shop.ScanBatchRecordDetailModel;
import com.world.model.shop.ShopConfIntegralModel;
import com.world.model.shop.ShopConfRebateModel;
import com.world.model.shop.ShopConfTaskMModel;
import com.world.model.shop.ShopDetailModel;
import com.world.task.shop.util.ScanUtil;

public class ScanInShopMonthThread extends Thread {
    private static Logger log = Logger.getLogger(ScanInShopThread.class);
    /*sql语句*/
    private String sql = "";
    /**报警msg*/
//    private  String msg = "";
    private CountDownLatch countDownLatch;
    private ScanBatchRecordDetailModel scanBatchRecordDetailModel;
    //计算月份格式：2020-03
    private String calMonth;
    //扫码工具栏
    private ScanUtil scanUtil;
    /**
     * 全局使用
     */
    //已扫码数量，没扫1次加1，全局使用
    private int hasScanInNum;
    //已扫码的积分
    private int calScanIntegralTotalScore;
    //已扫码的返利
    private int calScanRebateTotalScore;

    public ScanInShopMonthThread(ScanBatchRecordDetailModel scanBatchRecordDetailModel, CountDownLatch countDownLatch, String calMonth) {
        this.countDownLatch = countDownLatch;
        this.scanBatchRecordDetailModel = scanBatchRecordDetailModel;
        this.calMonth = calMonth;
        this.scanUtil = new ScanUtil();
        this.hasScanInNum = 0;
        this.calScanIntegralTotalScore = 0;
        this.calScanRebateTotalScore = 0;
    }

    @Override
    public void run() {
        log.info("ScanInShopMonthThread...run...scanBatchRecordDetailModel = " + JSON.toJSONString(scanBatchRecordDetailModel));
        /**门店ID*/
        int shopId = scanBatchRecordDetailModel.getShop_id();
        
        try {
        	//开启事务处理
            List<OneSql> sqls = new ArrayList<OneSql>();
            TransactionObject txObj = new TransactionObject();
            long startTime = System.currentTimeMillis();
        	
            /**
             * 月度处理
             */
            this.scanMonthDeal(sqls);

            if (null != sqls && sqls.size() > 0) {
                //执行事务
                txObj.excuteUpdateList(sqls);
                if (txObj.commit()) {
                    log.info("扫码月度结算报警REWARDINFO:【扫码月度结算处理】处理成功，门店ID【" + shopId + "】");
                } else {
                    log.info("扫码月度结算报警REWARDERROR:【扫码月度结算处理】处理失败，门店ID【" + shopId + "】");
                }
            }

            long endTime = System.currentTimeMillis();
            log.info("扫码月度结算报警REWARDInfo:【扫码月度结算处理】门店ID【" + shopId + "】处理耗时【" + (endTime - startTime) + "】");
        } catch (Exception e) {
            log.info("扫码入库报警REWARDERROR:ScanInShopMonthThread", e);
        } finally {
            countDownLatch.countDown();
        }
    }
    
    public void scanMonthDeal(List<OneSql> sqls) throws Exception {
    	/**门店ID*/
        int shopId = scanBatchRecordDetailModel.getShop_id();
    	//门店类型
        int shopType = 1;
        //门店信息
        ShopDetailModel shopDetailModel = scanUtil.findShopDetail(shopId);
        shopType = null == shopDetailModel.getShop_type() ? 1 : shopDetailModel.getShop_type();
    	
        /**
         * 查询门店月度任务量
         */
        ShopConfTaskMModel shopConfTaskMModel = scanUtil.findShopConfTaskM(shopType);
    	
        /**
         * 获取本月正常的扫码入库记录
         * `flow_state` int(11) DEFAULT '0' COMMENT '订单状态：1-正常;2-门店入库退货;3-门店未入库退货;'   不需要
         * `scan_type` int(11) DEFAULT NULL COMMENT '扫码种类：1-经销商出库;2-经销商退货扫码;3-门店入库;4-门店出库',
         */
        List<Bean> scanBatchRecordDetailModelList = scanUtil.listScanBatchRecordDetail(shopId, calMonth);
        
        /**检查月度任务量是否完成*/
        boolean checkMonthTaskFlag = this.checkMonthTask(scanBatchRecordDetailModelList.size(), shopType, shopConfTaskMModel);
        log.info("checkMonthTaskFlag = " + checkMonthTaskFlag);
        if(!checkMonthTaskFlag) {
        	log.info("扫码月结报警REWARDINFO:【门店月度任务未完成，不能获得相应积分和返利】");
        	return;
        }
        
        /**
         * 先删除已有数据，防止重复计算
         */
        this.delMonthPreScore(sqls, shopDetailModel);
        
        /**月结循环处理*/
        this.calMonthScore(sqls, scanBatchRecordDetailModelList, shopConfTaskMModel, shopDetailModel);

        /**将月结预计算值更新到表 es_member_integral */
        this.updateMonthPreScore(sqls, shopDetailModel);

        /** 将月结预计算记录保存到表 es_member_integral_month */
        this.saveMemberIntegralMonth(sqls, shopDetailModel);
    }

    public void saveMemberIntegralMonth(List<OneSql> sqls, ShopDetailModel shopDetailModel) {
        int memberId = shopDetailModel.getMember_id();

        //积分
        sql = "delete from es_member_integral_month "
            + "where member_id = " + memberId + " and integral_type = 5 and cal_pre_month = '" + calMonth + "' and create_way = 1 ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, -2, null, "shop_trade"));

        sql = "insert into es_member_integral_month ("
            + "member_id, member_name, shop_id, shop_name, integral_type, integral_type_name, balance, "
            + "freeze, cal_pre_num, cal_pre_score, cal_pre_month, create_way, create_datetime)  "
            + "select member_id, member_name, shop_id, shop_name, 5, '积分', " + calScanIntegralTotalScore + ", "
            + "0, " + hasScanInNum + ", " + calScanIntegralTotalScore + ", '" + calMonth + "', 1, now() "
            + "from es_member_integral where member_id = " + memberId + " and integral_type = 5 ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));

        //返利
        sql = "delete from es_member_integral_month "
            + "where member_id = " + memberId + " and integral_type = 6 and cal_pre_month = '" + calMonth + "' and create_way = 1 ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, -2, null, "shop_trade"));

        sql = "insert into es_member_integral_month ("
                + "member_id, member_name, shop_id, shop_name, integral_type, integral_type_name, balance, "
                + "freeze, cal_pre_num, cal_pre_score, cal_pre_month, create_way, create_datetime)  "
                + "select member_id, member_name, shop_id, shop_name, 6, '返利', " + calScanRebateTotalScore + ", "
                + "0, " + hasScanInNum + ", " + calScanRebateTotalScore + ", '" + calMonth + "', 1, now() "
                + "from es_member_integral where member_id = " + memberId + " and integral_type = 6 ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));
    }

    private void updateMonthPreScore(List<OneSql> sqls, ShopDetailModel shopDetailModel) {
        //买家ID
        int memberId = shopDetailModel.getMember_id();

        //更新积分
        sql = "update es_member_integral set cal_pre_score = " + calScanIntegralTotalScore + ", cal_pre_month = '" + calMonth + "' "
            + "where member_id = " + memberId + " and integral_type = 5 ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));

        //更新返利
        sql = "update es_member_integral set cal_pre_score = " + calScanRebateTotalScore + ", cal_pre_month = '" + calMonth + "' "
            + "where member_id = " + memberId + " and integral_type = 6 ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));
    }

    private void delMonthPreScore(List<OneSql> sqls, ShopDetailModel shopDetailModel) {
        //买家ID
        int memberId = shopDetailModel.getMember_id();

        sql = "delete from es_member_integral_month_bill where member_id = " + memberId + " and cal_month = '" + calMonth + "' ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, -2, null, "shop_trade"));
    }


    public void calMonthScore(List<OneSql> sqls, List<Bean> scanBatchRecordDetailModelList,
			ShopConfTaskMModel shopConfTaskMModel, ShopDetailModel shopDetailModel) throws Exception {
    	//扫码入库记录
        ScanBatchRecordDetailModel scanBatchRecordDetailModel = null;
        for (int i = 0; i < scanBatchRecordDetailModelList.size(); i++) {
            scanBatchRecordDetailModel = (ScanBatchRecordDetailModel) scanBatchRecordDetailModelList.get(i);
            
            /**
             * 按照扫码顺序，逐一处理
             */
            this.calOneByOne(sqls, scanBatchRecordDetailModel, shopConfTaskMModel, shopDetailModel);
        }
        log.info("hasScanInNum = " + hasScanInNum);
        log.info("calScanIntegralTotalScore = " + calScanIntegralTotalScore);
        log.info("calScanRebateTotalScore = " + calScanRebateTotalScore);
	}
    
    public void calOneByOne(List<OneSql> sqls, ScanBatchRecordDetailModel scanBatchRecordDetailModel, 
    		ShopConfTaskMModel shopConfTaskMModel, ShopDetailModel shopDetailModel) throws Exception {
    	//门店类型
        int shopType = shopDetailModel.getShop_type();
        //买家ID
        int memberId = shopDetailModel.getMember_id();
        
    	/**拆分获取商品size*/
        int goodsSizeInt = scanUtil.splitGoodsSize(scanBatchRecordDetailModel);
        
        /**
         * 按照当前已扫描数量和门店类型，计算扫描此条应获得的积分，可升级
         */
        int calScanIntegralScore = scanUtil.produceScanIntegralScore(hasScanInNum, shopConfTaskMModel, shopType, goodsSizeInt);
        /**
         * 按照当前已扫描数量和门店类型，计算扫描此条应获得的返利，可升级
         */
        int calScanRebateScore = scanUtil.produceScanRebateScore(hasScanInNum, shopConfTaskMModel, shopType, goodsSizeInt);
    	log.info("calScanIntegralScore = " + calScanIntegralScore + ", calScanRebateScore = " + calScanRebateScore);
        
        /**
         * 积分配置：按门店类型和size匹配对应积分分值，原始配置
         */
        ShopConfIntegralModel shopConfIntegralModel = scanUtil.findShopConfIntegral(shopType, goodsSizeInt);
        //对应的积分值
        int confIntegralScore = null == shopConfIntegralModel.getScore() ? 0 : shopConfIntegralModel.getScore();
        log.info("confIntegralScore = " + confIntegralScore);

        /**
         * 返利配置：按门店类型和size匹配对应返利分值，原始配置
         */
        ShopConfRebateModel shopConfRebateModel = scanUtil.findShopConfRebate(shopType, goodsSizeInt);
        //对应的积分值
        int confRebateScore = shopConfRebateModel.getScore();
        log.info("confRebateScore = " + confRebateScore);
        
        /**
         * 记录日志
         * TODO
         */
        long scanId = scanBatchRecordDetailModel.getId();
        this.produceCurMonthPre(sqls, scanId, memberId, goodsSizeInt, 
        		calScanIntegralScore, calScanRebateScore, confIntegralScore, confRebateScore);
        
        /**
         * 扫码数量，积分，返利
         */
        //计数加1
        this.hasScanInNum++;
        //已扫码的积分
        calScanIntegralTotalScore = calScanIntegralTotalScore + calScanIntegralScore;
        //已扫码的返利
        calScanRebateTotalScore = calScanRebateTotalScore + calScanRebateScore;
	}
    
    public void produceCurMonthPre(List<OneSql> sqls, long scanId, int memberId, int goodsSizeInt, 
    		int calScanIntegralScore, int calScanRebateScore, int confIntegralScore, int confRebateScore) {
    	/**
         * 生成流水
         * 虚拟积分类型：0-默认；7预积分；8-预返利；
         * 流水类型：0-默认；1-业务增加；2-业务减少；
         */
        //积分流水
        sql = "insert into es_member_integral_month_bill "
            + "(member_id, member_name, integral_type, bill_type, amount, balance, fee, "
            + "exchange_amount, busi_describe, busi_id, cal_month, busi_date, bill_date) "
            + "select member_id, member_name, 7, 1, " + calScanIntegralScore + ", (0), " + goodsSizeInt + ", "
            + "" + confIntegralScore + ", '扫码入库获得预展示积分-尺寸=" + goodsSizeInt + "', " + scanId + ", '" + calMonth + "', now(), now() "
            + "from es_member_integral where member_id = " + memberId + " and integral_type = 5 ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));
        
        //返利流水
        sql = "insert into es_member_integral_month_bill "
            + "(member_id, member_name, integral_type, bill_type, amount, balance, fee, "
            + "exchange_amount, busi_describe, busi_id, cal_month, busi_date, bill_date) "
            + "select member_id, member_name, 8, 1, " + calScanRebateScore + ", (0), " + goodsSizeInt + ", "
            + "" + confRebateScore + ", '扫码入库获得预展示返利-尺寸=" + goodsSizeInt + "', " + scanId + ", '" + calMonth + "', now(), now() "
            + "from es_member_integral where member_id = " + memberId + " and integral_type = 6 ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));
    }
    
    private boolean checkMonthTask(int monthScanInNum, int shopType, ShopConfTaskMModel shopConfTaskMModel ) throws Exception {
    	//check返回值
    	boolean checkMonthTaskFlag = false;
    	
    	//月度任务量
        int shopTaskM = shopConfTaskMModel.getShop_task_m();
        log.info("monthScanInNum = " + monthScanInNum + ", shopTaskM = " + shopTaskM);
        if (monthScanInNum >= shopTaskM) {
        	checkMonthTaskFlag = true;
        }
        
        return checkMonthTaskFlag;
	}

	
}
