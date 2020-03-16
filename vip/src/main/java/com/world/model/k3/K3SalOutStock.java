package com.world.model.k3;

import com.world.data.mysql.Bean;

import java.util.Date;

public class K3SalOutStock extends Bean {
    private static final long serialVersionUID = 1L;

    //出库单编号
    private String FBILLNO;
    //单据类型ID
    private String FBILLTYPEID;
    //单据类型名称
    private String FBILLTYPENAME;
    //出库单日期
    private Date FDATE;
    //客户编号
    private String FCUSTID;
    //客户名称
    private String FCUSTNAME;
    //结算币别ID
    private String FSETTLECURRID;
    //结算币别名称
    private String FSETTLECURRNAME;
    //交货地点编号
    private String FHEADLOCID;
    //交货地点名称
    private String FHEADLOCNAME;
    //备注
    private String fnote;
    /**
     * 传输方向：乾行新增或修改，麒麟云接收处理。
     * 双方处理状态：
     * 1代表乾行传入更新或新增，从2改成1或者直接插入时保存为1。
     * 2代表麒麟云接收处理完成，将1改成2。
     * 9代表麒麟云处理失败。
     */
    private String qlyrecstatus;
    //乾行传入时间
    private Date qlyrectime;
    //麒麟云处理时间
    private Date qlyrevtime;
    //麒麟云处理备注
    private String qlymessage;

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

    public String getFCUSTID() {
        return FCUSTID;
    }

    public void setFCUSTID(String FCUSTID) {
        this.FCUSTID = FCUSTID;
    }

    public String getFCUSTNAME() {
        return FCUSTNAME;
    }

    public void setFCUSTNAME(String FCUSTNAME) {
        this.FCUSTNAME = FCUSTNAME;
    }

    public String getFSETTLECURRID() {
        return FSETTLECURRID;
    }

    public void setFSETTLECURRID(String FSETTLECURRID) {
        this.FSETTLECURRID = FSETTLECURRID;
    }

    public String getFSETTLECURRNAME() {
        return FSETTLECURRNAME;
    }

    public void setFSETTLECURRNAME(String FSETTLECURRNAME) {
        this.FSETTLECURRNAME = FSETTLECURRNAME;
    }

    public String getFHEADLOCID() {
        return FHEADLOCID;
    }

    public void setFHEADLOCID(String FHEADLOCID) {
        this.FHEADLOCID = FHEADLOCID;
    }
    public String getFHEADLOCNAME() {
        return FHEADLOCNAME;
    }

    public void setFHEADLOCNAME(String FHEADLOCNAME) {
        this.FHEADLOCNAME = FHEADLOCNAME;
    }

    public String getFnote() {
        return fnote;
    }

    public void setFnote(String fnote) {
        this.fnote = fnote;
    }

    public String getQlyrecstatus() {
        return qlyrecstatus;
    }

    public void setQlyrecstatus(String qlyrecstatus) {
        this.qlyrecstatus = qlyrecstatus;
    }

    public Date getQlyrectime() {
        return qlyrectime;
    }

    public void setQlyrectime(Date qlyrectime) {
        this.qlyrectime = qlyrectime;
    }

    public Date getQlyrevtime() {
        return qlyrevtime;
    }

    public void setQlyrevtime(Date qlyrevtime) {
        this.qlyrevtime = qlyrevtime;
    }

    public String getQlymessage() {
        return qlymessage;
    }

    public void setQlymessage(String qlymessage) {
        this.qlymessage = qlymessage;
    }
}
