package com.world.task.shop.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.world.model.shop.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.task.shop.thread.ScanInShopThread;
import com.world.timer.DateUtilsEx;
import com.world.util.GoodsNameSplitUtil;
import com.world.util.date.TimeUtil;

/**
	* @author xian
	* @date   2020年4月7日 下午2:05:40
	* @version v1.0.0
	* @Description	扫码工具类
	* ScanUtil.java 
	* Modification History:
	* Date                         Author          Version          Description
	---------------------------------------------------------------------------------*
	* 2020年4月7日 下午2:05:40       xian            v1.0.0           Created
 */
public class ScanUtil {
	private static Logger log = Logger.getLogger(ScanInShopThread.class);
	
	/**
	* @Title: listScanBatchRecordDetail  
	* @Description: 2020年4月7日 下午2:12:58 获取门店月度的扫码入库记录
	* @param @param shopId
	* @param @param settleMonth
	* @param @return
	* @param @throws Exception    参数  
	* @return List<Bean>    返回类型  
	* @throws
	 */
	@SuppressWarnings("deprecation")
	public List<Bean> listScanBatchRecordDetail(int shopId, String calMonth) throws Exception {
		String sql = "";
		String msg = "";
		
		//拼接当前月的开始和结束时间，结束时间为计算月的最后1天
		String startDate = calMonth + "-01 00:00:00";
		String endDate = DateUtilsEx.formatDate(DateUtilsEx.getLastDayOfMonth(
									new Date(calMonth.replaceAll("-", "/") + "/01")), "yyyy-MM-dd") + " 23:59:59";
		
		/**
		 * `scan_type` int(11) DEFAULT NULL COMMENT '扫码种类：1-经销商出库;2-经销商退货扫码;3-门店入库;4-门店出库',
		 * `flow_state` int(11) DEFAULT '0' COMMENT '订单状态：1-正常;2-门店入库退货;3-门店未入库退货;'   不需要
		 */
		sql = "select * from scan_batch_record_detail "
			+ "where shop_id = " + shopId + " and create_datetime >= '" + startDate + "' and create_datetime <= '" + endDate + "' "
			+ "and scan_type = 3 and flow_state = 1 order by id asc ";
		log.info("sql = " + sql);
		List<Bean> scanBatchRecordDetailModelList = (List<Bean>) Data.Query("scan_main", sql, null, ScanBatchRecordDetailModel.class);
		if(null == scanBatchRecordDetailModelList) {
		    msg = "门店扫码月度结算报警REWARDERROR【门店没有扫码记录】shopId = " + shopId;
		    log.info(msg);
		    throw new Exception(msg);
		}
		return scanBatchRecordDetailModelList;
	}
	
	/**
	* @Title: findShopConfRebate  
	* @Description: 2020年4月7日 下午2:20:17 获取门店返利配置信息
	* @param @param shopType
	* @param @param goodsSizeInt
	* @param @return
	* @param @throws Exception    参数  
	* @return ShopConfRebateModel    返回类型  
	* @throws
	 */
	public ShopConfRebateModel findShopConfRebate(int shopType, int goodsSizeInt) throws Exception {
		String sql = "";
		String msg = "";
    	//当前日期
        Date curDate = new Date();
        String curDateStr = TimeUtil.getStringByDate(curDate, "yyyy-MM-dd");
        
		sql = "select * from es_shop_conf_rebate where shop_type = " + shopType + " and size = " + goodsSizeInt + " "
            + "and start_time <= '" + curDateStr + "' and end_time >= '" + curDateStr + "' ";
        log.info("sql = " + sql);
        ShopConfRebateModel shopConfRebateModel = (ShopConfRebateModel) Data.GetOne("shop_member", sql, null, ShopConfRebateModel.class);
        log.info("shopConfRebateModel = " + JSON.toJSONString(shopConfRebateModel));
        if (null == shopConfRebateModel || null == shopConfRebateModel.getScore()) {
            msg = "扫码报警REWARDERROR【返利配置信息不存在】";
            log.info(msg);
            throw new Exception(msg);
        }
        
		return shopConfRebateModel;
	}
	
