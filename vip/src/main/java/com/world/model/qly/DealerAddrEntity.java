package com.world.model.qly;

import com.world.data.mysql.Bean;


/**
 * @author gehaichao
 * @version v1.0.0
 * @date 2019/12/16$ 13:37$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2019/12/16$ 13:37$        gehaichao          v1.0.0           Created
 */
public class DealerAddrEntity extends Bean {
   private String dealerCode;
   private String addrCode;
   private String address;
   private String enable;
   private String dataEnable;

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getAddrCode() {
        return addrCode;
    }

    public void setAddrCode(String addrCode) {
        this.addrCode = addrCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
