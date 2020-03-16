package com.world.model.k3;

import com.world.data.mysql.Bean;

import java.math.BigDecimal;

public class K3SalOutStockEntry extends Bean {
    private static final long serialVersionUID = 1L;

    //出库单编号
    private String FBILLNO;
    //K3订单号
    private String F_PJQD_XSDDH1;
    //物料编码
    private String FMATERIALID;
    //物料名称
    private String FMATERIALNAME;
    //数量
    private int FQTY;
    //未税单价
    private BigDecimal Price;
    //含税单价
    private BigDecimal FTAXPRICE;
    //税率
    private BigDecimal FTAXRATE;
    //未税金额
    private BigDecimal FAMOUNT;
    //价税合计
    private BigDecimal FALLAMOUNT;

    public String getFBILLNO() {
        return FBILLNO;
    }

    public void setFBILLNO(String FBILLNO) {
        this.FBILLNO = FBILLNO;
    }

    public String getF_PJQD_XSDDH1() {
        return F_PJQD_XSDDH1;
    }

    public void setF_PJQD_XSDDH1(String f_PJQD_XSDDH1) {
        F_PJQD_XSDDH1 = f_PJQD_XSDDH1;
    }

    public String getFMATERIALID() {
        return FMATERIALID;
    }

    public void setFMATERIALID(String FMATERIALID) {
        this.FMATERIALID = FMATERIALID;
    }

    public String getFMATERIALNAME() {
        return FMATERIALNAME;
    }

    public void setFMATERIALNAME(String FMATERIALNAME) {
        this.FMATERIALNAME = FMATERIALNAME;
    }

    public int getFQTY() {
        return FQTY;
    }

    public void setFQTY(int FQTY) {
        this.FQTY = FQTY;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(BigDecimal price) {
        Price = price;
    }

    public BigDecimal getFTAXPRICE() {
        return FTAXPRICE;
    }

    public void setFTAXPRICE(BigDecimal FTAXPRICE) {
        this.FTAXPRICE = FTAXPRICE;
    }

    public BigDecimal getFTAXRATE() {
        return FTAXRATE;
    }

    public void setFTAXRATE(BigDecimal FTAXRATE) {
        this.FTAXRATE = FTAXRATE;
    }

    public BigDecimal getFAMOUNT() {
        return FAMOUNT;
    }

    public void setFAMOUNT(BigDecimal FAMOUNT) {
        this.FAMOUNT = FAMOUNT;
    }

    public BigDecimal getFALLAMOUNT() {
        return FALLAMOUNT;
    }

    public void setFALLAMOUNT(BigDecimal FALLAMOUNT) {
        this.FALLAMOUNT = FALLAMOUNT;
    }
}
