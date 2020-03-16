package com.world.model.qly;

import com.world.data.mysql.Bean;

import java.io.Serializable;

/**
 * @author gehaichao
 * @version v1.0.0
 * @date 2019/12/15$ 9:27$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2019/12/15$ 9:27$        gehaichao          v1.0.0           Created
 */
public class GoodsBrandEntity extends Bean {
    private String brandCode;
    private String brandName;
    private String nameShort;
    private String descript;
    private String enable;
    private String dataEnable;

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getEnable() {
        if("A".equals(enable)){
            return "1";
        }
        return "0";
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getDataEnable() {
        if("C".equals(dataEnable)){
            return "1";
        }
        return "0";
    }

    public void setDataEnable(String dataEnable) {
        this.dataEnable = dataEnable;
    }
}
