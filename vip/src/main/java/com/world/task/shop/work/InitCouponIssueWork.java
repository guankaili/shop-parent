package com.world.task.shop.work;

import com.alibaba.fastjson.JSON;
import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.shop.GoodsSkuModel;
import com.world.model.shop.ShopConfIntegralModel;
import com.world.model.shop.ShopConfRebateModel;
import com.world.util.GoodsNameSplitUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InitCouponIssueWork extends Worker {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /*查询SQL*/
    private String sql = "";

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public InitCouponIssueWork(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        /*记录核算开始时间*/
        long startTime = System.currentTimeMillis();
        //SQL语句
        String sql = "";

        sql = "select sn, goods_name from es_goods_sku";
        log.info("sql = " + sql);
        List<Bean> goodsSkuModelList = (List<Bean>) Data.Query("shop_goods", sql, null, GoodsSkuModel.class);
        log.info("goodsSkuModelList = " + JSON.toJSONString(goodsSkuModelList));

        //循环处理
        GoodsSkuModel goodsSkuModel = null;
        //sku编号
        String skuSn = "";
        String goodsName = "";
        Map<String,String> goodsMap = null;
        String goodsSize = "";  //商品尺寸
        for (int i = 0; i < goodsSkuModelList.size(); i++) {
            goodsSkuModel = (GoodsSkuModel) goodsSkuModelList.get(i);
            skuSn = goodsSkuModel.getSn();
            goodsName = goodsSkuModel.getGoods_name();

            goodsMap = GoodsNameSplitUtil.getSpec(goodsName);
            goodsSize = goodsMap.get("size");
            log.info("goodsMap = " + JSON.toJSONString(goodsMap));

            /**
             * INSERT INTO shop_member.es_coupon_issue (
             * issue_title, issue_rule, shop_type, goods_size, issue_type, issue_amount, seller_id,
             * seller_name, issue_start_time, issue_end_time, coupon_valid_day, is_usable)
             * VALUES ();
             *
             * issue_title = 扫码入库后 专享代金券, 扫码入库后 专享积分，扫码入库后 专享返利
             * issue_rule = skuSn,
             * shop_type, goods_size, issue_amount = 配置表 代金券先统一80
             *issue_type = 发行类型默认0；3-代金券；5-积分；6-返利；
             * seller_id = 1,seller_name = 森麒麟自营店
             * issue_start_time = 2020-03-01, issue_end_time = 2020-04-30
             * coupon_valid_day = 90
             * is_usable = 1
             *
             * 已经获取到了goodsSize，skuSn
             * 1、es_shop_conf_integral 配置了积分，门店，size的关系
             * 2、es_shop_conf_rebate   配置了返利，门店，size的关系
             *
             *
             *
             */
            if ("".equals(goodsSize)){
                goodsSize = 0+"";
            }
            Integer num = 0;
            switch(Integer.parseInt(goodsSize)){
                case 13:
                    num = 15;
                    break;
                case 14:
                    num = 20;
                    break;
                case 15:
                    num = 25;
                    break;
                case 16:
                    num = 40;
                    break;
                case 17:
                    num = 80;
                    break;
                case 18:
                    num = 100;
                    break;
                case 19:
                    num = 100;
                    break;
                case 20:
                    num = 150;
                    break;
                case 21:
                    num = 200;
                    break;
                case 22:
                    num = 200;
                    break;
                case 24:
                    num = 200;
                    break;
                case 26:
                    num = 200;
                    break;
                default:
                    num = 200;
            }
            if (Integer.parseInt(goodsSize) != 0 ) {
                sql = "INSERT INTO es_coupon_issue " +
                        "(issue_title, issue_rule, goods_size, issue_type, issue_amount," +
                        "seller_id,seller_name, issue_start_time, issue_end_time, coupon_valid_day, is_usable) " +
                        "VALUES ('扫码入库后 专享代金券','" + skuSn + "'," + goodsSize + ",3,"+num+",1,'森麒麟自营店','2020-03-01','2020-04-30',90,1)";
                log.info("insert...sql = " + sql);
                Data.Insert("shop_member", sql, null);
            }
            if (Integer.valueOf(goodsSize) >= 18){
                goodsSize = 18+"";
            }
            //查询配置积分表
            sql = "select shop_type,score,size from es_shop_conf_integral where size = " + goodsSize;
            log.info("sql = " + sql);
            List<Bean> confIntegralList = (List<Bean>) Data.Query("shop_member", sql, null, ShopConfIntegralModel.class);
            log.info("confIntegralList"+JSON.toJSONString(confIntegralList));
            if(confIntegralList.size()>0) {
                for (int j = 0; j < confIntegralList.size(); j++) {
                    ShopConfIntegralModel confIntegralModel = (ShopConfIntegralModel) confIntegralList.get(j);
                    log.info("confIntegralModel = " + JSON.toJSONString(confIntegralModel));
                    Integer shop_type = confIntegralModel.getShop_type();
                    Integer size = confIntegralModel.getSize();
                    Integer score = confIntegralModel.getScore();
                    sql = "INSERT INTO es_coupon_issue " +
                            "(issue_title, issue_rule, shop_type, goods_size, issue_type, issue_amount," +
                            "seller_id,seller_name, issue_start_time, issue_end_time, coupon_valid_day, is_usable) " +
                            "VALUES ('扫码入库后 专享积分','" + skuSn + "'," + shop_type + "," + size + ",5," + score + ",1,'森麒麟自营店','2020-03-01','2020-04-30',90,1)";
                    log.info("insert...sql = " + sql);
                    Data.Insert("shop_member", sql, null);
                }
                //查询返利配置表
                sql = "select shop_type,score,size from es_shop_conf_rebate where size = " + goodsSize;
                log.info("sql = " + sql);
                List<Bean> confRebateList = (List<Bean>) Data.Query("shop_member", sql, null, ShopConfRebateModel.class);
                log.info("confRebateList = " + JSON.toJSONString(confRebateList));
                for (int k = 0; k < confRebateList.size(); k++) {
                    ShopConfRebateModel confRebateModel = (ShopConfRebateModel) confRebateList.get(k);
                    log.info("confRebateModel = " + JSON.toJSONString(confRebateModel));
                    Integer shopType = confRebateModel.getShop_type();
                    Integer size = confRebateModel.getSize();
                    Integer score = confRebateModel.getScore();
                    sql = "INSERT INTO es_coupon_issue " +
                            "(issue_title, issue_rule, shop_type, goods_size, issue_type, issue_amount," +
                            "seller_id,seller_name, issue_start_time, issue_end_time, coupon_valid_day, is_usable) " +
                            "VALUES ('扫码入库后 专享返利','" + skuSn + "'," + shopType + "," + size + ",6," + score + ",1,'森麒麟自营店','2020-03-01','2020-04-30',90,1)";
                    log.info("insert...sql = " + sql);
                    Data.Insert("shop_member", sql, null);
                }

            }
        }

//        sql = "update es_member_coupon set used_status = 0, bar_code = " + barCode + " where mc_id = " + mcId;
//        log.info("sql = " + sql);
//        Data.Insert("shop_number", sql, null);

//        sql = "select * from es_shop_conf_integral where shop_type = " + shopType + " and size = " + goodsSizeInt + " "
//                + "and start_time <= now() and end_time >= now() ";
//        log.info("sql = " + sql);
//        ShopConfIntegralModel shopConfIntegralModel = (ShopConfIntegralModel) Data.GetOne("shop_member", sql, null, ShopConfIntegralModel.class);



        long endTime = System.currentTimeMillis();
        log.info("【初始化数据耗时：" + (endTime - startTime) + "】");
    }

    public static void main(String[] args) {
        InitCouponIssueWork initCouponIssueWork = new InitCouponIssueWork("", "");
        initCouponIssueWork.run();
    }
}
