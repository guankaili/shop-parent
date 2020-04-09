package com.world.model.shop;

import com.world.data.mysql.Bean;

import java.math.BigDecimal;
import java.util.Date;

public class MemberIntegralMonthModel extends Bean {
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
    private int cal_pre_num;

    /**
     * 月结预计算值
     */
    private BigDecimal cal_pre_score;

    /**
     * 月结月份：2020-05格式
     */
    private String cal_pre_month;

    /**
     * 创建来源默认0；1-月结；2-扫码退货重新计算；3-季度补差；
     */
    private int create_way;

    /**
     * 创建时间
     */
    private Date create_datetime;

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

    public int getCal_pre_num() {
        return cal_pre_num;
    }

    public void setCal_pre_num(int cal_pre_num) {
        this.cal_pre_num = cal_pre_num;
    }

    public BigDecimal getCal_pre_score() {
        return cal_pre_score;
    }

    public void setCal_pre_score(BigDecimal cal_pre_score) {
        this.cal_pre_score = cal_pre_score;
    }

    public String getCal_pre_month() {
        return cal_pre_month;
    }

    public void setCal_pre_month(String cal_pre_month) {
        this.cal_pre_month = cal_pre_month;
    }

    public int getCreate_way() {
        return create_way;
    }

    public void setCreate_way(int create_way) {
        this.create_way = create_way;
    }

    public Date getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }
}
