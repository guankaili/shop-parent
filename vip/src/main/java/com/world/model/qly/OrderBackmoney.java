package com.world.model.qly;

import com.world.data.mysql.Bean;

import java.math.BigDecimal;
import java.util.Date;

public class OrderBackmoney extends Bean {
    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    private Long id;

    /**
     * 回款单编号
     */
    private String backmoneyCode;

    /**
     * 单据类型编号
     */
    private String backmoneyTypeCode;

    /**
     * 单据类型名称
     */
    private String backmoneyTypeName;

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
     * 结算方式编号
     */
    private String settleTypeCode;

    /**
     * 结算方式名称
     */
    private String settleTypeName;

    /**
     * 回款用途编号
     */
    private String purposeCode;

    /**
     * 回款用途名称
     */
    private String purposeName;

    /**
     * 回款金额
     */
    private BigDecimal amount;

    /**
     * 出库日期
     */
    private Date backmoneyDate;

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

    public String getBackmoneyCode() {
        return backmoneyCode;
    }

    public void setBackmoneyCode(String backmoneyCode) {
        this.backmoneyCode = backmoneyCode;
    }

    public String getBackmoneyTypeCode() {
        return backmoneyTypeCode;
    }

    public void setBackmoneyTypeCode(String backmoneyTypeCode) {
        this.backmoneyTypeCode = backmoneyTypeCode;
    }

    public String getBackmoneyTypeName() {
        return backmoneyTypeName;
    }

    public void setBackmoneyTypeName(String backmoneyTypeName) {
        this.backmoneyTypeName = backmoneyTypeName;
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

    public String getSettleTypeCode() {
        return settleTypeCode;
    }

    public void setSettleTypeCode(String settleTypeCode) {
        this.settleTypeCode = settleTypeCode;
    }

    public String getSettleTypeName() {
        return settleTypeName;
    }

    public void setSettleTypeName(String settleTypeName) {
        this.settleTypeName = settleTypeName;
    }

    public String getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getPurposeName() {
        return purposeName;
    }

    public void setPurposeName(String purposeName) {
        this.purposeName = purposeName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getBackmoneyDate() {
        return backmoneyDate;
    }

    public void setBackmoneyDate(Date backmoneyDate) {
        this.backmoneyDate = backmoneyDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
