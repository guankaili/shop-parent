package com.world.model.qly;

import com.world.data.mysql.Bean;

import java.math.BigDecimal;
import java.util.Date;

public class OrderOutlet extends Bean {
    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    private Long id;

    /**
     * 出库单编号
     */
    private String outletCode;

    /**
     * 单据类型编号
     */
    private String outletTypeCode;

    /**
     * 单据类型名称
     */
    private String outletTypeName;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 客户编号
     */
    private String dealerCode;

    /**
     * 客户名称
     */
    private String dealerName;

    /**
     * 结算币别编号
     */
    private String settleCurrencyCode;

    /**
     * 结算币别名称
     */
    private String settleCurrencyName;

    /**
     * 收货地址编号
     */
    private String reciverCode;

    /**
     * 收货地址名称
     */
    private String reciverName;

    /**
     * 出库日期
     */
    private Date outletDate;

    /**
     * 发货备注
     */
    private String descript;

    /**
     * 税率
     */
    private BigDecimal tax;

    /**
     * 订单未税金额
     */
    private BigDecimal orderNtaxAmount;

    /**
     * 订单含税金额
     */
    private BigDecimal orderHtaxAmount;

    /**
     * 抽取保存时间
     */
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getOutletTypeCode() {
        return outletTypeCode;
    }

    public void setOutletTypeCode(String outletTypeCode) {
        this.outletTypeCode = outletTypeCode;
    }

    public String getOutletTypeName() {
        return outletTypeName;
    }

    public void setOutletTypeName(String outletTypeName) {
        this.outletTypeName = outletTypeName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getSettleCurrencyCode() {
        return settleCurrencyCode;
    }

    public void setSettleCurrencyCode(String settleCurrencyCode) {
        this.settleCurrencyCode = settleCurrencyCode;
    }

    public String getSettleCurrencyName() {
        return settleCurrencyName;
    }

    public void setSettleCurrencyName(String settleCurrencyName) {
        this.settleCurrencyName = settleCurrencyName;
    }

    public String getReciverCode() {
        return reciverCode;
    }

    public void setReciverCode(String reciverCode) {
        this.reciverCode = reciverCode;
    }

    public String getReciverName() {
        return reciverName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    public Date getOutletDate() {
        return outletDate;
    }

    public void setOutletDate(Date outletDate) {
        this.outletDate = outletDate;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getOrderNtaxAmount() {
        return orderNtaxAmount;
    }

    public void setOrderNtaxAmount(BigDecimal orderNtaxAmount) {
        this.orderNtaxAmount = orderNtaxAmount;
    }

    public BigDecimal getOrderHtaxAmount() {
        return orderHtaxAmount;
    }

    public void setOrderHtaxAmount(BigDecimal orderHtaxAmount) {
        this.orderHtaxAmount = orderHtaxAmount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
