package com.world.task.shop.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.world.data.mysql.Bean;
import com.world.model.shop.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;
import com.world.data.mysql.Data;
import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.util.GoodsNameSplitUtil;
import com.world.util.date.TimeUtil;

public class ScanInShopThread extends Thread {
    private static Logger log = Logger.getLogger(ScanInShopThread.class);
    /*sql语句*/
    private String sql = "";
    /**报警msg*/
    private  String msg = "";
    private CountDownLatch countDownLatch;
    private ScanBatchRecordDetailModel scanBatchRecordDetailModel;

    public ScanInShopThread(ScanBatchRecordDetailModel scanBatchRecordDetailModel, CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.scanBatchRecordDetailModel = scanBatchRecordDetailModel;
    }

    @Override
    public void run() {
        log.info("ScanInDealerThread...run...scanBatchRecordDetailModel = " + JSON.toJSONString(scanBatchRecordDetailModel));
        /**扫码明细表主键*/
        long scanId = scanBatchRecordDetailModel.getId();

        try {
            //开启事务处理
            List<OneSql> sqls = new ArrayList<OneSql>();
            TransactionObject txObj = new TransactionObject();
            long startTime = System.currentTimeMillis();

            /**
             * 扫码入库处理
             * sqls别轻易使用
             */
            this.scanInDeal(scanBatchRecordDetailModel, sqls);
            //更新扫码明细表数据状态
            this.updateScanDetailFlagTS(sqls, scanId, 1, "扫码入库处理成功");
            
            /**提交事务*/
            txObj.excuteUpdateList(sqls);
            if (txObj.commit()) {
                log.info("扫码入库报警REWARDINFO:【扫码入库处理】处理成功，数据ID【" + scanId + "】");
            } else {
                log.info("扫码入库报警REWARDERROR:【扫码入库处理】处理失败，数据ID【" + scanId + "】");
            }
            
            long endTime = System.currentTimeMillis();
            log.info("扫码入库报警REWARDInfo:【扫码入库处理】数据ID【" + scanId + "】处理耗时【" + (endTime - startTime) + "】");
        } catch (Exception e) {
            log.info("扫码入库报警REWARDERROR:ScanInDealerThread", e);
            this.updateScanDetailFlag(scanId, 2, msg);
        } finally {
            countDownLatch.countDown();
        }
    }

