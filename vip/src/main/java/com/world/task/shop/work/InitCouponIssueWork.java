package com.world.task.shop.work;

import com.alibaba.fastjson.JSON;
import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.shop.GoodsSkuModel;
import com.world.util.GoodsNameSplitUtil;

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
        for (int i = 0; i < goodsSkuModelList.size(); i++) {
            goodsSkuModel = (GoodsSkuModel) goodsSkuModelList.get(i);
            skuSn = goodsSkuModel.getSn();
            goodsName = goodsSkuModel.getGoods_name();

            goodsMap = GoodsNameSplitUtil.getSpec(goodsName);
            log.info("goodsMap = " + JSON.toJSONString(goodsMap));
        }

        long endTime = System.currentTimeMillis();
        log.info("【初始化数据耗时：" + (endTime - startTime) + "】");
    }

    public static void main(String[] args) {
        InitCouponIssueWork initCouponIssueWork = new InitCouponIssueWork("", "");
        initCouponIssueWork.run();
    }
}
