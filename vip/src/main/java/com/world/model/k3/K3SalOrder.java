package com.world.model.k3;

import java.math.BigDecimal;
import java.util.Date;
import com.world.data.mysql.Bean;

/**
 * @author cuihuofei
 * @date 2019年12月20日 下午1:24:11
 * @version v1.0.0
 * @Description K3SalOrder.java Modification History: Date Author Version
 *              Description
 *              ---------------------------------------------------------------------------------*
 *              2019年12月20日 下午1:24:11 cuihuofei v1.0.0 Created
 */

public class K3SalOrder extends Bean {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 单据类型编号
	 * 提供类型标准订单：XSDD01_SYS；三包订单：XSDD001
	 */
	private String fbilltypeid;
	
	/**
	 * 单据类型名称
	 * 提供类型标准订单：XSDD01_SYS；三包订单：XSDD001
	 */
	private String fbilltypename;
	
	/**
	 * 三包类型编号
	 * 提供类型 麒麟保：QLB;三包：SOC
	 * 标准订单，麒麟保是没值的
	 */
	private String f_pjqd_shfs;
	
	/**
	 * 三包类型名称
	 * 提供类型 麒麟保：QLB;三包：SOC
	 * 标准订单，麒麟保是没值的
	 */
	private String f_pjqd_shfsname;
	
	/**
	 * 业务类型：默认：“普通销售”
	 */
	private String fbusinesstype;
	
	/**
	 * 下单日期
	 */
	private Date fdate;
	
	/**
	 * 销售组织：默认：1000
	 */
	private String fsaleorgid;
	
	/**
	 * 销售组织：默认：1000
	 */
	private String fsaleorgname;
	
	/**
	 * 客户编码
	 */
	private String fcustid;
	
	/**
	 * 客户名称
	 */
	private String fcustname;
	
	/**
	 * 结算币别编号
	 * 提供人民币默认类型 PRE001
	 */
	private String fsettlecurrid;
	
	/**
	 * 结算币别名称
	 * 默认人民币
	 */
	private String fsettlecurrname;
	
	/**
	 * 交货地点编号：收货地址编号。自提或快递可空。
	 */
	private String fheadlocid;
	
	/**
	 * 交货地点名称
	 */
	private String fheadlocname;
	
	/**
	 * 交货方式编号：JHFS03  快递 JHFS01_SYS 发货 JHFS02_SYS 自提
	 */
	private String fheaddeliveryway;
	
	/**
	 * 交货方式名称
	 */
	private String fheaddeliverywayname;
	
	/**
	 * QLY单号
	 */
	private String fqlyorderid;

	/**
	 * 订单状态
	 */
	private String forderstatus;

	/**
	 * 收货方（门店）暂时不需要传递
	 */
	private String freceiveid;

	/**
	 * 收货方联系人（门店）暂时不需要传递
	 */
	private String freccontactid;

	/**
	 * 收货方地址（门店）暂时不需要传递
	 */
	private String freceiveaddress;

	/**
	 * 销售部门 暂时不需要传递
	 */
	private String fsaledeptid;

	/**
	 * 销售组 暂时不需要传递
	 */
	private String fsalegroupid;

	/**
	 * 销售员
	 */
	private String fsalerid;

	/**
	 * 收款条件
	 */
	private String frecconditionid;
	
	/**
	 * K3订单编号
	 */
	private String fk3ordercode;
	
	/**
	 * 订单备注
	 */
	private String fdescript;

	/**
	 * 返利金额
	 */
	private BigDecimal f_td_rebate;

	/**
	 * 附加费用
	 */
	private BigDecimal f_td_extracharges;

	/**
	 * 备货状态
	 */
	private String fstockstatus;

	/**
	 * 出库状态
	 */
	private String foutstockstatus;

	/**
	 * 发票状态
	 */
	private String finvoicestatus;

	/**
	 * qlyrecstatus
	 */
	private String qlyrecstatus;

	/**
	 * qlyrectime
	 */
	private Date qlyrectime;

	/**
	 * qlyrevtime
	 */
	private Date qlyrevtime;

	/**
	 * qlymessage
	 */
	private String qlymessage;

	public String getFbilltypeid() {
		return fbilltypeid;
	}

	public void setFbilltypeid(String fbilltypeid) {
		this.fbilltypeid = fbilltypeid;
	}

	public String getFbilltypename() {
		return fbilltypename;
	}

	public void setFbilltypename(String fbilltypename) {
		this.fbilltypename = fbilltypename;
	}

	public String getF_pjqd_shfs() {
		return f_pjqd_shfs;
	}

	public void setF_pjqd_shfs(String f_pjqd_shfs) {
		this.f_pjqd_shfs = f_pjqd_shfs;
	}

	public String getF_pjqd_shfsname() {
		return f_pjqd_shfsname;
	}

	public void setF_pjqd_shfsname(String f_pjqd_shfsname) {
		this.f_pjqd_shfsname = f_pjqd_shfsname;
	}

