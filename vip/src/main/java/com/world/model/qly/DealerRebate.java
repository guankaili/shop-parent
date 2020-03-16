package com.world.model.qly;

import com.world.data.mysql.Bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* 
* @author gkl
* @date 2019-12-20 08:21:26
**/
public class DealerRebate extends Bean implements Serializable {

	private static final long serialVersionUID = 1L;


    private Long id;
    /**
     * 经销商返利编号
     */
    private String dealerRebateCode;
    /**
     * 经销商返利名称
     */
    private String dealerRebateName;
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
     * 返利已用金额
     */
    private BigDecimal useAmount;
    /**
     * 余额-剩余金额
     */
    private BigDecimal surplusAmount;
    /**
     * 数据状态
     */
    private Integer dataEnable;
    /**
     * 禁用状态
     */
    private int enable;
    /**
     * 创建时间
     */
    private Date createDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealerRebateCode() {
        return dealerRebateCode;
    }

    public void setDealerRebateCode(String dealerRebateCode) {
        this.dealerRebateCode = dealerRebateCode;
    }

    public String getDealerRebateName() {
        return dealerRebateName;
    }

    public void setDealerRebateName(String dealerRebateName) {
        this.dealerRebateName = dealerRebateName;
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

    public Integer getDataEnable() {
        return dataEnable;
    }

    public void setDataEnable(Integer dataEnable) {
        this.dataEnable = dataEnable;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}