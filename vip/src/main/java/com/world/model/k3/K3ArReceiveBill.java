package com.world.model.k3;

import com.world.data.mysql.Bean;
import java.math.BigDecimal;
import java.util.Date;

public class K3ArReceiveBill extends Bean {
    private static final long serialVersionUID = 1L;

    //收款单(回款单)编号
    private String FBILLNO;
    //单据类型ID
    private String FBILLTYPEID;
    //单据类型名称
    private String FBILLTYPENAME;
    //日期
    private Date FDATE;
    //客户编号
    private String FCONTACTUNIT;
    //客户名称
    private String FCONTACTUNITNAME;
    //结算币别ID
    private String FCURRENCYID;
    //结算币别名称
    private String FCURRENCYIDNAME;
    //结算方式编号
    private String FSETTLETYPEID;
    //结算方式名称
    private String FSETTLETYPENAME;
    //收款用途
    private String FPURPOSEID;
    //收款用途名称
    private String FPURPOSENAME;
    //收款金额
    private BigDecimal FREALRECAMOUNTFOR;

    public String getFBILLNO() {
        return FBILLNO;
    }

    public void setFBILLNO(String FBILLNO) {
        this.FBILLNO = FBILLNO;
    }

    public String getFBILLTYPEID() {
        return FBILLTYPEID;
    }

    public void setFBILLTYPEID(String FBILLTYPEID) {
        this.FBILLTYPEID = FBILLTYPEID;
    }

    public String getFBILLTYPENAME() {
        return FBILLTYPENAME;
    }

    public void setFBILLTYPENAME(String FBILLTYPENAME) {
        this.FBILLTYPENAME = FBILLTYPENAME;
    }

    public Date getFDATE() {
        return FDATE;
    }

    public void setFDATE(Date FDATE) {
        this.FDATE = FDATE;
    }

    public String getFCONTACTUNIT() {
        return FCONTACTUNIT;
    }

    public void setFCONTACTUNIT(String FCONTACTUNIT) {
        this.FCONTACTUNIT = FCONTACTUNIT;
    }

    public String getFCONTACTUNITNAME() {
        return FCONTACTUNITNAME;
    }

    public void setFCONTACTUNITNAME(String FCONTACTUNITNAME) {
        this.FCONTACTUNITNAME = FCONTACTUNITNAME;
    }

    public String getFCURRENCYID() {
        return FCURRENCYID;
    }

    public void setFCURRENCYID(String FCURRENCYID) {
        this.FCURRENCYID = FCURRENCYID;
    }

    public String getFCURRENCYIDNAME() {
        return FCURRENCYIDNAME;
    }

    public void setFCURRENCYIDNAME(String FCURRENCYIDNAME) {
        this.FCURRENCYIDNAME = FCURRENCYIDNAME;
    }

    public String getFSETTLETYPEID() {
        return FSETTLETYPEID;
    }

    public void setFSETTLETYPEID(String FSETTLETYPEID) {
        this.FSETTLETYPEID = FSETTLETYPEID;
    }

    public String getFSETTLETYPENAME() {
        return FSETTLETYPENAME;
    }

    public void setFSETTLETYPENAME(String FSETTLETYPENAME) {
        this.FSETTLETYPENAME = FSETTLETYPENAME;
    }

    public String getFPURPOSEID() {
        return FPURPOSEID;
    }

    public void setFPURPOSEID(String FPURPOSEID) {
        this.FPURPOSEID = FPURPOSEID;
    }

    public String getFPURPOSENAME() {
        return FPURPOSENAME;
    }

    public void setFPURPOSENAME(String FPURPOSENAME) {
        this.FPURPOSENAME = FPURPOSENAME;
    }

    public BigDecimal getFREALRECAMOUNTFOR() {
        return FREALRECAMOUNTFOR;
    }

    public void setFREALRECAMOUNTFOR(BigDecimal FREALRECAMOUNTFOR) {
        this.FREALRECAMOUNTFOR = FREALRECAMOUNTFOR;
    }
}
