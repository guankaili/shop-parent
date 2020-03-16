package com.world.model.qly;


import com.world.data.mysql.Bean;

import java.math.BigDecimal;
import java.util.Date;

public class OrderOutletGoods extends Bean {
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
     * 商品编号
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 商品不含税单价
     */
    private BigDecimal goodsNtaxPrice;

    /**
     * 商品含税单价
     */
    private BigDecimal goodsHtaxPrice;

    /**
     * 税率
     */
    private BigDecimal tax;

    /**
     * 未税金额
     */
    private BigDecimal goodsNtaxAmount;

    /**
     * 价税合计
     */
    private BigDecimal goodsHtaxAmount;

    /**
     * 订单商品金额
     */
    private BigDecimal goodsAmount;

    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 出库日期
     */
    private Date outletDate;

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

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public BigDecimal getGoodsNtaxPrice() {
        return goodsNtaxPrice;
    }

    public void setGoodsNtaxPrice(BigDecimal goodsNtaxPrice) {
        this.goodsNtaxPrice = goodsNtaxPrice;
    }

    public BigDecimal getGoodsHtaxPrice() {
        return goodsHtaxPrice;
    }

    public void setGoodsHtaxPrice(BigDecimal goodsHtaxPrice) {
        this.goodsHtaxPrice = goodsHtaxPrice;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getGoodsNtaxAmount() {
        return goodsNtaxAmount;
    }

    public void setGoodsNtaxAmount(BigDecimal goodsNtaxAmount) {
        this.goodsNtaxAmount = goodsNtaxAmount;
    }

    public BigDecimal getGoodsHtaxAmount() {
        return goodsHtaxAmount;
    }

    public void setGoodsHtaxAmount(BigDecimal goodsHtaxAmount) {
        this.goodsHtaxAmount = goodsHtaxAmount;
    }

    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Date getOutletDate() {
        return outletDate;
    }

    public void setOutletDate(Date outletDate) {
        this.outletDate = outletDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