    /**
     * 扫码入库处理
     * @param scanBatchRecordDetailModel
     * @param sqls
     * @throws Exception
     */
    public void scanInDeal(ScanBatchRecordDetailModel scanBatchRecordDetailModel, List<OneSql> sqls) throws Exception {
        /**扫码明细表主键*/
        long scanId = scanBatchRecordDetailModel.getId();
        /**扫码条码*/
        long barCode = scanBatchRecordDetailModel.getBar_code();
        //所属门店ID
        int shopId = scanBatchRecordDetailModel.getShop_id();

        //门店类型
        int shopType = 1;
        //买家ID
        int memberId = 0;
        //门店信息
        ShopDetailModel shopDetailModel = findShopDetail(shopId);
        if (null == shopDetailModel || null == shopDetailModel.getShop_type() || null == shopDetailModel.getMember_id()) {
            msg = "扫码退货报警REWARDERROR【门店信息不存在】:sql = " + sql;
            log.info(msg);
            throw new Exception(msg);
        }

        shopType = null == shopDetailModel.getShop_type() ? 1 : shopDetailModel.getShop_type();
        memberId = shopDetailModel.getMember_id();

        /**
         * 积分&返利表
         * 查询积分，获取当前月份已扫码条数
         * scan_in_expect_num 扫码退货数量
         * 积分和返利扫码条数相同，取1个即可 = 当前已扫码数量
         */
        MemberIntegralModel memberIntegralScore = findMemberIntegralScore(scanBatchRecordDetailModel);
        int curScanInExpectNum = 0;
        if (null == memberIntegralScore) {
            msg = "扫码退货报警REWARDERROR【积分和返利账户信息不存在】:sql = " + sql;
            log.info(msg);
            throw new Exception(msg);
        }
        //积分和返利扫码条数相同，取1个即可 = 当前已扫码数量
        curScanInExpectNum = null == memberIntegralScore.getScan_in_expect_num() ? 0 : memberIntegralScore.getScan_in_expect_num();
        log.info("curScanInExpectNum = " + curScanInExpectNum);

        /**
         * 查询门店月度任务量
         */
        ShopConfTaskMModel shopConfTaskMModel = findShopConfTaskM(shopType);
        if (null == shopConfTaskMModel) {
            msg = "扫码退货报警REWARDERROR【门店任务量不存在】:sql = " + sql;
            log.info(msg);
            throw new Exception(msg);
        }

        /**拆分获取商品size*/
        int goodsSizeInt = splitGoodsSize(scanBatchRecordDetailModel);

        /**
         * 按照当前已扫描数量和门店类型，计算扫描此条应获得的积分
         */
        int calScanIntegralScore = this.produceScanIntegralScore(curScanInExpectNum, shopConfTaskMModel, shopType, goodsSizeInt);
        /**
         * 按照当前已扫描数量和门店类型，计算扫描此条应获得的返利
         */
        int calScanRebateScore = this.produceScanRebateScore(curScanInExpectNum, shopConfTaskMModel, shopType, goodsSizeInt);

        
        /**
         * 积分配置：按门店类型和size匹配对应积分分值
         */
        ShopConfIntegralModel shopConfIntegralModel = findShopConfIntegral(shopType, goodsSizeInt);
        if (null == shopConfIntegralModel) {
            msg = "扫码退货报警REWARDERROR【积分配置信息不存在】:sql = " + sql;
            log.info(msg);
            throw new Exception(msg);
        }
        //对应的积分值
        int confIntegralScore = null == shopConfIntegralModel.getScore() ? 0 : shopConfIntegralModel.getScore();
        log.info("confIntegralScore = " + confIntegralScore);

        /**
         * 返利配置：按门店类型和size匹配对应返利分值
         */
        ShopConfRebateModel shopConfRebateModel = findShopConfRebate(shopType, goodsSizeInt);
        if (null == shopConfRebateModel) {
            msg = "扫码退货报警REWARDERROR【返利配置信息不存在】:sql = " + sql;
            log.info(msg);
            throw new Exception(msg);
        }
        //对应的积分值
        int confRebateScore = shopConfRebateModel.getScore();
        log.info("confRebateScore = " + confRebateScore);
        

        /**
         * 查找 es_member_coupon 中是否有此条码对应的，未激活的代金券
         * 使用状态;0未使用，3-使用中(冻结)，1已使用,2是已过期；4-未激活；5-退货失效；
         * 优惠券类型：1-默认原始的；2-赠券；3-代金券
         * 
         * TODO 需要按照产品组查询
         */
//        MemberCouponModel memberCouponModel = findMemberCoupon(goodsCode, memberId);
        MemberCouponModel memberCouponModel = findMemberCouponRelyGoodsGroup(scanBatchRecordDetailModel, memberId);
        
        if (null != memberCouponModel) {
        	/**
        	 * 还有可以激活的代金券，激活代金券
        	 * 修订订单项(扫码入库数量)
             * 修改订单状态
        	 */
        	activeMemberCoupon(sqls, barCode, memberCouponModel, scanBatchRecordDetailModel);
        }
        
        /**
         * 生成月度预展示积分和返利
         */
        produceCurMonthShow(sqls, scanId, memberId, memberIntegralScore, goodsSizeInt, calScanIntegralScore, calScanRebateScore,
				confIntegralScore, confRebateScore);

    }

