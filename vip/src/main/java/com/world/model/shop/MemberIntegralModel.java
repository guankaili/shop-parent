package com.world.model.shop;

import com.world.data.mysql.Bean;

import java.math.BigDecimal;

public class MemberIntegralModel extends Bean {
    private static final long serialVersionUID = 1L;

    /**主键*/
    private int id;

    /**会员ID*/
    private int member_id;

    /**
     * 会员登录用户名
     */
    private String member_name;

    /**
     * 门店ID
     */
    private int shop_id;

    /**
     * 门店名称
     */
    private String shop_name;

    /**
     * 虚拟积分类型：0-默认；5-积分；6-返利；
     */
    private int integral_type;

    /**
     * 虚拟积分类型名称：0-默认；5-积分；6-返利；
     */
    private int integral_type_name;

    /**
     * 虚拟积分余额
     */
    private BigDecimal balance;

    /**
     * 虚拟积分冻结金额(下单未支付冻结)
     */
    private BigDecimal freeze;

    /**
     * 扫码入库数量
     */
    private Integer scan_in_expect_num;

    /**
     * 扫码入库预计分值
     */
    private BigDecimal scan_in_expect_score;

    /**
     * 当前月份：2020-05格式
     */
    private String scan_in_expect_month;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getIntegral_type() {
        return integral_type;
    }

    public void setIntegral_type(int integral_type) {
        this.integral_type = integral_type;
    }

    public int getIntegral_type_name() {
        return integral_type_name;
    }

    public void setIntegral_type_name(int integral_type_name) {
        this.integral_type_name = integral_type_name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFreeze() {
        return freeze;
    }

    public void setFreeze(BigDecimal freeze) {
        this.freeze = freeze;
    }

    public Integer getScan_in_expect_num() {
        return scan_in_expect_num;
    }

    public void setScan_in_expect_num(Integer scan_in_expect_num) {
        this.scan_in_expect_num = scan_in_expect_num;
    }

    public BigDecimal getScan_in_expect_score() {
        return scan_in_expect_score;
    }

    public void setScan_in_expect_score(BigDecimal scan_in_expect_score) {
        this.scan_in_expect_score = scan_in_expect_score;
    }

    public String getScan_in_expect_month() {
        return scan_in_expect_month;
    }

    public void setScan_in_expect_month(String scan_in_expect_month) {
        this.scan_in_expect_month = scan_in_expect_month;
    }
}