	/**
	* @Title: findShopConfIntegral  
	* @Description: 2020年4月7日 下午2:21:01 获取门店积分配置信息
	* @param @param shopType
	* @param @param goodsSizeInt
	* @param @return
	* @param @throws Exception    参数  
	* @return ShopConfIntegralModel    返回类型  
	* @throws
	 */
	public ShopConfIntegralModel findShopConfIntegral(int shopType, int goodsSizeInt) throws Exception {
		String sql = "";
		String msg = "";
		//当前日期
        Date curDate = new Date();
        String curDateStr = TimeUtil.getStringByDate(curDate, "yyyy-MM-dd");
        
		sql = "select * from es_shop_conf_integral where shop_type = " + shopType + " and size = " + goodsSizeInt + " "
            + "and start_time <= '" + curDateStr + "' and end_time >= '" + curDateStr + "' ";
        log.info("sql = " + sql);
        ShopConfIntegralModel shopConfIntegralModel = (ShopConfIntegralModel) Data.GetOne("shop_member", sql, null, ShopConfIntegralModel.class);
        log.info("shopConfIntegralModel = " + JSON.toJSONString(shopConfIntegralModel));
        if (null == shopConfIntegralModel || null == shopConfIntegralModel.getScore()) {
            msg = "扫码报警REWARDERROR【积分配置信息不存在】";
            log.info(msg);
            throw new Exception(msg);
        }
        
		return shopConfIntegralModel;
	}
	
	/**
     * 按照当前已扫描数量和门店类型，计算扫描此条应获得的积分
     * @param curScanInExpectNum
     * @param shopConfTaskMModel
     * @param shopType
     * @param goodsSizeInt
     */
    public int produceScanIntegralScore(int curScanInExpectNum, ShopConfTaskMModel shopConfTaskMModel, int shopType, int goodsSizeInt) throws Exception{
        String sql = "";
        String msg = "";
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
        	msg = "扫码报警REWARDERROR【积分配置信息不存在】";
            log.info(msg);
            throw new Exception(msg);
        }
        //对应的积分值
        int confIntegralScore = shopConfIntegralModel.getScore();
        log.info("confIntegralScore = " + confIntegralScore);

