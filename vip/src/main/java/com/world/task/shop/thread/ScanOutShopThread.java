package com.world.task.shop.thread;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.world.data.mysql.Data;
import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.shop.MemberCouponModel;
import com.world.model.shop.MemberIntegralModel;
import com.world.model.shop.ScanBatchRecordDetailModel;
import com.world.model.shop.ShopConfIntegralModel;
import com.world.model.shop.ShopConfRebateModel;
import com.world.model.shop.ShopConfTaskMModel;
import com.world.model.shop.ShopDetailModel;
import com.world.task.shop.util.ScanUtil;
import com.world.util.date.TimeUtil;

public class ScanOutShopThread extends Thread {
    private static Logger log = Logger.getLogger(ScanOutShopThread.class);
    /**sql语句*/
    private String sql = "";
    /**报警msg*/
    private  String msg = "";
    private CountDownLatch countDownLatch;
    private ScanBatchRecordDetailModel scanBatchRecordDetailModel;
    //扫码工具栏
    private ScanUtil scanUtil;

    public ScanOutShopThread(ScanBatchRecordDetailModel scanBatchRecordDetailModel, CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.scanBatchRecordDetailModel = scanBatchRecordDetailModel;
        this.scanUtil = new ScanUtil();
    }

    @Override
    public void run() {
        log.info("ScanOutShopThread...run...scanBatchRecordDetailModel = " + JSON.toJSONString(scanBatchRecordDetailModel));
        /**扫码明细表主键*/
        Long scanId = scanBatchRecordDetailModel.getId();

        try {
            //开启事务处理
            List<OneSql> sqls = new ArrayList<OneSql>();
            TransactionObject txObj = new TransactionObject();
            long startTime = System.currentTimeMillis();

            /**
             * 扫码退货处理
             * sqls别轻易使用
             * `flow_state` int(11) DEFAULT '0' COMMENT '订单状态：1-正常;2-门店入库退货;3-门店未入库退货;'	1不需要
             */
            this.scanOutDeal(scanBatchRecordDetailModel, sqls);
            //更新扫码明细表数据状态
            this.updateScanDetailFlagTS(sqls, scanId, 1, "扫码退货处理成功");
            
            //执行事务
            txObj.excuteUpdateList(sqls);
            if (txObj.commit()) {
                log.info("扫码退货报警REWARDINFO:【扫码退货处理】处理成功，扫码ID【" + scanId + "】");
            } else {
                log.info("扫码退货报警REWARDERROR:【扫码退货处理】处理失败，扫码ID【" + scanId + "】");
            }

            long endTime = System.currentTimeMillis();
            log.info("扫码退货报警REWARDInfo:【扫码退货处理】扫码ID【" + scanId + "】处理耗时【" + (endTime - startTime) + "】");
        } catch (Exception e) {
            log.info("扫码退货报警REWARDERROR:ScanInDealerThread", e);
            this.updateScanDetailFlag(scanId, 2, msg);
        } finally {
            countDownLatch.countDown();
        }
    }
    
