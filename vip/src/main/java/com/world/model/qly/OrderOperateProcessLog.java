package com.world.model.qly;

import java.util.Date;

import com.world.data.mysql.Bean;

/**
	* @author cuihuofei
	* @date   2019年12月28日 下午4:15:21
	* @version v1.0.0
	* @Description
	* OrderOperateProcessLog.java 
	* Modification History:
	* Date                         Author          Version          Description
	---------------------------------------------------------------------------------*
	* 2019年12月28日 下午4:15:21       cuihuofei        v1.0.0           Created
 */
public class OrderOperateProcessLog extends Bean {
	
	private static final long serialVersionUID = 1L;

	/**
     * 主键id
     * 必须为包装类型 , 不管是自增长或是使用雪花算法 , 因为基础数据类型有默认值 , ORM 框架仅当 id 值为 null 时才会自动生成主键
     */
    private Long id;

    /**
    * 订单编号
    */
    private Long order_id;

    /**
    * 订单当前状态
    */
    private String order_status;

    /**
    * 创建
    */
    private String create_userId;

    /**
    * 创建
    */
    private String create_user_name;

    /**
    * 创建
    */
    private Date create_date_time;

    /**
    * 提交
    */
    private String submit_user_id;

    /**
    * 提交
    */
    private String submit_user_name;

    /**
    * 提交
    */
    private Date submit_datetime;

    /**
    * 审核
    */
    private String verify_userId;

    /**
    * 审核
    */
    private String verify_user_name;

    /**
    * 审核
    */
    private Date verify_datetime;

    /**
    * 支付
    */
    private String pay_user_id;

    /**
    * 支付
    */
    private String pay_user_name;

    /**
    * 支付
    */
    private Date pay_datetime;

    /**
    * 备货
    */
    private String stock_user_id;

    /**
    * 备货
    */
    private String stock_user_uame;

    /**
    * 备货
    */
    private Date stock_datetime;

    /**
    * 配送
    */
    private String delivery_user_id;

    /**
    * 配送
    */
    private String delivery_user_name;

    /**
    * 配送
    */
    private Date delivery_datetime;

    /**
    * 签收
    */
    private String sign_user_id;

    /**
    * 签收
    */
    private String sign_user_name;

    /**
    * 签收
    */
    private Date sign_datetime;

    /**
    * 发票
    */
    private String ticketing_userId;

    /**
    * 发票
    */
    private String ticketing_user_name;

    /**
    * 发票
    */
    private Date ticketing_datetime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getCreate_userId() {
		return create_userId;
	}

	public void setCreate_userId(String create_userId) {
		this.create_userId = create_userId;
	}

	public String getCreate_user_name() {
		return create_user_name;
	}

	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}

	public Date getCreate_date_time() {
		return create_date_time;
	}

	public void setCreate_date_time(Date create_date_time) {
		this.create_date_time = create_date_time;
	}

	public String getSubmit_user_id() {
		return submit_user_id;
	}

	public void setSubmit_user_id(String submit_user_id) {
		this.submit_user_id = submit_user_id;
	}

	public String getSubmit_user_name() {
		return submit_user_name;
	}

	public void setSubmit_user_name(String submit_user_name) {
		this.submit_user_name = submit_user_name;
	}

	public Date getSubmit_datetime() {
		return submit_datetime;
	}

	public void setSubmit_datetime(Date submit_datetime) {
		this.submit_datetime = submit_datetime;
	}

	public String getVerify_userId() {
		return verify_userId;
	}

	public void setVerify_userId(String verify_userId) {
		this.verify_userId = verify_userId;
	}

	public String getVerify_user_name() {
		return verify_user_name;
	}

	public void setVerify_user_name(String verify_user_name) {
		this.verify_user_name = verify_user_name;
	}

	public Date getVerify_datetime() {
		return verify_datetime;
	}

	public void setVerify_datetime(Date verify_datetime) {
		this.verify_datetime = verify_datetime;
	}

	public String getPay_user_id() {
		return pay_user_id;
	}

	public void setPay_user_id(String pay_user_id) {
		this.pay_user_id = pay_user_id;
	}

	public String getPay_user_name() {
		return pay_user_name;
	}

	public void setPay_user_name(String pay_user_name) {
		this.pay_user_name = pay_user_name;
	}

	public Date getPay_datetime() {
		return pay_datetime;
	}

	public void setPay_datetime(Date pay_datetime) {
		this.pay_datetime = pay_datetime;
	}

	public String getStock_user_id() {
		return stock_user_id;
	}

	public void setStock_user_id(String stock_user_id) {
		this.stock_user_id = stock_user_id;
	}

	public String getStock_user_uame() {
		return stock_user_uame;
	}

	public void setStock_user_uame(String stock_user_uame) {
		this.stock_user_uame = stock_user_uame;
	}

	public Date getStock_datetime() {
		return stock_datetime;
	}

	public void setStock_datetime(Date stock_datetime) {
		this.stock_datetime = stock_datetime;
	}

	public String getDelivery_user_id() {
		return delivery_user_id;
	}

	public void setDelivery_user_id(String delivery_user_id) {
		this.delivery_user_id = delivery_user_id;
	}

	public String getDelivery_user_name() {
		return delivery_user_name;
	}

	public void setDelivery_user_name(String delivery_user_name) {
		this.delivery_user_name = delivery_user_name;
	}

	public Date getDelivery_datetime() {
		return delivery_datetime;
	}

	public void setDelivery_datetime(Date delivery_datetime) {
		this.delivery_datetime = delivery_datetime;
	}

	public String getSign_user_id() {
		return sign_user_id;
	}

	public void setSign_user_id(String sign_user_id) {
		this.sign_user_id = sign_user_id;
	}

	public String getSign_user_name() {
		return sign_user_name;
	}

	public void setSign_user_name(String sign_user_name) {
		this.sign_user_name = sign_user_name;
	}

	public Date getSign_datetime() {
		return sign_datetime;
	}

	public void setSign_datetime(Date sign_datetime) {
		this.sign_datetime = sign_datetime;
	}

	public String getTicketing_userId() {
		return ticketing_userId;
	}

	public void setTicketing_userId(String ticketing_userId) {
		this.ticketing_userId = ticketing_userId;
	}

	public String getTicketing_user_name() {
		return ticketing_user_name;
	}

	public void setTicketing_user_name(String ticketing_user_name) {
		this.ticketing_user_name = ticketing_user_name;
	}

	public Date getTicketing_datetime() {
		return ticketing_datetime;
	}

	public void setTicketing_datetime(Date ticketing_datetime) {
		this.ticketing_datetime = ticketing_datetime;
	}
}