	public String getFbusinesstype() {
		return fbusinesstype;
	}

	public void setFbusinesstype(String fbusinesstype) {
		this.fbusinesstype = fbusinesstype;
	}

	public Date getFdate() {
		return fdate;
	}

	public void setFdate(Date fdate) {
		this.fdate = fdate;
	}

	public String getFsaleorgid() {
		return fsaleorgid;
	}

	public void setFsaleorgid(String fsaleorgid) {
		this.fsaleorgid = fsaleorgid;
	}

	public String getFsaleorgname() {
		return fsaleorgname;
	}

	public void setFsaleorgname(String fsaleorgname) {
		this.fsaleorgname = fsaleorgname;
	}

	public String getFcustid() {
		return fcustid;
	}

	public void setFcustid(String fcustid) {
		this.fcustid = fcustid;
	}

	public String getFcustname() {
		return fcustname;
	}

	public void setFcustname(String fcustname) {
		this.fcustname = fcustname;
	}

	public String getFsettlecurrid() {
		return fsettlecurrid;
	}

	public void setFsettlecurrid(String fsettlecurrid) {
		this.fsettlecurrid = fsettlecurrid;
	}

	public String getFsettlecurrname() {
		return fsettlecurrname;
	}

	public void setFsettlecurrname(String fsettlecurrname) {
		this.fsettlecurrname = fsettlecurrname;
	}

	public String getFheadlocid() {
		return fheadlocid;
	}

	public void setFheadlocid(String fheadlocid) {
		this.fheadlocid = fheadlocid;
	}

	public String getFheadlocname() {
		return fheadlocname;
	}

	public void setFheadlocname(String fheadlocname) {
		this.fheadlocname = fheadlocname;
	}

	public String getFheaddeliveryway() {
		return fheaddeliveryway;
	}

	public void setFheaddeliveryway(String fheaddeliveryway) {
		this.fheaddeliveryway = fheaddeliveryway;
	}

	public String getFheaddeliverywayname() {
		return fheaddeliverywayname;
	}

	public void setFheaddeliverywayname(String fheaddeliverywayname) {
		this.fheaddeliverywayname = fheaddeliverywayname;
	}

	public String getFqlyorderid() {
		return fqlyorderid;
	}

	public void setFqlyorderid(String fqlyorderid) {
		this.fqlyorderid = fqlyorderid;
	}

	public String getForderstatus() {
		return forderstatus;
	}

	public void setForderstatus(String forderstatus) {
		this.forderstatus = forderstatus;
	}

	public String getFreceiveid() {
		return freceiveid;
	}

	public void setFreceiveid(String freceiveid) {
		this.freceiveid = freceiveid;
	}

	public String getFreccontactid() {
		return freccontactid;
	}

	public void setFreccontactid(String freccontactid) {
		this.freccontactid = freccontactid;
	}

	public String getFreceiveaddress() {
		return freceiveaddress;
	}

	public void setFreceiveaddress(String freceiveaddress) {
		this.freceiveaddress = freceiveaddress;
	}

	public String getFsaledeptid() {
		return fsaledeptid;
	}

	public void setFsaledeptid(String fsaledeptid) {
		this.fsaledeptid = fsaledeptid;
	}

	public String getFsalegroupid() {
		return fsalegroupid;
	}

	public void setFsalegroupid(String fsalegroupid) {
		this.fsalegroupid = fsalegroupid;
	}

	public String getFsalerid() {
		return fsalerid;
	}

	public void setFsalerid(String fsalerid) {
		this.fsalerid = fsalerid;
	}

	public String getFrecconditionid() {
		return frecconditionid;
	}

	public void setFrecconditionid(String frecconditionid) {
		this.frecconditionid = frecconditionid;
	}

	public BigDecimal getF_td_rebate() {
		return f_td_rebate;
	}

	public void setF_td_rebate(BigDecimal f_td_rebate) {
		this.f_td_rebate = f_td_rebate;
	}

	public BigDecimal getF_td_extracharges() {
		return f_td_extracharges;
	}

	public void setF_td_extracharges(BigDecimal f_td_extracharges) {
		this.f_td_extracharges = f_td_extracharges;
	}

	public String getFstockstatus() {
		return fstockstatus;
	}

	public void setFstockstatus(String fstockstatus) {
		this.fstockstatus = fstockstatus;
	}

	public String getFoutstockstatus() {
		return foutstockstatus;
	}

	public void setFoutstockstatus(String foutstockstatus) {
		this.foutstockstatus = foutstockstatus;
	}

	public String getFinvoicestatus() {
		return finvoicestatus;
	}

	public void setFinvoicestatus(String finvoicestatus) {
		this.finvoicestatus = finvoicestatus;
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

	public String getFk3ordercode() {
		return fk3ordercode;
	}

	public void setFk3ordercode(String fk3ordercode) {
		this.fk3ordercode = fk3ordercode;
	}

	public String getFdescript() {
		return fdescript;
	}

	public void setFdescript(String fdescript) {
		this.fdescript = fdescript;
	}
}
