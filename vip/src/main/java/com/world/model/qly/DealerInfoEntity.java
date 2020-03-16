package com.world.model.qly;

import com.world.data.mysql.Bean;

import java.io.Serializable;

/**
 * @author gehaichao
 * @version v1.0.0
 * @date 2019/12/12$ 19:07$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2019/12/12$ 19:07$        gehaichao          v1.0.0           Created
 */
public class DealerInfoEntity extends Bean {
    private String dealerCode;
    private String dealerName;
    private String enable;


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

    public String getEnable() {
        if("A".equals(enable)){
            return "1";
        }
        return "0";
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }
}
