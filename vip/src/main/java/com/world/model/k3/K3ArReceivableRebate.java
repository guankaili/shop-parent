package com.world.model.k3;

import com.world.data.mysql.Bean;

import java.math.BigDecimal;

public class K3ArReceivableRebate extends Bean {
    private static final long serialVersionUID = 1L;

    //应收单编号
    private  String FBILLNO;
    //返利单编码
    private  String F_TD_Rebateid;
    //返利单名称
    private  String F_TD_RebateidNAME;
    //返利余额
    private BigDecimal F_TD_ThisBalance;
    //使用返利金额（此商品使用的返利金额）
    private BigDecimal F_TD_UseAmt;

    public String getFBILLNO() {
        return FBILLNO;
    }

    public void setFBILLNO(String FBILLNO) {
        this.FBILLNO = FBILLNO;
    }

    public String getF_TD_Rebateid() {
        return F_TD_Rebateid;
    }

    public void setF_TD_Rebateid(String f_TD_Rebateid) {
        F_TD_Rebateid = f_TD_Rebateid;
    }

    public String getF_TD_RebateidNAME() {
        return F_TD_RebateidNAME;
    }

    public void setF_TD_RebateidNAME(String f_TD_RebateidNAME) {
        F_TD_RebateidNAME = f_TD_RebateidNAME;
    }

    public BigDecimal getF_TD_ThisBalance() {
        return F_TD_ThisBalance;
    }

    public void setF_TD_ThisBalance(BigDecimal f_TD_ThisBalance) {
        F_TD_ThisBalance = f_TD_ThisBalance;
    }

    public BigDecimal getF_TD_UseAmt() {
        return F_TD_UseAmt;
    }

    public void setF_TD_UseAmt(BigDecimal f_TD_UseAmt) {
        F_TD_UseAmt = f_TD_UseAmt;
    }
}