    public void produceCurMonthShow(List<OneSql> sqls, long scanId, int memberId, MemberIntegralModel memberIntegralScore,
			int goodsSizeInt, int calScanIntegralScore, int calScanRebateScore, int confIntegralScore,
			int confRebateScore) {
		//当前日期
        Date curDate = new Date();
        String curDateStr = TimeUtil.getStringByDate(curDate, "yyyy-MM");
        //数据库中扫码当前月份：2020-05格式
        String scanInExpectMonth = null == memberIntegralScore.getScan_in_expect_month() ? curDateStr : memberIntegralScore.getScan_in_expect_month();
        if (curDateStr.equals(scanInExpectMonth)) {
            /**
             * 做累加处理，更新分值和扫码数量
             */
            //积分
            sql = "update es_member_integral set scan_in_expect_score = scan_in_expect_score + " + calScanIntegralScore + ", "
                + "scan_in_expect_num = scan_in_expect_num + 1 "
                + "where member_id = " + memberId + " and integral_type = 5 ";
            log.info("sql = " + sql);
            sqls.add(new OneSql(sql, 1, null, "shop_trade"));

            //返利
            sql = "update es_member_integral set scan_in_expect_score = scan_in_expect_score + " + calScanRebateScore + ", "
                + "scan_in_expect_num = scan_in_expect_num + 1 "
                + "where member_id = " + memberId + " and integral_type = 6 ";
            log.info("sql = " + sql);
            sqls.add(new OneSql(sql, 1, null, "shop_trade"));
        } else {
            /**
             * 新的月份，更新扫码数量和扫码月份
             */
            //积分
            sql = "update es_member_integral set scan_in_expect_score = " + calScanIntegralScore + ", "
                + "scan_in_expect_num = 1, scan_in_expect_month = '" + curDateStr + "' "
                + "where member_id = " + memberId + " and integral_type = 5 ";
            log.info("sql = " + sql);
            sqls.add(new OneSql(sql, 1, null, "shop_trade"));

            //返利
            sql = "update es_member_integral set scan_in_expect_score = " + calScanRebateScore + ", "
                + "scan_in_expect_num = 1, scan_in_expect_month = '" + curDateStr + "' "
                + "where member_id = " + memberId + " and integral_type = 6 ";
            log.info("sql = " + sql);
            sqls.add(new OneSql(sql, 1, null, "shop_trade"));
        }

        /**
         * 生成流水
         * 虚拟积分类型：0-默认；7预积分；8-预返利；
         * 流水类型：0-默认；1-业务增加；2-业务减少；
         */
        //积分流水
        sql = "insert into es_member_integral_expect_bill "
            + "(member_id, member_name, integral_type, bill_type, amount, balance, fee, "
            + "exchange_amount, busi_describe, busi_id, busi_date, bill_date) "
            + "select member_id, member_name, 7, 1, " + calScanIntegralScore + ", (scan_in_expect_score), " + goodsSizeInt + ", "
            + "" + confIntegralScore + ", '扫码入库获得预展示积分-尺寸=" + goodsSizeInt + "', " + scanId + ", now(), now() from es_member_integral "
            + "where member_id = " + memberId + " and integral_type = 5 for update ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));

        //返利流水
        sql = "insert into es_member_integral_expect_bill "
            + "(member_id, member_name, integral_type, bill_type, amount, balance, fee, "
            + "exchange_amount, busi_describe, busi_id, busi_date, bill_date) "
            + "select member_id, member_name, 8, 1, " + calScanRebateScore + ", (scan_in_expect_score), " + goodsSizeInt + ", "
            + "" + confRebateScore + ", '扫码入库获得预展示返利-尺寸=" + goodsSizeInt + "', " + scanId + ", now(), now() from es_member_integral "
            + "where member_id = " + memberId + " and integral_type = 6 for update ";
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));
	}

	public void activeMemberCoupon(List<OneSql> sqls, long barCode, MemberCouponModel memberCouponModel,
                                   ScanBatchRecordDetailModel scanBatchRecordDetailModel) throws Exception {
        /**扫码明细表主键*/
        long scanId = scanBatchRecordDetailModel.getId();
		//需要激活的代金券ID
		int mcId = memberCouponModel.getMc_id();
		//订单ID
		int busiId = memberCouponModel.getBusi_id();
		//订单项ID
		int itemId = memberCouponModel.getItem_id();
		
		/**
		 * 本次扫码可激活代金券
		 */
		sql = "update es_member_coupon set used_status = 0, scan_in_id = " + scanId + ", bar_code = " + barCode + " where mc_id = " + mcId;
		log.info("sql = " + sql);
		sqls.add(new OneSql(sql, 1, null, "shop_trade"));

		/**
		 * 更新此订单明细对应的扫码入库数量
		 */
		sql = "update es_order_items set scan_in_num = scan_in_num + 1 where item_id = " + itemId;
		log.info("sql = " + sql);
		sqls.add(new OneSql(sql, 1, null, "shop_trade"));
		
		/**
		 * 判断修订订单完成状态
		 */
        OrderModel orderModel = this.findOrder(busiId);
        //订单编号
        String orderSn = orderModel.getSn();
		this.updateOrderStatus(sqls, orderSn, busiId);
	}

	public void updateOrderStatus(List<OneSql> sqls, String orderSn, int orderId) throws Exception {
        /**
         * 根据订单明细，判断订单发货状态
         * cast(SUM(number) AS SIGNED )
         */
        sql = "select cast(sum(refund_num) as signed) refund_num, cast(sum(ship_num) as signed) ship_num, "
            + "cast(sum(num) as signed) num, cast(sum(scan_in_num) as signed) scan_in_num from es_order_items "
            + "where order_sn = '" + orderSn + "' ";
        log.info("sql = " + sql);
        OrderItemsModel orderItemsModel = (OrderItemsModel) Data.GetOne("shop_trade", sql, null, OrderItemsModel.class);
        log.info("orderItemsModel = " + JSON.toJSONString(orderItemsModel));
        if (null == orderItemsModel) {
            msg = "扫码入库报警REWARDERROR【订单信息不存在】:sql = " + sql;
            log.info(msg);
            throw new Exception(msg);
        }

        /**
         * 订单相关数量信息
         */
        long refundNum = orderItemsModel.getRefund_num();   //退货数量
//        int shipNum = orderItemsModel.getShip_num();   //退货数量
        long num = orderItemsModel.getNum(); //订单原数量
        long scanInNum = orderItemsModel.getScan_in_num();

        if ((scanInNum + 1 + refundNum) == num) {
            /*设置订单状态为完成*/
            sql = "update es_order set order_status = 'COMPLETE' where order_id = " + orderId;
            log.info("sql = " + sql);
            sqls.add(new OneSql(sql, 1, null, "shop_trade"));
        }

    }

    public OrderModel findOrder(int orderId) throws Exception {
        sql = "select * from es_order where order_id = " + orderId;
        log.info("sql = " + sql);
        OrderModel orderModel = (OrderModel) Data.GetOne("shop_trade", sql, null, OrderModel.class);
        if (null == orderModel || StringUtils.isEmpty(orderModel.getSn())) {
            msg = "扫码入库报警REWARDERROR【订单信息不存在】:sql = " + sql;
            log.info(msg);
            throw new Exception(msg);
        }
        log.info("orderModel = " + JSON.toJSONString(orderModel));
        return orderModel;
    }

	public MemberCouponModel findMemberCoupon(String goodsCode, int memberId) {
		sql = "select * from es_member_coupon where member_id = " + memberId + " and seller_id = 1 and goods_sku_sn = '" + goodsCode + "' "
            + "and coupon_type = 3 and used_status = 4 order by mc_id asc limit 1 ";
        log.info("sql = " + sql);
        MemberCouponModel memberCouponModel = (MemberCouponModel) Data.GetOne("shop_trade", sql, null, MemberCouponModel.class);
        log.info("memberCouponModel = " + JSON.toJSONString(memberCouponModel));
		return memberCouponModel;
	}

    public MemberCouponModel findMemberCouponRelyGoodsGroup(ScanBatchRecordDetailModel scanBatchRecordDetailModel, int memberId) {
    	/**商品编号*/
        String goodsCode = scanBatchRecordDetailModel.getGoods_code();
        
        List<Bean> goodsGroupModelList = this.findGoodsGroupModel(goodsCode);
        //拼接IN查询
        goodsCode = "'" + goodsCode + "'";
        if (null != goodsGroupModelList && goodsGroupModelList.size() > 0) {
            GoodsGroupModel goodsGroupModel = null;
            for (int i = 0; i < goodsGroupModelList.size(); i++) {
                goodsGroupModel = (GoodsGroupModel) goodsGroupModelList.get(i);
                goodsCode += ", '" + goodsGroupModel.getGoods_code() + "'";
            }
        }
        log.info("goodsCode = " + goodsCode);

        sql = "select * from es_member_coupon where member_id = " + memberId + " and seller_id = 1 and goods_sku_sn in (" + goodsCode + ") "
                + "and coupon_type = 3 and used_status = 4 order by mc_id asc limit 1 ";
        log.info("sql = " + sql);
        MemberCouponModel memberCouponModel = (MemberCouponModel) Data.GetOne("shop_trade", sql, null, MemberCouponModel.class);
        log.info("memberCouponModel = " + JSON.toJSONString(memberCouponModel));
        return memberCouponModel;
    }

	public List<Bean> findGoodsGroupModel(String goodsCode) {
        sql = "select * from es_goods_group "
            + "where group_code = (select group_code from es_goods_group where goods_code = '" + goodsCode + "' )";
        log.info("sql = " + sql);
        List<Bean> goodsGroupModelList = (List<Bean>) Data.Query("shop_goods", sql, null, GoodsGroupModel.class);
        log.info("goodsGroupModelList = " + JSON.toJSONString(goodsGroupModelList));
        return goodsGroupModelList;
    }
    
    public ShopConfRebateModel findShopConfRebate(int shopType, int goodsSizeInt) {
    	//当前日期
        Date curDate = new Date();
        String curDateStr = TimeUtil.getStringByDate(curDate, "yyyy-MM-dd");
        
		sql = "select * from es_shop_conf_rebate where shop_type = " + shopType + " and size = " + goodsSizeInt + " "
            + "and start_time <= '" + curDateStr + "' and end_time >= '" + curDateStr + "' ";
        log.info("sql = " + sql);
        ShopConfRebateModel shopConfRebateModel = (ShopConfRebateModel) Data.GetOne("shop_member", sql, null, ShopConfRebateModel.class);
        log.info("shopConfRebateModel = " + JSON.toJSONString(shopConfRebateModel));
		return shopConfRebateModel;
	}

	public ShopConfIntegralModel findShopConfIntegral(int shopType, int goodsSizeInt) {
		//当前日期
        Date curDate = new Date();
        String curDateStr = TimeUtil.getStringByDate(curDate, "yyyy-MM-dd");
        
		sql = "select * from es_shop_conf_integral where shop_type = " + shopType + " and size = " + goodsSizeInt + " "
            + "and start_time <= '" + curDateStr + "' and end_time >= '" + curDateStr + "' ";
        log.info("sql = " + sql);
        ShopConfIntegralModel shopConfIntegralModel = (ShopConfIntegralModel) Data.GetOne("shop_member", sql, null, ShopConfIntegralModel.class);
        log.info("shopConfIntegralModel = " + JSON.toJSONString(shopConfIntegralModel));
		return shopConfIntegralModel;
	}
    
    public int splitGoodsSize(ScanBatchRecordDetailModel scanBatchRecordDetailModel) {
		/**商品名称*/
        String goodsName = scanBatchRecordDetailModel.getGoods_name();
		/**
         * 匹配积分和返利分值
         */
        Map<String, String> goodsMap = GoodsNameSplitUtil.getSpec(goodsName);
        String goodsSize = goodsMap.get("size");
        int goodsSizeInt = 13;
        if (StringUtils.isEmpty(goodsSize)) {
            goodsSizeInt = 13;
        }
        try {
            goodsSizeInt = Integer.parseInt(goodsSize);
            /**18寸及以上按照18寸*/
            if (goodsSizeInt > 18) {
                goodsSizeInt = 18;
            }
        } catch (Exception e) {
            goodsSizeInt = 13;
        }
        log.info("goodsSizeInt = " + goodsSizeInt);
		return goodsSizeInt;
	}

	public ShopConfTaskMModel findShopConfTaskM(int shopType) {
		sql = "select * from es_shop_conf_task_m where shop_type = " + shopType;
        log.info("sql = " + sql);
        ShopConfTaskMModel shopConfTaskMModel = (ShopConfTaskMModel) Data.GetOne("shop_member", sql, null, ShopConfTaskMModel.class);
        log.info("shopConfMtaskModel = " + JSON.toJSONString(shopConfTaskMModel));
		return shopConfTaskMModel;
	}

	public MemberIntegralModel findMemberIntegralScore(ScanBatchRecordDetailModel scanBatchRecordDetailModel) {
		//所属门店ID
        int shopId = scanBatchRecordDetailModel.getShop_id();
		MemberIntegralModel memberIntegralScore;
		sql = "select * from es_member_integral where shop_id = '" + shopId + "' and integral_type = 5 ";
        log.info("sql = " + sql);
        memberIntegralScore = (MemberIntegralModel) Data.GetOne("shop_trade", sql, null, MemberIntegralModel.class);
        log.info("memberIntegralScore = " + JSON.toJSONString(memberIntegralScore));
		return memberIntegralScore;
	}

	public ShopDetailModel findShopDetail(int shopId) {
		ShopDetailModel shopDetailModel;
		sql = "select shop_type, member_id from es_shop_detail where shop_id = " + shopId;
        log.info("sql = " + sql);
        shopDetailModel = (ShopDetailModel) Data.GetOne("shop_member", sql, null, ShopDetailModel.class);
        log.info("shopDetailModel = " + JSON.toJSONString(shopDetailModel));
		return shopDetailModel;
	}

    /**
     * 按照当前已扫描数量和门店类型，计算扫描此条应获得的返利
     * @param curScanInExpectNum
     * @param shopConfTaskMModel
     * @param shopType
     * @param goodsSizeInt
     */
    public int produceScanRebateScore(int curScanInExpectNum, ShopConfTaskMModel shopConfTaskMModel, int shopType, int goodsSizeInt) throws Exception {
        /**
         * 根据门店当前类型和已扫码数量，计算门店升级后的门店类型
         */
        int calShopType = this.calShopType(curScanInExpectNum, shopConfTaskMModel);
        log.info("门店原类型 = " + shopType + ", 门店升级后类型 = " + calShopType + ", 当前已扫码数量 = " + curScanInExpectNum + ", 尺寸 = " + goodsSizeInt);
        
        //当前日期
        Date curDate = new Date();
        String curDateStr = TimeUtil.getStringByDate(curDate, "yyyy-MM-dd");
        /**
         * 积分配置：按门店类型和size匹配对应积分分值
         */
        sql = "select * from es_shop_conf_rebate where shop_type = " + calShopType + " and size = " + goodsSizeInt + " "
                + "and start_time <= '" + curDateStr + "' and end_time >= '" + curDateStr + "' ";
        log.info("sql = " + sql);
        
        ShopConfRebateModel shopConfRebateModel = (ShopConfRebateModel) Data.GetOne("shop_member", sql, null, ShopConfRebateModel.class);
        log.info("shopConfRebateModel = " + JSON.toJSONString(shopConfRebateModel));
        if (null == shopConfRebateModel || null == shopConfRebateModel.getScore() || 0 == shopConfRebateModel.getScore()) {
        	msg = "扫码入库报警REWARDERROR【返利配置信息不存在】:sql = " + sql;
            log.info(msg);
            throw new Exception(msg);
        }
        //对应的积分值
        int confRebateScore = shopConfRebateModel.getScore();
        log.info("confRebateScore = " + confRebateScore);

        return confRebateScore;
    }

    /**
     * 按照当前已扫描数量和门店类型，计算扫描此条应获得的积分
     * @param curScanInExpectNum
     * @param shopConfTaskMModel
     * @param shopType
     * @param goodsSizeInt
     */
    public int produceScanIntegralScore(int curScanInExpectNum, ShopConfTaskMModel shopConfTaskMModel, int shopType, int goodsSizeInt) throws Exception{
        /**
         * 根据门店当前类型和已扫码数量，计算门店升级后的门店类型
         */
        int calShopType = this.calShopType(curScanInExpectNum, shopConfTaskMModel);
        log.info("门店原类型 = " + shopType + ", 门店升级后类型 = " + calShopType + ", 当前已扫码数量 = " + curScanInExpectNum + ", 尺寸 = " + goodsSizeInt);

        //当前日期
        Date curDate = new Date();
        String curDateStr = TimeUtil.getStringByDate(curDate, "yyyy-MM-dd");
        /**
         * 积分配置：按门店类型和size匹配对应积分分值
         */
        sql = "select * from es_shop_conf_integral where shop_type = " + calShopType + " and size = " + goodsSizeInt + " "
            + "and start_time <= '" + curDateStr + "' and end_time >= '" + curDateStr + "' ";
        log.info("sql = " + sql);
        
        ShopConfIntegralModel shopConfIntegralModel = (ShopConfIntegralModel) Data.GetOne("shop_member", sql, null, ShopConfIntegralModel.class);
        log.info("shopConfIntegralModel = " + JSON.toJSONString(shopConfIntegralModel));
        if (null == shopConfIntegralModel || null == shopConfIntegralModel.getScore() || 0 == shopConfIntegralModel.getScore()) {
        	msg = "扫码入库报警REWARDERROR【积分配置信息不存在】:sql = " + sql;
            log.info(msg);
            throw new Exception(msg);
        }
        //对应的积分值
        int confIntegralScore = shopConfIntegralModel.getScore();
        log.info("confIntegralScore = " + confIntegralScore);

        return confIntegralScore;
    }

    /**
     * 根据门店当前类型和已扫码数量，计算门店升级后的门店类型
     * @param curScanInExpectNum
     * @param shopConfTaskMModel
     */
    public int calShopType(int curScanInExpectNum, ShopConfTaskMModel shopConfTaskMModel) {
        //门店当前类型
        int shopType = shopConfTaskMModel.getShop_type();
        //门店下1个等级类型 1-2,2-3
        int nextShopType = shopType + 1;
        //是否可升级：0-不可升级 ； 1-可升级
        int isUpgrade = null == shopConfTaskMModel.getIs_upgrade() ? 0 : shopConfTaskMModel.getIs_upgrade();
        //返回的计算后门店类型
        int calShopType = shopType;

        //门店月度任务量
        int shopTaskM = shopConfTaskMModel.getShop_task_m();

        /**
         * 门店不可升级
         */
        if (0 == isUpgrade) {
            return calShopType;
        } else {
            /**
             * 门店可升级
             * 判断下1个等级是否存在
             */
            sql = "select * from es_shop_conf_task_m where shop_type = " + nextShopType;
            log.info("sql = " + sql);
            ShopConfTaskMModel nextShopConfTaskMModel = (ShopConfTaskMModel) Data.GetOne("shop_member", sql, null, ShopConfTaskMModel.class);
            log.info("nextShopConfMtaskModel = " + JSON.toJSONString(nextShopConfTaskMModel));
            if (null == nextShopConfTaskMModel) {
                return calShopType;
            } else {
                /**
                 * 校验是否可升级，curScanInExpectNum >= 升级后的门店月度任务量
                 */
                shopTaskM = nextShopConfTaskMModel.getShop_task_m();
                if ((curScanInExpectNum + 1) >= shopTaskM) {
                    /**
                     * 扫码数量达到下1级别的门槛，递归计算
                     */
                    calShopType = this.calShopType(curScanInExpectNum, nextShopConfTaskMModel);
                } else {
                    //扫码数量未达到下1级别的门槛
                    return calShopType;
                }
            }
        }

        return calShopType;
    }

    /**
     * 更新扫码明细表数据状态，单行处理
     * @param scanId
     * @param showDealFlag
     * @param showDealMsg
     */
    public void updateScanDetailFlag(long scanId, int showDealFlag, String showDealMsg) {
        //更新扫码明细数据状态
        sql = "update scan_batch_record_detail_bak set show_deal_flag = " + showDealFlag + ", show_deal_msg = '" + showDealMsg + "' "
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
        sql = "update scan_batch_record_detail_bak set show_deal_flag = " + showDealFlag + ", show_deal_msg = '" + showDealMsg + "' "
                + "where id = " + scanId;
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "scan_main"));
    }
}
