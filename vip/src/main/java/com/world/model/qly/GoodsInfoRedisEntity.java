package com.world.model.qly;

import com.world.data.mysql.Bean;
import java.math.BigDecimal;


/**
 * @author gehaichao
 * @version v1.0.0
 * @date 2019/12/3$ 15:19$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2019/12/3$ 15:19$        gehaichao          v1.0.0           Created
 */
public class GoodsInfoRedisEntity extends Bean {


    private String size;
    private String spec;
    private String speed;
    private String deco;
    private String sto;
    private String priceCode;
    private String dataEnable;
    private String priceName;
    private String goodsShortName;



    private String createUserId;

    private String brandName;

    private String goodsCode;

    private String modifyUserId;

    private String createUserName;

    private String categoryCodeThr;


    private String categoryNameFir;

    private String baseUnit;

    private String retailUnit;

    private String modifyUserName;

    private String categoryCodeFir;

    private int enable;


    private String goodsName;

    private BigDecimal goodsBasePrice;

    private String marketPriceStr;


    private String id;

    private String specModel;

    private String descript;

    private String stockUnit;

    private String categoryCodeSec;


    private BigDecimal costPrice;

    private String brandCode;


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDeco() {
        return deco;
    }

    public void setDeco(String deco) {
        this.deco = deco;
    }

    public String getSto() {
        return sto;
    }

    public void setSto(String sto) {
        this.sto = sto;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public String getDataEnable() {
        return dataEnable;
    }

    public void setDataEnable(String dataEnable) {
        this.dataEnable = dataEnable;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public String getGoodsShortName() {
        return goodsShortName;
    }

    public void setGoodsShortName(String goodsShortName) {
        this.goodsShortName = goodsShortName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCategoryCodeThr() {
        return categoryCodeThr;
    }

    public void setCategoryCodeThr(String categoryCodeThr) {
        this.categoryCodeThr = categoryCodeThr;
    }

    public String getCategoryNameFir() {
        return categoryNameFir;
    }

    public void setCategoryNameFir(String categoryNameFir) {
        this.categoryNameFir = categoryNameFir;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public String getRetailUnit() {
        return retailUnit;
    }

    public void setRetailUnit(String retailUnit) {
        this.retailUnit = retailUnit;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }

    public String getCategoryCodeFir() {
        return categoryCodeFir;
    }

    public void setCategoryCodeFir(String categoryCodeFir) {
        this.categoryCodeFir = categoryCodeFir;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getGoodsBasePrice() {
        return goodsBasePrice;
    }

    public void setGoodsBasePrice(BigDecimal goodsBasePrice) {
        this.goodsBasePrice = goodsBasePrice;
    }

    public String getMarketPriceStr() {
        return marketPriceStr;
    }

    public void setMarketPriceStr(String marketPriceStr) {
        this.marketPriceStr = marketPriceStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecModel() {
        return specModel;
    }

    public void setSpecModel(String specModel) {
        this.specModel = specModel;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(String stockUnit) {
        this.stockUnit = stockUnit;
    }

    public String getCategoryCodeSec() {
        return categoryCodeSec;
    }

    public void setCategoryCodeSec(String categoryCodeSec) {
        this.categoryCodeSec = categoryCodeSec;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    @Override
    public String toString() {
        return "GoodsInfoRedisEntity{" +
                "size='" + size + '\'' +
                ", spec='" + spec + '\'' +
                ", speed='" + speed + '\'' +
                ", deco='" + deco + '\'' +
                ", sto='" + sto + '\'' +
                ", priceCode='" + priceCode + '\'' +
                ", dataEnable='" + dataEnable + '\'' +
                ", priceName='" + priceName + '\'' +
                ", goodsShortName='" + goodsShortName + '\'' +
                ", createUserId='" + createUserId + '\'' +
                ", brandName='" + brandName + '\'' +
                ", goodsCode='" + goodsCode + '\'' +
                ", modifyUserId='" + modifyUserId + '\'' +
                ", createUserName='" + createUserName + '\'' +
                ", categoryCodeThr='" + categoryCodeThr + '\'' +
                ", categoryNameFir='" + categoryNameFir + '\'' +
                ", baseUnit='" + baseUnit + '\'' +
                ", retailUnit='" + retailUnit + '\'' +
                ", modifyUserName='" + modifyUserName + '\'' +
                ", categoryCodeFir='" + categoryCodeFir + '\'' +
                ", enable=" + enable +
                ", goodsName='" + goodsName + '\'' +
                ", goodsBasePrice=" + goodsBasePrice +
                ", marketPriceStr='" + marketPriceStr + '\'' +
                ", id='" + id + '\'' +
                ", specModel='" + specModel + '\'' +
                ", descript='" + descript + '\'' +
                ", stockUnit='" + stockUnit + '\'' +
                ", categoryCodeSec='" + categoryCodeSec + '\'' +
                ", costPrice=" + costPrice +
                ", brandCode='" + brandCode + '\'' +
                '}';
    }
}