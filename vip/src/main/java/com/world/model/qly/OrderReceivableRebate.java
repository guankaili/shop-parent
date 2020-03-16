package com.world.model.qly;

import com.world.data.mysql.Bean;

import java.math.BigDecimal;
import java.util.Date;

public class OrderReceivableRebate extends Bean {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 应收单号
     */
    private String receivableCode;

    /**
     * 返利编号
     */
    private String rebateCode;

    /**
     * 返利名称
     */
    private String rebateName;

    /**
     * 经销商编号
     */
    private String dealerCode;

    /**
     * 经销商名称
     */
    private String dealerName;

    /**
     * 返利总金额
     */
    private BigDecimal totalAmount;

    /**
     * 使用返利金额
     */
    private BigDecimal useAmount;

    /**
     * 返利剩余金额
     */
    private BigDecimal surplusAmount;

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

    public String getReceivableCode() {
        return receivableCode;
    }

    public void setReceivableCode(String receivableCode) {
        this.receivableCode = receivableCode;
    }

    public String getRebateCode() {
        return rebateCode;
    }

    public void setRebateCode(String rebateCode) {
        this.rebateCode = rebateCode;
    }

    public String getRebateName() {
        return rebateName;
    }

    public void setRebateName(String rebateName) {
        this.rebateName = rebateName;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(BigDecimal useAmount) {
        this.useAmount = useAmount;
    }

    public BigDecimal getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(BigDecimal surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