    private void scanOutDeal(ScanBatchRecordDetailModel scanBatchRecordDetailModel, List<OneSql> sqls) throws Exception {
    	//门店ID
        int shopId = scanBatchRecordDetailModel.getShop_id();
        /**
         * `flow_state` int(11) DEFAULT '0' COMMENT '订单状态：1-正常;2-门店入库退货;3-门店未入库退货;'	1不需要
         */
        
        //门店信息
        ShopDetailModel shopDetailModel = scanUtil.findShopDetail(shopId);
        //门店类型
        int shopType = shopDetailModel.getShop_type();
        //买家ID
        int memberId = shopDetailModel.getMember_id();

        /**
         * 积分&返利表
         * 查询积分，获取当前月份已扫码条数
         * scan_in_expect_num 扫码退货数量
         * 积分和返利扫码条数相同，取1个即可 = 当前已扫码数量
         */
        MemberIntegralModel memberIntegralScore = scanUtil.findMemberIntegralScore(memberId);
        int curScanInExpectNum = 0;
        //积分和返利扫码条数相同，取1个即可 = 当前已扫码数量
        curScanInExpectNum = null == memberIntegralScore.getScan_in_expect_num() ? 0 : memberIntegralScore.getScan_in_expect_num();
        log.info("curScanInExpectNum = " + curScanInExpectNum);

        /**
         * 查询门店月度任务量
         */
        ShopConfTaskMModel shopConfTaskMModel = scanUtil.findShopConfTaskM(shopType);
        
        /**拆分获取商品size*/
        int goodsSizeInt = scanUtil.splitGoodsSize(scanBatchRecordDetailModel);

        /**
         * 按照当前已扫描数量和门店类型，计算扫描此条应获得的积分
         */
        int calScanIntegralScore = scanUtil.produceScanIntegralScore(curScanInExpectNum - 1, shopConfTaskMModel, shopType, goodsSizeInt);
        /**
         * 按照当前已扫描数量和门店类型，计算扫描此条应获得的返利
         */
        int calScanRebateScore = scanUtil.produceScanRebateScore(curScanInExpectNum - 1, shopConfTaskMModel, shopType, goodsSizeInt);

        
        /**
         * 积分配置：按门店类型和size匹配对应积分分值
         */
        ShopConfIntegralModel shopConfIntegralModel = scanUtil.findShopConfIntegral(shopType, goodsSizeInt);
        //对应的积分值
        int confIntegralScore = shopConfIntegralModel.getScore();
        log.info("confIntegralScore = " + confIntegralScore);

        /**
         * 返利配置：按门店类型和size匹配对应返利分值
         */
        ShopConfRebateModel shopConfRebateModel = scanUtil.findShopConfRebate(shopType, goodsSizeInt);
        //对应的积分值
        int confRebateScore = shopConfRebateModel.getScore();
        log.info("confRebateScore = " + confRebateScore);
        
        
        /**
		 * 查找 es_member_coupon 中是否有此条码对应的，未激活的代金券
		 * 使用状态;0未使用，3-使用中(冻结)，1已使用,2是已过期；4-未激活；5-退货失效；6-退货(只有赠券占用)；
		 * 优惠券类型：1-默认原始的；2-赠券；3-代金券
         * and bar_code = "+ barCode + " and recover_flag = 0
         * TODO
		 */
        MemberCouponModel memberCouponModel = scanUtil.findMemberCoupon(memberId, scanBatchRecordDetailModel);
//        MemberCouponModel memberCouponModel = findMemberCouponRelyGoodsGroup(scanBatchRecordDetailModel, memberId);
        
        /**
         * `flow_state` int(11) DEFAULT '0' COMMENT '订单状态：1-正常;2-门店入库退货;3-门店未入库退货;'	1不需要
         * 追回原扫码入库的代金券
         */
        //查询扫码入库的记录和时间
        ScanBatchRecordDetailModel scanInRecordDetail = null;
        if (null != memberCouponModel) {
            /**
             * 激活过代金券，追回代金券
             */
        	recoverScanInCoupon(sqls, scanBatchRecordDetailModel, memberId, memberCouponModel);
        	
        	//查询扫码入库的记录和时间
            scanInRecordDetail = scanUtil.findScanInRecordDetail(scanBatchRecordDetailModel, memberCouponModel);
        } else {
            /**
             * 没有激活过代金券，只需要追回积分和返利
             * recover_flag = 0 and and show_deal_flag = 1
             */
        	scanInRecordDetail = scanUtil.findScanInRecordDetail(scanBatchRecordDetailModel);
        }

        //扫码入库时间
        Date scanInDate = scanInRecordDetail.getCreate_datetime();
        String scanInDateStr = TimeUtil.getStringByDate(scanInDate, "yyyy-MM");
        //当前日期
        Date curDate = new Date();
        String curDateStr = TimeUtil.getStringByDate(curDate, "yyyy-MM");
        
        /**
         * 如果是当前月份入库，而且是当前月份退货
         * 追回当前月的预展示积分和返利
         */
        if (curDateStr.equals(scanInDateStr)) {
        	recoverCurMonthShow(sqls, scanBatchRecordDetailModel, memberId, memberIntegralScore, goodsSizeInt, calScanIntegralScore,
    				calScanRebateScore, confIntegralScore, confRebateScore);
        }
        /**
         * 将退货对应的扫码记录 recover_flag 更新为1
         */
        updateScanInDetailRecoverFlag(sqls, scanInRecordDetail);
        
    }
    
