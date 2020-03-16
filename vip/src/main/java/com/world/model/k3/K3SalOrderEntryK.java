package com.world.model.k3;

import java.math.BigDecimal;
import java.util.Date;

import com.world.data.mysql.Bean;

/**
	* @author cuihuofei
	* @date   2019年12月24日 下午4:09:17
	* @version v1.0.0
	* @Description	K3回传的订单商品详情
	* K3SalOrderEntryK.java 
	* Modification History:
	* Date                         Author          Version          Description
	---------------------------------------------------------------------------------*
	* 2019年12月24日 下午4:09:17       cuihuofei        v1.0.0           Created
*/
public class K3SalOrderEntryK extends Bean {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 订单详情商品ID
	 */
	private String fqlyordergoodsid;
	
	/**
	 * 物料编码
	 */
	private String fmaterialid;
	
	/**
	 * 物料名称
	 */
	private String fmaterialname;
	
	/**
	 * 价目表（编号）
	 */
	private String fpricelistid;
	
	/**
	 * 价目表名称
	 */
	private String fpricelistname;
	
	/**
	 * 销售基本数量
	 */
	private int fqty;
	
	/**
	 * 含税单价
	 */
	private BigDecimal ftdsimpleprice;
	
	/**
	 * 含税单价
	 */
	private BigDecimal ftaxprice;
	
	/**
	 * 要货日期
	 */
	private Date fdeliverydate;
	
	/**
	 * QLY订单ID
	 */
	private String fqlyorderid;
	
	/**
	 * 传输方向：乾行新增或修改，麒麟云接收处理。
	 * 双方处理状态：
	 * 1代表乾行传入更新或新增，从2改成1或者直接插入时保存为1。
	 * 2代表麒麟云接收处理完成，将1改成2。
	 * 9代表麒麟云处理失败。
	 */
	private String qlyrecstatus;
	
	/**
	 * 乾行传入时间
	 */
	private Date qlyrectime;
	
	/**
	 * 麒麟云处理时间
	 */
	private Date qlyrevtime;
	
	/**
	 * 麒麟云处理备注
	 */
	private String qlymessage;

	public String getFqlyordergoodsid() {
		return fqlyordergoodsid;
	}

	public void setFqlyordergoodsid(String fqlyordergoodsid) {
		this.fqlyordergoodsid = fqlyordergoodsid;
	}

	public String getFmaterialid() {
		return fmaterialid;
	}

	public void setFmaterialid(String fmaterialid) {
		this.fmaterialid = fmaterialid;
	}

	public String getFpricelistid() {
		return fpricelistid;
	}

	public void setFpricelistid(String fpricelistid) {
		this.fpricelistid = fpricelistid;
	}

	public int getFqty() {
		return fqty;
	}

	public void setFqty(int fqty) {
		this.fqty = fqty;
	}

	public BigDecimal getFtdsimpleprice() {
		return ftdsimpleprice;
	}

	public void setFtdsimpleprice(BigDecimal ftdsimpleprice) {
		this.ftdsimpleprice = ftdsimpleprice;
	}

	public BigDecimal getFtaxprice() {
		return ftaxprice;
	}

	public void setFtaxprice(BigDecimal ftaxprice) {
		this.ftaxprice = ftaxprice;
	}

	public Date getFdeliverydate() {
		return fdeliverydate;
	}

	public void setFdeliverydate(Date fdeliverydate) {
		this.fdeliverydate = fdeliverydate;
	}

	public String getFqlyorderid() {
		return fqlyorderid;
	}

	public void setFqlyorderid(String fqlyorderid) {
		this.fqlyorderid = fqlyorderid;
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

	public String getFmaterialname() {
		return fmaterialname;
	}

	public void setFmaterialname(String fmaterialname) {
		this.fmaterialname = fmaterialname;
	}

	public String getFpricelistname() {
		return fpricelistname;
	}

	public void setFpricelistname(String fpricelistname) {
		this.fpricelistname = fpricelistname;
	}
}
