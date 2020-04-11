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


            /**
             * 月度处理
             */
            if (null != sqls && sqls.size() > 0) {
                //执行事务
                txObj.excuteUpdateList(sqls);
                if (txObj.commit()) {
                    log.info("扫码月度结算报警REWARDINFO:【扫码月度结算处理】处理成功，门店ID【" + orderItemId + "】");
                } else {
                    log.info("扫码月度结算报警REWARDERROR:【扫码月度结算处理】处理失败，门店ID【" + orderItemId + "】");
                }
            }

            long endTime = System.currentTimeMillis();
            log.info("扫码月度结算报警REWARDInfo:【扫码月度结算处理】门店ID【" + orderItemId + "】处理耗时【" + (endTime - startTime) + "】");
        } catch (Exception e) {
            log.info("扫码入库报警REWARDERROR:ScanInShopMonthThread", e);
        } finally {
            countDownLatch.countDown();
        }
    }


    private void dealActivityGoods(List<OneSql> sqls, OrderItemsModel orderItemsModel) {
        /**订单id*/
        int orderItemId = orderItemsModel.getItem_id();
        /**门店ID*/
        int shopId = orderItemsModel.getShop_id();
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

    private void saveActivityGoods(List<OneSql> sqls, OrderItemsModel orderItemsModel, ActivityGoodsModel activityGoodsModel) {
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
        int shopId = orderItemsModel.getMember_id();
        String shopName = orderItemsModel.getMember_name();
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

        sql = "delete from data_shop_activity_stats where item_id = " + orderItemId;
        log.info("sql = " + sql);
        sqls.add(new OneSql(sql, 0, null, "sbms_main"));

        sql = "INSERT INTO data_shop_activity_stats(" +
                "item_id, activity_id, activity_name, sku_sn, goods_name, goods_num, goods_price, " +
                "ship_num, refund_num, ship_refund_num, shop_id, shop_name, dealer_code, dealer_name, dealer_cm_id, " +
                "province_code, province_name,city_name,area_name,shop_keeper, payment_time, create_date) VALUES (" +
                "" + orderItemId + ", " + activityId + ", '" + activityName + "', '" + skuSn + "', '" + goodsName + "', " + goodsNum + ", " + goodsPrice + ", " +
                "" + shipNum + ", " + refundNum + ", " + shipRefundNum + ", " + shopId + ", '" + shopName + "', '" + shipDealerCode + "', '" + shipDealerName + "', '" + dealerCmId + "', " +
                "'" + provinceCode + "', '" + provinceName + "', '" + cityName + "', '" + areaName + "', '" + shopKeeper + "', '" + paymentTime + "', now() )";
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
        sql = " select shop_city AS city_name,shop_county AS area_name,shop_keeper,dealer_cm_id, dealer_cm_name from es_shop_detail where shop_id = '" + shopId + "' ";
        log.info("查询经销商业务员 sql = " + sql);
        DataShopActivityStats dataShopActivityStats = (DataShopActivityStats) Data.GetOne("shop_member", sql, null, DataShopActivityStats.class);
        log.info("dataShopActivityStats = " + JSON.toJSONString(dataShopActivityStats));
        if (null != dataShopActivityStats) {
            dealerCmId = null == dataShopActivityStats.getDealer_cm_id() ? "" : dataShopActivityStats.getDealer_cm_id();
            cityName = null == dataShopActivityStats.getCity_name() ? "" : dataShopActivityStats.getCity_name();
            areaName = null == dataShopActivityStats.getArea_name() ? "" : dataShopActivityStats.getArea_name();
            shopKeeper = null == dataShopActivityStats.getShop_keeper() ? "" : dataShopActivityStats.getShop_keeper();
        }else {
            dealerCmId = "";
            cityName = "";
            areaName = "";
            shopKeeper = "";
        }
    }
}
