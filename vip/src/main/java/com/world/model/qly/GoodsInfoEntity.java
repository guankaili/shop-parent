package com.world.model.qly;

import com.world.data.mysql.Bean;

import java.io.Serializable;

/**
 * @author gehaichao
 * @version v1.0.0
 * @date 2019/12/15$ 10:42$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2019/12/15$ 10:42$        gehaichao          v1.0.0           Created
 */
public class GoodsInfoEntity extends Bean {

    private String goodsCode;
    private String goodsName;
    private String deco;
    private String speed;
    private String spec;
    private String size;
    private String brandName;
    private String baseUnit;
    private String weight;
    private String stockTypeCode;
    private String enable;
    private String checkDate;


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

    public String getDeco() {
        return deco;
    }

    public void setDeco(String deco) {
        this.deco = deco;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getStockTypeCode() {
        return stockTypeCode;
    }

    public void setStockTypeCode(String stockTypeCode) {
        this.stockTypeCode = stockTypeCode;
    }

    public String getEnable() {
        if("A".equals(enable.trim())){
            return "1";
        }
        return "0";
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }
}
