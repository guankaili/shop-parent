package com.world.model.shop;

import com.world.data.mysql.Bean;

import java.util.Date;

public class MemberCouponModel extends Bean {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer mc_id;

    /**
     * 优惠券表主键
     */
    private Integer coupon_id;

    /**
     * 券码
     */
    private String coupon_sn;

    /**
     * 会员主键id
     */
    private Integer member_id;

    /**
     * 使用时间
     */
    private Date used_time;

    /**
     * 领取时间
     */
    private Date create_time;

    /**
     * 业务产生ID，例如订单ID
     */
    private Integer busi_id;

    /**
     * 业务产生ID，例如订单项ID对应es_order_items
     */
    private Integer item_id;

    /**
     * 订单主键
     */
    private Integer order_id;

    /**
     * 订单编号
     */
    private String order_sn;

    /**
     * 会员昵称
     */
    private String member_name;

    /**
     * 优惠券名称
     */
    private String title;

    /**
     * 优惠券面额
     */
    private Double coupon_price;

    /**
     * 优惠券门槛金额
     */
    private Double coupon_threshold_price;

    /**
     * 使用起始时间
     */
    private Date start_time;

    /**
     * 使用截止时间
     */
    private Date end_time;

    /**
     * 使用状态;0未使用，3-使用中(冻结)，1已使用,2是已过期；5-退货占用；
     */
    private Integer used_status;

    private Integer seller_id;

    private String seller_name;
    /**
     * 增加关联字段
     * xian 2020-03-21
     */
    private long bar_code;

    /**
     * 绑定商品id本期按照商品编号=es_goods_sku sn字段
     */
    private String goods_sku_sn;

    /**
     * 绑定商品名称
     */
    private String goods_name;

    /**
     * 优惠券类型：1-默认原始的；2-赠券；3-代金券
     */
    private Integer coupon_type;

    public Integer getMc_id() {
        return mc_id;
    }

    public void setMc_id(Integer mc_id) {
        this.mc_id = mc_id;
    }

    public Integer getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Integer coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCoupon_sn() {
        return coupon_sn;
    }

    public void setCoupon_sn(String coupon_sn) {
        this.coupon_sn = coupon_sn;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public Date getUsed_time() {
        return used_time;
    }

    public void setUsed_time(Date used_time) {
        this.used_time = used_time;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getBusi_id() {
        return busi_id;
    }

    public void setBusi_id(Integer busi_id) {
        this.busi_id = busi_id;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(Double coupon_price) {
        this.coupon_price = coupon_price;
    }

    public Double getCoupon_threshold_price() {
        return coupon_threshold_price;
    }

    public void setCoupon_threshold_price(Double coupon_threshold_price) {
        this.coupon_threshold_price = coupon_threshold_price;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public Integer getUsed_status() {
        return used_status;
    }

    public void setUsed_status(Integer used_status) {
        this.used_status = used_status;
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

    public long getBar_code() {
        return bar_code;
    }

    public void setBar_code(long bar_code) {
        this.bar_code = bar_code;
    }

    public String getGoods_sku_sn() {
        return goods_sku_sn;
    }

    public void setGoods_sku_sn(String goods_sku_sn) {
        this.goods_sku_sn = goods_sku_sn;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public Integer getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(Integer coupon_type) {
        this.coupon_type = coupon_type;
    }
}
