package com.world.model.shop;

import com.world.data.mysql.Bean;

import java.util.Date;

public class CouponIssueModel extends Bean {
    private static final long serialVersionUID = 1L;

    /**主键*/
    private Integer id;

    /**优惠券名称*/
    private String issue_title;

    /**代金券发行规则(本期按照商品编号=es_goods_sku sn字段)*/
    private String issue_rule;

    /**门店类型默认0;1-CPS;2-CCS;3-CCS+;4-CTS;5-CTS+;*/
    private Integer shop_type;

    /**商品尺寸*/
    private String goods_size;

    /**发行类型默认0；3-优惠券；5-积分；6-返利；*/
    private String issue_type;

    /**优惠券面额*/
    private Double issue_amount;

    /**店铺ID*/
    private Integer seller_id;

    /**店铺名称*/
    private String seller_name;

    /**总发行量*/
    private Integer issue_num;

    /**每人限领数量*/
    private Integer limit_num;

    /**已被领取的数量*/
    private Integer received_num;

    /**已被使用的数量*/
    private Integer used_num;

    /**优惠券发行开始时间*/
    private Date issue_start_time;

    /**优惠券发行开始时间*/
    private Date issue_end_time;

    /**优惠券使用开始时间*/
    private Date coupon_start_time;

    /**优惠券使用截止时间*/
    private Date coupon_end_time;

    /**代金券从下单支付生成后N天后失效*/
    private Integer coupon_valid_day;

    /**是否可用：0-不可用  1-可用*/
    private Long is_usable;

    /**记录创建人ID*/
    private Long create_user_id;

    /**记录创建人名称*/
    private Long create_user_name;

    /**记录创建时间*/
    private Long create_datetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIssue_title() {
        return issue_title;
    }

    public void setIssue_title(String issue_title) {
        this.issue_title = issue_title;
    }

    public String getIssue_rule() {
        return issue_rule;
    }

    public void setIssue_rule(String issue_rule) {
        this.issue_rule = issue_rule;
    }

    public Integer getShop_type() {
        return shop_type;
    }

    public void setShop_type(Integer shop_type) {
        this.shop_type = shop_type;
    }

    public String getGoods_size() {
        return goods_size;
    }

    public void setGoods_size(String goods_size) {
        this.goods_size = goods_size;
    }

    public String getIssue_type() {
        return issue_type;
    }

    public void setIssue_type(String issue_type) {
        this.issue_type = issue_type;
    }

    public Double getIssue_amount() {
        return issue_amount;
    }

    public void setIssue_amount(Double issue_amount) {
        this.issue_amount = issue_amount;
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

    public Integer getIssue_num() {
        return issue_num;
    }

    public void setIssue_num(Integer issue_num) {
        this.issue_num = issue_num;
    }

    public Integer getLimit_num() {
        return limit_num;
    }

    public void setLimit_num(Integer limit_num) {
        this.limit_num = limit_num;
    }

    public Integer getReceived_num() {
        return received_num;
    }

    public void setReceived_num(Integer received_num) {
        this.received_num = received_num;
    }

    public Integer getUsed_num() {
        return used_num;
    }

    public void setUsed_num(Integer used_num) {
        this.used_num = used_num;
    }

    public Date getIssue_start_time() {
        return issue_start_time;
    }

    public void setIssue_start_time(Date issue_start_time) {
        this.issue_start_time = issue_start_time;
    }

    public Date getIssue_end_time() {
        return issue_end_time;
    }

    public void setIssue_end_time(Date issue_end_time) {
        this.issue_end_time = issue_end_time;
    }

    public Date getCoupon_start_time() {
        return coupon_start_time;
    }

    public void setCoupon_start_time(Date coupon_start_time) {
        this.coupon_start_time = coupon_start_time;
    }

    public Date getCoupon_end_time() {
        return coupon_end_time;
    }

    public void setCoupon_end_time(Date coupon_end_time) {
        this.coupon_end_time = coupon_end_time;
    }

    public Integer getCoupon_valid_day() {
        return coupon_valid_day;
    }

    public void setCoupon_valid_day(Integer coupon_valid_day) {
        this.coupon_valid_day = coupon_valid_day;
    }

    public Long getIs_usable() {
        return is_usable;
    }

    public void setIs_usable(Long is_usable) {
        this.is_usable = is_usable;
    }

    public Long getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(Long create_user_id) {
        this.create_user_id = create_user_id;
    }

    public Long getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(Long create_user_name) {
        this.create_user_name = create_user_name;
    }

    public Long getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Long create_datetime) {
        this.create_datetime = create_datetime;
    }
}
