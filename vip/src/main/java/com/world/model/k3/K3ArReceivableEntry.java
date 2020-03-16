package com.world.model.k3;

import com.world.data.mysql.Bean;

import java.math.BigDecimal;

public class K3ArReceivableEntry extends Bean {
    private static final long serialVersionUID = 1L;

    //应收单编号
    private String FBILLNO;
    //K3订单号
    private String F_PJQD_XSDDH1;
    //K3出库单号
    private String F_PJQD_XSCKDH;
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
    //折扣分摊
    private BigDecimal F_TD_ROWDISCOUNT;
    //返利分摊
    private BigDecimal F_TD_RowRebate;
    //附加费用分摊
    private BigDecimal F_TD_RowExtraCharges;
    //原单单价
    private BigDecimal F_TD_SimplePrice;
    //原单金额
    private BigDecimal F_TD_SimpleAMOUNT;

    public BigDecimal getF_TD_ROWDISCOUNT() {
        return F_TD_ROWDISCOUNT;
    }

    public void setF_TD_ROWDISCOUNT(BigDecimal f_TD_ROWDISCOUNT) {
        F_TD_ROWDISCOUNT = f_TD_ROWDISCOUNT;
    }

    public BigDecimal getF_TD_RowRebate() {
        return F_TD_RowRebate;
    }

    public void setF_TD_RowRebate(BigDecimal f_TD_RowRebate) {
        F_TD_RowRebate = f_TD_RowRebate;
    }

    public BigDecimal getF_TD_RowExtraCharges() {
        return F_TD_RowExtraCharges;
    }

    public void setF_TD_RowExtraCharges(BigDecimal f_TD_RowExtraCharges) {
        F_TD_RowExtraCharges = f_TD_RowExtraCharges;
    }

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

    public String getF_PJQD_XSCKDH() {
        return F_PJQD_XSCKDH;
    }

    public void setF_PJQD_XSCKDH(String f_PJQD_XSCKDH) {
        F_PJQD_XSCKDH = f_PJQD_XSCKDH;
    }

    public BigDecimal getF_TD_SimplePrice() {
        return F_TD_SimplePrice;
    }

    public void setF_TD_SimplePrice(BigDecimal f_TD_SimplePrice) {
        F_TD_SimplePrice = f_TD_SimplePrice;
    }

    public BigDecimal getF_TD_SimpleAMOUNT() {
        return F_TD_SimpleAMOUNT;
    }

    public void setF_TD_SimpleAMOUNT(BigDecimal f_TD_SimpleAMOUNT) {
        F_TD_SimpleAMOUNT = f_TD_SimpleAMOUNT;
    }
}
