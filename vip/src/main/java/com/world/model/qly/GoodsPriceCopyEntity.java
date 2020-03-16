package com.world.model.qly;

import com.world.data.mysql.Bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author gehaichao
 * @version v1.0.0
 * @date 2019/12/20$ 19:46$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2019/12/20$ 19:46$        gehaichao          v1.0.0           Created
 */
public class GoodsPriceCopyEntity implements Serializable {
    private String priceCode;
    private String priceName;
    private String goodsCode;
    private String goodsName;
    private String currency;
    private BigDecimal price;
    private Date startTime;
    private Date endTime;

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        return "GoodsPriceCopyEntity{" +
                "priceCode='" + priceCode + '\'' +
                ", priceName='" + priceName + '\'' +
                ", goodsCode='" + goodsCode + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", currency='" + currency + '\'' +
                ", price=" + price +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
