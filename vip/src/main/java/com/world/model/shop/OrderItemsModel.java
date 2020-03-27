package com.world.model.shop;

import com.world.data.mysql.Bean;

public class OrderItemsModel extends Bean {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    private Integer item_id;

    /**商品ID*/
    private Integer goods_id;

    /**货品ID*/
    private Integer product_id;

    /**SKU产品SN*/
    private String sku_sn;

    /**销售量*/
    private Integer num;

    /**发货量*/
    private Integer ship_num;

    /**交易编号*/
    private String trade_sn;

    /**订单编号*/
    private String order_sn;

    /**图片*/
    private String image;

    /**商品名称*/
    private String name;

    /**销售金额*/
    private Double price;

    /**分类ID*/
    private Integer cat_id;

    /**状态*/
    private Integer state;

    /**快照id*/
    private Integer snapshot_id;

    /**规格json*/
    private String spec_json;

    /**促销类型*/
    private String promotion_type;

    /**促销id*/
    private Integer promotion_id;

    /**可退款金额*/
    private Double refund_price;

    /**评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，*/
    private String comment_status;

    /**
     * 扫码相关信息
     * xian 2020-03-17
     */
    /**卖家ID*/
    private Integer seller_id;

    /**卖家名称*/
    private String seller_name;

    /**会员ID*/
    private Integer member_id;

    /**会员名称*/
    private String member_name;

    /**门店ID*/
    private Integer shop_id;

    /**门店名称*/
    private String shop_name;

    /**扫码入库数量*/
    private Integer scan_in_num;

    /**扫码出库数量*/
    private Integer scan_return_num;

    /**配送-经销商编号*/
    private String ship_dealer_code;

    /**配送-经销商名称*/
    private String ship_dealer_name;

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getSku_sn() {
        return sku_sn;
    }

    public void setSku_sn(String sku_sn) {
        this.sku_sn = sku_sn;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getShip_num() {
        return ship_num;
    }

    public void setShip_num(Integer ship_num) {
        this.ship_num = ship_num;
    }

    public String getTrade_sn() {
        return trade_sn;
    }

    public void setTrade_sn(String trade_sn) {
        this.trade_sn = trade_sn;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCat_id() {
        return cat_id;
    }

    public void setCat_id(Integer cat_id) {
        this.cat_id = cat_id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSnapshot_id() {
        return snapshot_id;
    }

    public void setSnapshot_id(Integer snapshot_id) {
        this.snapshot_id = snapshot_id;
    }

    public String getSpec_json() {
        return spec_json;
    }

    public void setSpec_json(String spec_json) {
        this.spec_json = spec_json;
    }

    public String getPromotion_type() {
        return promotion_type;
    }

    public void setPromotion_type(String promotion_type) {
        this.promotion_type = promotion_type;
    }

    public Integer getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(Integer promotion_id) {
        this.promotion_id = promotion_id;
    }

    public Double getRefund_price() {
        return refund_price;
    }

    public void setRefund_price(Double refund_price) {
        this.refund_price = refund_price;
    }

    public String getComment_status() {
        return comment_status;
    }

    public void setComment_status(String comment_status) {
        this.comment_status = comment_status;
    }

    public Integer getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(Integer seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public Integer getShop_id() {
        return shop_id;
    }

    public void setShop_id(Integer shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public Integer getScan_in_num() {
        return scan_in_num;
    }

    public void setScan_in_num(Integer scan_in_num) {
        this.scan_in_num = scan_in_num;
    }

    public Integer getScan_return_num() {
        return scan_return_num;
    }

    public void setScan_return_num(Integer scan_return_num) {
        this.scan_return_num = scan_return_num;
    }

    public String getShip_dealer_code() {
        return ship_dealer_code;
    }

    public void setShip_dealer_code(String ship_dealer_code) {
        this.ship_dealer_code = ship_dealer_code;
    }

    public String getShip_dealer_name() {
        return ship_dealer_name;
    }

    public void setShip_dealer_name(String ship_dealer_name) {
        this.ship_dealer_name = ship_dealer_name;
    }
}