        return confIntegralScore;
    }
    
    /**
     * 按照当前已扫描数量和门店类型，计算扫描此条应获得的返利
     * @param curScanInExpectNum
     * @param shopConfTaskMModel
     * @param shopType
     * @param goodsSizeInt
     */
    public int produceScanRebateScore(int curScanInExpectNum, ShopConfTaskMModel shopConfTaskMModel, int shopType, int goodsSizeInt) throws Exception {
        String sql = "";
        String msg = "";
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
        	msg = "扫码报警REWARDERROR【返利配置信息不存在】";
            log.info(msg);
            throw new Exception(msg);
        }
        //对应的积分值
        int confRebateScore = shopConfRebateModel.getScore();
        log.info("confRebateScore = " + confRebateScore);

        return confRebateScore;
    }
    
    /**
     * 根据门店当前类型和已扫码数量，计算门店升级后的门店类型
     * @param curScanInExpectNum
     * @param shopConfTaskMModel
     */
    public int calShopType(int curScanInExpectNum, ShopConfTaskMModel shopConfTaskMModel) {
    	String sql = "";
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
    * @Title: splitGoodsSize  
    * @Description: 2020年4月7日 下午2:21:24 拆分商品名称，获取商品尺寸
    * @param @param scanBatchRecordDetailModel
    * @param @return    参数  
    * @return int    返回类型  
    * @throws
     */
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
	
	/**
	* @Title: findShopConfTaskM  
	* @Description: 2020年4月7日 下午2:21:55 获取门店月度任务配置信息
	* @param @param shopType
	* @param @return
	* @param @throws Exception    参数  
	* @return ShopConfTaskMModel    返回类型  
	* @throws
	 */
	public ShopConfTaskMModel findShopConfTaskM(int shopType) throws Exception {
		String sql = "";
		String msg = "";
		sql = "select * from es_shop_conf_task_m where shop_type = " + shopType;
        log.info("sql = " + sql);
        ShopConfTaskMModel shopConfTaskMModel = (ShopConfTaskMModel) Data.GetOne("shop_member", sql, null, ShopConfTaskMModel.class);
        log.info("shopConfMtaskModel = " + JSON.toJSONString(shopConfTaskMModel));
        if (null == shopConfTaskMModel || null == shopConfTaskMModel.getShop_task_m() || 0 == shopConfTaskMModel.getShop_task_m()) {
            msg = "扫码报警REWARDERROR【门店任务量不存在】";
            log.info(msg);
            throw new Exception(msg);
        }
        
		return shopConfTaskMModel;
	}
	
	/**
	* @Title: findShopDetail  
	* @Description: 2020年4月7日 下午2:22:14 获取门店信息
	* @param @param shopId
	* @param @return    参数  
	* @return ShopDetailModel    返回类型  
	* @throws
	 */
	public ShopDetailModel findShopDetail(int shopId) throws Exception {
		String sql = "";
		String msg = "";
		
		ShopDetailModel shopDetailModel;
		sql = "select shop_type, member_id from es_shop_detail where shop_id = " + shopId;
        log.info("sql = " + sql);
        shopDetailModel = (ShopDetailModel) Data.GetOne("shop_member", sql, null, ShopDetailModel.class);
        log.info("shopDetailModel = " + JSON.toJSONString(shopDetailModel));
        if (null == shopDetailModel || null == shopDetailModel.getShop_type() || null == shopDetailModel.getMember_id()) {
            msg = "扫码报警REWARDERROR【门店信息不存在】";
            log.info(msg);
            throw new Exception(msg);
        }
        
		return shopDetailModel;
	}
	
	public MemberIntegralModel findMemberIntegralScore(int memberId) throws Exception {
		String sql = "";
		String msg = "";
		
		//积分返利表
		MemberIntegralModel memberIntegralScore;
		sql = "select * from es_member_integral where member_id = " + memberId + " and integral_type = 5 ";
        log.info("sql = " + sql);
        memberIntegralScore = (MemberIntegralModel) Data.GetOne("shop_trade", sql, null, MemberIntegralModel.class);
        log.info("memberIntegralScore = " + JSON.toJSONString(memberIntegralScore));
        if (null == memberIntegralScore) {
            msg = "扫码退货报警REWARDERROR【积分和返利账户信息不存在】";
            log.info(msg);
            throw new Exception(msg);
        }
        
		return memberIntegralScore;
	}
	
	public MemberCouponModel findMemberCouponRelyGoodsGroup(ScanBatchRecordDetailModel scanBatchRecordDetailModel, int memberId) {
		String sql = "";
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
		String sql = "";
		
		sql = "select * from es_goods_group "
            + "where group_code = (select group_code from es_goods_group where goods_code = '" + goodsCode + "' )";
        log.info("sql = " + sql);
        List<Bean> goodsGroupModelList = (List<Bean>) Data.Query("shop_goods", sql, null, GoodsGroupModel.class);
        log.info("goodsGroupModelList = " + JSON.toJSONString(goodsGroupModelList));
        return goodsGroupModelList;
    }
	
	public OrderModel findOrder(int orderId) throws Exception {
		String sql = "";
		String msg = "";
		
        sql = "select * from es_order where order_id = " + orderId;
        log.info("sql = " + sql);
        OrderModel orderModel = (OrderModel) Data.GetOne("shop_trade", sql, null, OrderModel.class);
        if (null == orderModel || StringUtils.isEmpty(orderModel.getSn())) {
            msg = "扫码入库报警REWARDERROR【订单信息不存在】";
            log.info(msg);
            throw new Exception(msg);
        }
        log.info("orderModel = " + JSON.toJSONString(orderModel));
        return orderModel;
    }
	
	public MemberCouponModel findMemberCoupon(int memberId, ScanBatchRecordDetailModel scanBatchRecordDetailModel) {
		String sql = "";
		
		/**条码编号*/
        long barCode = scanBatchRecordDetailModel.getBar_code();
		/**
		 * 查找 es_member_coupon 中是否有此条码对应的，未激活的代金券
		 * 使用状态;0未使用，3-使用中(冻结)，1已使用,2是已过期；4-未激活；5-退货失效；6-退货(只有赠券占用)；
		 * 优惠券类型：1-默认原始的；2-赠券；3-代金券
         * used_status in (0, 1)
         * 代表激活过
		 */
		sql = "select * from es_member_coupon where member_id = " + memberId + " and bar_code = "+ barCode + " "
            + "and recover_flag = 0 order by mc_id desc limit 1 ";
		log.info("sql = " + sql);
		MemberCouponModel memberCouponModel = (MemberCouponModel) Data.GetOne("shop_trade", sql, null, MemberCouponModel.class);
        log.info("memberCouponModel = " + JSON.toJSONString(memberCouponModel));
		return memberCouponModel;
	}
	
	public ScanBatchRecordDetailModel findScanInRecordDetail(ScanBatchRecordDetailModel scanBatchRecordDetailModel) throws Exception {
		String sql = "";
		String msg = "";
		//条码
    	long barCode = scanBatchRecordDetailModel.getBar_code();
    	/**扫码明细表主键*/
        Long scanId = scanBatchRecordDetailModel.getId();
        
        /**激活代金券扫码入库对应记录的主键*/
        sql = "select * from scan_batch_record_detail "
        	+ "where bar_code = " + barCode + " and scan_type = 3 and recover_flag = 0 and show_deal_flag = 1 "
        	+ "order by id desc limit 1";
        log.info("sql = " + sql);
        ScanBatchRecordDetailModel scanInRecordDetail = (ScanBatchRecordDetailModel) Data.GetOne("scan_main", sql, null, ScanBatchRecordDetailModel.class);
        log.info("scanInRecordDetail = " + JSON.toJSONString(scanInRecordDetail));
        if (null == scanInRecordDetail || scanInRecordDetail.getId() < 1) {
            msg = "扫码报警REWARDERROR【没有找到对应的扫码入库记录】:扫码ID = " + scanId;
            log.info(msg);
            throw new Exception(msg);
        }
        
		return scanInRecordDetail;
	}
	
	public ScanBatchRecordDetailModel findScanInRecordDetail(ScanBatchRecordDetailModel scanBatchRecordDetailModel, MemberCouponModel memberCouponModel) throws Exception {
		String sql = "";
		String msg = "";
		/**扫码明细表主键*/
        Long scanId = scanBatchRecordDetailModel.getId();
		
        /**
         * 检查扫码入库激活的代金券信息
         */
        if (null == memberCouponModel || null == memberCouponModel.getScan_in_id()) {
        	msg = "扫码报警REWARDERROR【激活代金券扫码入库对应的记录不存在】:扫码ID = " + scanId;
            log.info(msg);
            throw new Exception(msg);
        }
        /**激活代金券扫码入库对应记录的主键*/
        long scanInId = memberCouponModel.getScan_in_id();
        
        sql = "select * from scan_batch_record_detail where id = " + scanInId;
        log.info("sql = " + sql);
        ScanBatchRecordDetailModel scanInRecordDetail = (ScanBatchRecordDetailModel) Data.GetOne("scan_main", sql, null, ScanBatchRecordDetailModel.class);
        log.info("scanInRecordDetail = " + JSON.toJSONString(scanInRecordDetail));
        if (null == scanInRecordDetail || scanInRecordDetail.getId() < 1) {
            msg = "扫码报警REWARDERROR【激活代金券扫码入库对应的记录不存在】:扫码ID = " + scanId;
            log.info(msg);
            throw new Exception(msg);
        }
        
		return scanInRecordDetail;
	}

	public ScanTaskMModel findScanTaskMModel() throws Exception {
	    String sql = "";

        sql = "select * from scan_task_m where task_name = 'ScanInShopMonthWork' and open_flag = 1 and cal_flag = 0 and execute_datetime <= now() ";
        log.info("sql = " + sql);
        ScanTaskMModel scanTaskMModel = (ScanTaskMModel) Data.GetOne("shop_xxl_job", sql, null, ScanTaskMModel.class);

        return scanTaskMModel;
    }
}
