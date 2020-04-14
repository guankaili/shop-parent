package com.world.task.sbms.thread;

import com.alibaba.fastjson.JSON;
import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DataShopActivityStats;
import com.world.model.shop.*;
import com.world.task.shop.thread.ScanInShopThread;
import com.world.task.shop.util.ScanUtil;
import com.world.util.date.TimeUtil;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DataActivityThread extends Thread {
    private static Logger log = Logger.getLogger(ScanInShopThread.class);
    /*sql语句*/
    private String sql = "";
    /**报警msg*/
//    private  String msg = "";
    private CountDownLatch countDownLatch;
    private OrderItemsModel orderItemsModel;

    /**省份编号*/
    private String provinceCode;
    /**省份名称*/
    private String provinceName;
    /**市名*/
    private String cityName;
    /**区名*/
    private String areaName;
    /**
     * 店主姓名
     */
    private String shopKeeper;
    /**经销商业务员id*/
    private String dealerCmId;

    /**
     * 门店信息
     */
    private int shopId;
    private String shopName;

    public DataActivityThread(OrderItemsModel orderItemsModel, CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.orderItemsModel = orderItemsModel;
    }

    @Override
    public void run() {
        log.info("ScanInShopMonthThread...run...scanBatchRecordDetailModel = " + JSON.toJSONString(orderItemsModel));
        /**订单id*/
        int orderItemId = orderItemsModel.getItem_id();
        
        try {
        	//开启事务处理
            List<OneSql> sqls = new ArrayList<OneSql>();
            TransactionObject txObj = new TransactionObject();
            long startTime = System.currentTimeMillis();

            /**
             * 数据抽取处理
             */
            this.dealActivityGoods(sqls, orderItemsModel);

            //更新扫码明细表数据状态
            this.updateOrderIteamFlagTS(sqls, orderItemId, 1, "处理成功");

            //执行事务
            txObj.excuteUpdateList(sqls);
            if (txObj.commit()) {
                log.info("扫码月度结算报警REWARDINFO:【扫码月度结算处理】处理成功，门店ID【" + orderItemId + "】");
            } else {
                //执行错误记录日志
                updateOrderIteamFlag(orderItemId, 2, "处理失败");
                log.info("扫码月度结算报警REWARDERROR:【扫码月度结算处理】处理失败，门店ID【" + orderItemId + "】");
            }

            long endTime = System.currentTimeMillis();
            log.info("扫码月度结算报警REWARDInfo:【扫码月度结算处理】门店ID【" + orderItemId + "】处理耗时【" + (endTime - startTime) + "】");
        } catch (Exception e) {
            log.info("扫码入库报警REWARDERROR:ScanInShopMonthThread", e);
        } finally {
            countDownLatch.countDown();
        }
    }


    public void dealActivityGoods(List<OneSql> sqls, OrderItemsModel orderItemsModel) {
        /**订单id*/
        int orderItemId = orderItemsModel.getItem_id();
        /**门店ID*/
        shopId = orderItemsModel.getShop_id();
        /**经销商code*/
        String dealerCode = orderItemsModel.getShip_dealer_code();
        /**sku*/
        String skuSn = orderItemsModel.getSku_sn();
        /**订单付款时间*/
        Date paymentTime = orderItemsModel.getPayment_time();

        /**获取经销商区域信息*/
        findDealerProvince(dealerCode);

        /**获取经销商业务员id*/
        findDealerCmId(shopId);

        /**获取商品活动信息*/
        ActivityGoodsModel activityGoodsModel = findGoodsActivity(skuSn, paymentTime);

        this.saveActivityGoods(sqls, orderItemsModel, activityGoodsModel);
    }

    public void saveActivityGoods(List<OneSql> sqls, OrderItemsModel orderItemsModel, ActivityGoodsModel activityGoodsModel) {
        /**
         * 订单信息
         */
        /**订单id*/
        int orderItemId = orderItemsModel.getItem_id();
        String skuSn = orderItemsModel.getSku_sn();
        String goodsName = orderItemsModel.getName();
        long goodsNum = orderItemsModel.getNum();
        BigDecimal goodsPrice = orderItemsModel.getPrice();
        long shipNum = orderItemsModel.getShip_num();
        long refundNum = orderItemsModel.getRefund_num();
        long shipRefundNum = orderItemsModel.getShip_refund_num();
        long scanInNum = orderItemsModel.getScan_in_num();
        long scanReturnNum = orderItemsModel.getScan_return_num();
//        int shopId = orderItemsModel.getMember_id();
//        String shopName = orderItemsModel.getMember_name();
        String shipDealerCode = orderItemsModel.getShip_dealer_code();
        String shipDealerName = orderItemsModel.getShip_dealer_name();
        Date paymentTime = orderItemsModel.getPayment_time();

        /**
         * 活动信息
         */
        int activityId = 0;
        String activityName = "";
        if (null != activityGoodsModel) {
            activityId = null == activityGoodsModel.getId() ? 0 : activityGoodsModel.getId();
            activityName = null == activityGoodsModel.getActivity_name() ? "" : activityGoodsModel.getActivity_name();
//          Date activityStartDatetime = activityGoodsModel.getActivity_start_datetime();
//          Date activityEndDatetime = activityGoodsModel.getActivity_end_datetime();
        }

        /**
         * 查询记录是否存在，如果存在则删除。此处是为防止查询到空数据锁表
         */
        DataShopActivityStats dataShopActivityStats = findDataShopActivity(orderItemId);

        if (null != dataShopActivityStats && dataShopActivityStats.getItem_id() > 1) {
            sql = "delete from data_shop_activity_stats where item_id = " + orderItemId;
            log.info("sql = " + sql);
            sqls.add(new OneSql(sql, -2, null, "sbms_main"));
        }

        sql = "INSERT INTO data_shop_activity_stats(" +
                "item_id, activity_id, activity_name, sku_sn, goods_name, goods_num, goods_price, " +
                "ship_num, refund_num, ship_refund_num, shop_id, shop_name, dealer_code, dealer_name, dealer_cm_id, " +
                "province_code, province_name,city_name,area_name,shop_keeper, " +
                "payment_time, create_date, scan_in_num, scan_return_num) VALUES (" +
                "" + orderItemId + ", " + activityId + ", '" + activityName + "', '" + skuSn + "', '" + goodsName + "', " + goodsNum + ", " + goodsPrice + ", " +
                "" + shipNum + ", " + refundNum + ", " + shipRefundNum + ", " + shopId + ", '" + shopName + "', '" + shipDealerCode + "', '" + shipDealerName + "', '" + dealerCmId + "', " +
                "'" + provinceCode + "', '" + provinceName + "', '" + cityName + "', '" + areaName + "', '" + shopKeeper + "', " +
                "'" + paymentTime + "', now(), " + scanInNum + ", " + scanReturnNum + " )";
        log.info("插入 sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "sbms_main"));
    }

    public ActivityGoodsModel findGoodsActivity(String skuSn, Date paymentTime) {
        String paymentTimeStr = TimeUtil.getStringByDate(paymentTime, "yyyy-MM-dd HH:mm:ss");

        sql = " select fa.* from es_activity fa, es_activity_goods fb " +
                "where fa.id = fb.activity_id and fb.spu_sn = '" + skuSn + "' and " +
                "fa.activity_start_datetime >= '" + paymentTimeStr + "' and fa.activity_end_datetime <= '" + paymentTimeStr + "' limit 1 ";
        log.info("sql = " + sql);
        ActivityGoodsModel activityGoodsModel = (ActivityGoodsModel) Data.GetOne("shop_goods", sql, null, ActivityGoodsModel.class);

        return activityGoodsModel;
    }

    public void findDealerProvince(String dealerCode) {
        sql = "select distinct province_code province_code, province province_name from cbm_dealer_scope where channel_id = '" + dealerCode + "' limit 1";
        log.info("查询省份信息 sql = " + sql);
        DataShopActivityStats dataShopActivityStats = (DataShopActivityStats) Data.GetOne("weilai_uaa", sql, null, DataShopActivityStats.class);
        log.info("dataShopActivityStats = " + JSON.toJSONString(dataShopActivityStats));
        if (null != dataShopActivityStats) {
            provinceCode = null == dataShopActivityStats.getProvince_code() ? "" : dataShopActivityStats.getProvince_code();
            provinceName = null == dataShopActivityStats.getProvince_name() ? "" : dataShopActivityStats.getProvince_name();
        }else {
            provinceCode = "";
            provinceName = "";
        }
    }

    public void findDealerCmId(int shopId){
        sql = "select shop_city AS city_name,shop_county AS area_name,shop_keeper,dealer_cm_id, dealer_cm_name, shop_id, shop_name "
            + "from es_shop_detail where shop_id = '" + shopId + "' ";
        log.info("查询经销商业务员 sql = " + sql);
        DataShopActivityStats dataShopActivityStats = (DataShopActivityStats) Data.GetOne("shop_member", sql, null, DataShopActivityStats.class);
        log.info("dataShopActivityStats = " + JSON.toJSONString(dataShopActivityStats));
        if (null != dataShopActivityStats) {
            dealerCmId = null == dataShopActivityStats.getDealer_cm_id() ? "" : dataShopActivityStats.getDealer_cm_id();
            cityName = null == dataShopActivityStats.getCity_name() ? "" : dataShopActivityStats.getCity_name();
            areaName = null == dataShopActivityStats.getArea_name() ? "" : dataShopActivityStats.getArea_name();
            shopKeeper = null == dataShopActivityStats.getShop_keeper() ? "" : dataShopActivityStats.getShop_keeper();
            shopName = null == dataShopActivityStats.getShop_name() ? "" : dataShopActivityStats.getShop_name();
        }else {
            dealerCmId = "";
            cityName = "";
            areaName = "";
            shopKeeper = "";
            shopName = "";
        }
    }

    public DataShopActivityStats findDataShopActivity(int orderItemId) {
        //更新扫码明细数据状态
        sql = "select item_id from data_shop_activity_stats where item_id = " + orderItemId;
        log.info("sql = " + sql);
        DataShopActivityStats dataShopActivityStats = (DataShopActivityStats) Data.GetOne("sbms_main", sql, null, DataShopActivityStats.class);
        log.info("dataShopActivityStats = " + JSON.toJSONString(dataShopActivityStats));

        return dataShopActivityStats;
    }

    public void updateOrderIteamFlag(int orderItemId, int showDealFlag, String showDealMsg) {
        //更新扫码明细数据状态
        sql = "update es_order_items set activity_deal_flag = " + showDealFlag + ", "
            + "activity_deal_msg = '" + showDealMsg + "', activity_deal_datetime = now() "
            + "where item_id = " + orderItemId;
        log.info("sql = " + sql);
        Data.Update("shop_trade", sql, null);
    }

    public void updateOrderIteamFlagTS(List<OneSql> sqls, int orderItemId, int showDealFlag, String showDealMsg) {
        //更新扫码明细数据状态
        sql = "update es_order_items set activity_deal_flag = " + showDealFlag + ", "
            + "activity_deal_msg = '" + showDealMsg + "', activity_deal_datetime = now() "
            + "where item_id = " + orderItemId;
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 1, null, "shop_trade"));
    }
}