    private void updateScanInDetailRecoverFlag(List<OneSql> sqls, ScanBatchRecordDetailModel scanInRecordDetail) {
    	long scanInId = scanInRecordDetail.getId();
    	sql = "update scan_batch_record_detail set recover_flag = 1 where id = " + scanInId;
    	log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "scan_main"));
		
	}

	public void recoverCurMonthShow(List<OneSql> sqls, ScanBatchRecordDetailModel scanBatchRecordDetailModel, int memberId, 
			MemberIntegralModel memberIntegralScore, int goodsSizeInt, int calScanIntegralScore, int calScanRebateScore, 
			int confIntegralScore, int confRebateScore) {
		/**扫码明细表主键*/
        Long scanId = scanBatchRecordDetailModel.getId();
        
        /**
         * 做扣减处理，更新分值和扫码数量
         */
        //积分
        sql = "update es_member_integral set scan_in_expect_score = scan_in_expect_score - " + calScanIntegralScore + ", "
            + "scan_in_expect_num = scan_in_expect_num - 1 "
            + "where member_id = " + memberId + " and integral_type = 5 ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));
        
        //返利
        sql = "update es_member_integral set scan_in_expect_score = scan_in_expect_score - " + calScanRebateScore + ", "
            + "scan_in_expect_num = scan_in_expect_num - 1 "
            + "where member_id = " + memberId + " and integral_type = 6 ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));
        
        
        /**
         * 生成流水
         * 虚拟积分类型：0-默认；7预积分；8-预返利；
         * 流水类型：0-默认；1-业务增加；2-业务减少；
         */
        //积分流水
        sql = "insert into es_member_integral_expect_bill "
            + "(member_id, member_name, integral_type, bill_type, amount, balance, fee, "
            + "exchange_amount, busi_describe, busi_id, busi_date, bill_date) "
            + "select member_id, member_name, 7, 2, " + calScanIntegralScore + ", (scan_in_expect_score), " + goodsSizeInt + ", "
            + "" + confIntegralScore + ", '扫码退货获得预展示积分-尺寸=" + goodsSizeInt + "', " + scanId + ", now(), now() from es_member_integral "
            + "where member_id = " + memberId + " and integral_type = 5 for update ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));

        //返利流水
        sql = "insert into es_member_integral_expect_bill "
            + "(member_id, member_name, integral_type, bill_type, amount, balance, fee, "
            + "exchange_amount, busi_describe, busi_id, busi_date, bill_date) "
            + "select member_id, member_name, 8, 2, " + calScanRebateScore + ", (scan_in_expect_score), " + goodsSizeInt + ", "
            + "" + confRebateScore + ", '扫码退货获得预展示返利-尺寸=" + goodsSizeInt + "', " + scanId + ", now(), now() from es_member_integral "
            + "where member_id = " + memberId + " and integral_type = 6 for update ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));
	}

    /**
     * 追回原扫码入库的代金券
     * 2-门店入库退货
     * @param sqls
     * @param scanBatchRecordDetailModel
     * @param memberId
     */
	public void recoverScanInCoupon(List<OneSql> sqls, ScanBatchRecordDetailModel scanBatchRecordDetailModel, 
			int memberId, MemberCouponModel memberCouponModel) throws Exception{
        /**扫码明细表主键*/
        long scanId = scanBatchRecordDetailModel.getId();
        
		/**
		 * 正常状态，需要退还积分，返利，代金券，退款处理
		 */
		if (null == memberCouponModel || null == memberCouponModel.getUsed_status()) {
		    msg = "扫码退货报警REWARDERROR【没有查询到原扫码入库的代金券】";
            log.info(msg);
            throw new Exception(msg);
        }
        //券ID
        int mcId = memberCouponModel.getMc_id();
        //订单项ID
        int itemId = memberCouponModel.getItem_id();
        //券金额
        BigDecimal couponPrice = null == memberCouponModel.getCoupon_price() ? BigDecimal.ZERO : memberCouponModel.getCoupon_price();
		//使用状态
        int usedStatus = memberCouponModel.getUsed_status();
        //追回标记：默认0；1-已追回；
        long recoverFlag = memberCouponModel.getRecover_flag();
		log.info("usedStatus = " + usedStatus + ", recoverFlag = " + recoverFlag);

        /**
         * 使用状态;0未使用，3-使用中(冻结)，1已使用,2是已过期；4-未激活；5-退货失效；6-退货(只有赠券占用)；
         */
		if (0 == usedStatus && 0 == recoverFlag) {
            /**
             * 可以追回
             */
		    /**
		     * 本次扫码可激活代金券
             * 使用状态;0未使用，3-使用中(冻结)，1已使用,2是已过期；4-未激活；5-退货失效；6-退货(只有赠券占用)；
		     */
		    sql = "update es_member_coupon set used_status = 5, scan_out_id = " + scanId + ", recover_flag = 1 where mc_id = " + mcId;
		    log.info("sql = " + sql);
		    sqls.add(new OneSql(sql, 1, null, "shop_trade"));
		} else {
            /**
             * 追不回，追返利
             */
			//更新处理标记
		    sql = "update es_member_coupon set scan_out_id = " + scanId + ", recover_flag = 1 where mc_id = " + mcId;
		    log.info("sql = " + sql);
		    sqls.add(new OneSql(sql, 1, null, "shop_trade"));
			
			//更新返利金额,可追减为负数 TODO
			sql = "update es_member_integral set balance = balance - " + couponPrice + " "
				+ "where member_id = " + memberId + " and integral_type = 6 ";
			log.info("sql = " + sql);
			sqls.add(new OneSql(sql, 1, null, "shop_trade"));
			
			/**
			 * 保存到流水
			 * 虚拟积分类型：0-默认；5-积分；6-返利；7预积分；8-预返利；
			 * 流水类型：0-默认；1-业务增加；2-业务减少；3-系统增加；4-系统减少；
			 */
			sql = "insert into es_member_integral_bill "
				+ "(member_id, member_name, integral_type, bill_type, amount, balance, fee, "
				+ "exchange_amount, busi_describe, busi_id, busi_date, bill_date) "
				+ "select member_id, member_name, 6, 4, " + couponPrice + ", (balance + freeze), 0, "
				+ "0, '扫码退货代金券已使用追回返利', " + scanId + ", now(), now() from es_member_integral "
				+ "where member_id = " + memberId + " and integral_type = 6 for update ";
			log.info("sql = " + sql);
			sqls.add(new OneSql(sql, 1, null, "shop_trade"));
        }
		
		/**
         * 更新此订单明细对应的扫码退货数量
         */
        sql = "update es_order_items set scan_return_num = scan_return_num + 1 where item_id = " + itemId;
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));
	}

    /**
     * 更新扫码明细表数据状态，单行处理
     * @param scanId
     * @param showDealFlag
     * @param showDealMsg
     */
    public void updateScanDetailFlag(long scanId, int showDealFlag, String showDealMsg) {
        //更新扫码明细数据状态
        sql = "update scan_batch_record_detail set show_deal_flag = " + showDealFlag + ", show_deal_msg = '" + showDealMsg + "' "
                + "where id = " + scanId;
        log.info("sql = " + sql);
        Data.Update("scan_main", sql, null);
    }

    /**
     * 更新扫码明细表数据状态，事务中处理
     * @param sqls
     * @param scanId
     * @param showDealFlag
     * @param showDealMsg
     */
    public void updateScanDetailFlagTS(List<OneSql> sqls, long scanId, int showDealFlag, String showDealMsg) {
        //更新扫码明细数据状态
        sql = "update scan_batch_record_detail set show_deal_flag = " + showDealFlag + ", show_deal_msg = '" + showDealMsg + "' "
                + "where id = " + scanId;
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "scan_main"));
    }
}
