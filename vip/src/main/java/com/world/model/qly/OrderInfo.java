package com.world.model.qly;

import java.math.BigDecimal;
import java.util.Date;

import com.world.data.mysql.Bean;

/**
	* @author cuihuofei
	* @date   2019年12月24日 下午3:28:24
	* @version v1.0.0
	* @Description	麒麟云订单主表
	* OrderInfo.java 
	* Modification History:
	* Date                         Author          Version          Description
	---------------------------------------------------------------------------------*
	* 2019年12月24日 下午3:28:24       cuihuofei        v1.0.0           Created
 */
public class OrderInfo extends Bean {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id 必须为包装类型 , 不管是自增长或是使用雪花算法 , 因为基础数据类型有默认值 , ORM 框架仅当 id 值为 null 时才会自动生成主键
	 */
	private Long id;

	/**
	 * 用于关联 K3 唯一标识 下单完成需要往 K3 下单 , K3 的结构会反馈一个 code 需要做关联记录
	 */
	private String k3_code;

	/**
	 * 订单总金额 不能默认值 , 必须大于 0 该价格为不计算返利 , 不计算优惠的价格
	 */
	private BigDecimal order_amount;

	/**
	 * 支付金额 该订单实际支付的金额 未支付之前默认为 0.00 在 K3 确认订单之后 , 会传递过来一个 实际付款金额
	 */
	private BigDecimal pay_amount;

	/**
	 * 返利 默认为 0.00 现阶段在本系统不计算可控制返利 , 值为 K3 反馈过来的 , 订单模块只需要记录
	 */
	private BigDecimal rebate;

	/**
	 * 订单总数量 所有的单条记录的购买数量的总和
	 */
	private Integer order_num;
	
	/**
	 * 订单确认数量
	 */
	private Integer confirm_num;

	/**
	 * 签收数量 如果存在多次发货 , 需要一直使用到该字段 , 在经销商确认收货的时候需要更新本字段 未有签收操作之前默认为 0
	 */
	private Integer sign_num;
	
	/**
	 * 订单结清标记
	 */
	private int order_num_settle;

	/**
	 * 线下付款业务流程使用 按订单数量付款 or 按实际确认数量付款
	 */
	private int under_pay_num_type;

	/**
	 * 支付方式
	 */
	private int pay_type;

	/**
	 * 配送方式 1公司配送默认，配送方式，现仅有公司配送一种选项，但具体需要参照实际枚举
	 */
	private String delivery_type;
	
	/**
	 * 订单类型 默认为: B2B订单
	 */
	private String order_type;

	/**
	 * 单据类型 提供类型标准订单：XSDD01_SYS；三包订单：XSDD001
	 */
	private String bill_type;

	/**
	 * 三包类型 提供类型 麒麟保：QLB;三包：SOC
	 */
	private String three_packs;

	/**
	 * 业务类型 默认为: 经销商普通订单
	 */
	private String busi_type;
	
	/**
	 * 币别
	 */
	private String settle_currency;

	/**
	 * 订单状态
	 */
	private int order_status;

	/**
	 * 备注
	 */
	private String descript;

	/**
	 * 期望到货时间 允许为空
	 */
	private Date order_expect_date;

	/**
	 * 发票状态
	 *
	 */
	private int invoice_status;

	/**
	 * 供货方编号 默认值: 1000
	 */
	private String supplier_code;

	/**
	 * 供货方名称 默认值: 青岛森麒麟轮胎股份有限公司
	 */
	private String supplier_name;

	/**
	 * 经销商编号
	 */
	private String dealer_code;

	/**
	 * 经销商名称
	 */
	private String dealer_name;

	/**
	 * 收货地址编号
	 */
	private String reciver_code;

	/**
	 * 收货地址
	 */
	private String reciver_address;
	
	/**
	 * 创建人ID
	 */
	private String create_user_id;
	
	/**
	 * 创建人姓名
	 */
	private String create_user_name;
	
	/**
	 * 创建时间
	 */
	private Date create_date;

	/**
	 * 修改时间
	 */
	private Date modify_date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getK3_code() {
		return k3_code;
	}

	public void setK3_code(String k3_code) {
		this.k3_code = k3_code;
	}

	public BigDecimal getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(BigDecimal order_amount) {
		this.order_amount = order_amount;
	}

	public BigDecimal getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(BigDecimal pay_amount) {
		this.pay_amount = pay_amount;
	}

	public BigDecimal getRebate() {
		return rebate;
	}

	public void setRebate(BigDecimal rebate) {
		this.rebate = rebate;
	}

	public Integer getOrder_num() {
		return order_num;
	}

	public void setOrder_num(Integer order_num) {
		this.order_num = order_num;
	}

	public Integer getConfirm_num() {
		return confirm_num;
	}

	public void setConfirm_num(Integer confirm_num) {
		this.confirm_num = confirm_num;
	}

	public Integer getSign_num() {
		return sign_num;
	}

	public void setSign_num(Integer sign_num) {
		this.sign_num = sign_num;
	}

	public int getOrder_num_settle() {
		return order_num_settle;
	}

	public void setOrder_num_settle(int order_num_settle) {
		this.order_num_settle = order_num_settle;
	}

	public int getUnder_pay_num_type() {
		return under_pay_num_type;
	}

	public void setUnder_pay_num_type(int under_pay_num_type) {
		this.under_pay_num_type = under_pay_num_type;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public String getDelivery_type() {
		return delivery_type;
	}

	public void setDelivery_type(String delivery_type) {
		this.delivery_type = delivery_type;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public String getBill_type() {
		return bill_type;
	}

	public void setBill_type(String bill_type) {
		this.bill_type = bill_type;
	}

	public String getThree_packs() {
		return three_packs;
	}

	public void setThree_packs(String three_packs) {
		this.three_packs = three_packs;
	}

	public String getBusi_type() {
		return busi_type;
	}

	public void setBusi_type(String busi_type) {
		this.busi_type = busi_type;
	}

	public String getSettle_currency() {
		return settle_currency;
	}

	public void setSettle_currency(String settle_currency) {
		this.settle_currency = settle_currency;
	}

	public int getOrder_status() {
		return order_status;
	}

	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Date getOrder_expect_date() {
		return order_expect_date;
	}

	public void setOrder_expect_date(Date order_expect_date) {
		this.order_expect_date = order_expect_date;
	}

	public int getInvoice_status() {
		return invoice_status;
	}

	public void setInvoice_status(int invoice_status) {
		this.invoice_status = invoice_status;
	}

	public String getSupplier_code() {
		return supplier_code;
	}

	public void setSupplier_code(String supplier_code) {
		this.supplier_code = supplier_code;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getDealer_code() {
		return dealer_code;
	}

	public void setDealer_code(String dealer_code) {
		this.dealer_code = dealer_code;
	}

	public String getDealer_name() {
		return dealer_name;
	}

	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}

	public String getReciver_code() {
		return reciver_code;
	}

	public void setReciver_code(String reciver_code) {
		this.reciver_code = reciver_code;
	}

	public String getReciver_address() {
		return reciver_address;
	}

	public void setReciver_address(String reciver_address) {
		this.reciver_address = reciver_address;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getModify_date() {
		return modify_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}

	public String getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(String create_user_id) {
		this.create_user_id = create_user_id;
	}

	public String getCreate_user_name() {
		return create_user_name;
	}

	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}
}
