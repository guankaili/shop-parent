package com.world.model.qly;

import com.world.data.mysql.Bean;

import java.math.BigDecimal;

/**
 * @author gehaichao
 * @version v1.0.0
 * @date 2019/12/21$ 8:57$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2019/12/21$ 8:57$        gehaichao          v1.0.0           Created
 */
public class StockEntity extends Bean {

    private String goodsCode;
    private String goodsName;
    private BigDecimal sto;
    private String storage;


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

    public BigDecimal getSto() {
        return sto;
    }

    public void setSto(BigDecimal sto) {
        this.sto = sto;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }
}
