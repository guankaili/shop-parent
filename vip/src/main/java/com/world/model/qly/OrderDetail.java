package com.world.model.qly;

import java.math.BigDecimal;
import com.world.data.mysql.Bean;

/**
	* @author cuihuofei
	* @date   2019年12月26日 上午11:06:33
	* @version v1.0.0
	* @Description
	* OrderDetail.java 
	* Modification History:
	* Date                         Author          Version          Description
	---------------------------------------------------------------------------------*
	* 2019年12月26日 上午11:06:33       cuihuofei        v1.0.0           Created
 */
public class OrderDetail extends Bean {
	private static final long serialVersionUID = 1L;
	
	private Long id;

	/**
	 * 订单编号 关联订单主表
	 */
	private Long order_id;

	/**
	 * 商品编号 关联商品表
	 */
	private String goods_code;

	/**
	 * 商品 name 冗余设计
	 */
	private String goods_name;
	
	/**
	 * 商品 规格型号
	 */
	private String goods_model;
	
	/**
	 * 商品 尺寸
	 */
	private String goods_size;
	
	/**
	 * 商品 花纹
	 */
	private String goods_pattern;
	
	/**
	 * 商品 速级
	 */
	private String goods_speedClass;
	
	/**
	 * 商品 规格型号搜索用
	 */
	private String goods_search_model;

	/**
	 * 标准单价编码
	 */
	private String price_code;
	
	/**
	 * 标准单价名称
	 */
	private String price_name;

	/**
	 * 标准单价
	 */
	private BigDecimal goods_base_price;

	/**
	 * 实际单价
	 */
	private BigDecimal goods_real_price;

	/**
	 * 商品数量
	 */
	private Integer goods_num;

	/**
	 * 确认数量 默认为 商品数量
	 */
	private Integer confirm_num;

	/**
	 * 已发货数量
	 */
	private Integer shipped_num;

	/**
	 * 签收数量 如果存在多次发货 , 需要一直使用到该字段 , 在经销商确认收货的时候需要更新本字段 未有签收操作之前默认为 0
	 */
	private Integer sign_num;

	/**
	 * 购买总价 购买总价=confirm_num*goods_real_price
	 */
	private BigDecimal goods_total_price;

	/**
	 * 折扣率
	 */
	private BigDecimal discount_rate;

	/**
	 * 价税合计金额=confirm_num*goods_base_price*discount_rate
	 */
	private BigDecimal discount_rate_total_price;

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

	public String getGoods_code() {
		return goods_code;
	}

	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getPrice_code() {
		return price_code;
	}

	public void setPrice_code(String price_code) {
		this.price_code = price_code;
	}

	public BigDecimal getGoods_base_price() {
		return goods_base_price;
	}

	public void setGoods_base_price(BigDecimal goods_base_price) {
		this.goods_base_price = goods_base_price;
	}

	public BigDecimal getGoods_real_price() {
		return goods_real_price;
	}

	public void setGoods_real_price(BigDecimal goods_real_price) {
		this.goods_real_price = goods_real_price;
	}

	public Integer getGoods_num() {
		return goods_num;
	}

	public void setGoods_num(Integer goods_num) {
		this.goods_num = goods_num;
	}

	public Integer getConfirm_num() {
		return confirm_num;
	}

	public void setConfirm_num(Integer confirm_num) {
		this.confirm_num = confirm_num;
	}

	public Integer getShipped_num() {
		return shipped_num;
	}

	public void setShipped_num(Integer shipped_num) {
		this.shipped_num = shipped_num;
	}

	public Integer getSign_num() {
		return sign_num;
	}

	public void setSign_num(Integer sign_num) {
		this.sign_num = sign_num;
	}

	public BigDecimal getGoods_total_price() {
		return goods_total_price;
	}

	public void setGoods_total_price(BigDecimal goods_total_price) {
		this.goods_total_price = goods_total_price;
	}

	public BigDecimal getDiscount_rate() {
		return discount_rate;
	}

	public void setDiscount_rate(BigDecimal discount_rate) {
		this.discount_rate = discount_rate;
	}

	public BigDecimal getDiscount_rate_total_price() {
		return discount_rate_total_price;
	}

	public void setDiscount_rate_total_price(BigDecimal discount_rate_total_price) {
		this.discount_rate_total_price = discount_rate_total_price;
	}

	public String getPrice_name() {
		return price_name;
	}
	public void setPrice_name(String price_name) {
		this.price_name = price_name;
	}

	public String getGoods_model() {
		return goods_model;
	}

	public void setGoods_model(String goods_model) {
		this.goods_model = goods_model;
	}

	public String getGoods_size() {
		return goods_size;
	}

	public void setGoods_size(String goods_size) {
		this.goods_size = goods_size;
	}

	public String getGoods_pattern() {
		return goods_pattern;
	}

	public void setGoods_pattern(String goods_pattern) {
		this.goods_pattern = goods_pattern;
	}

	public String getGoods_speedClass() {
		return goods_speedClass;
	}

	public void setGoods_speedClass(String goods_speedClass) {
		this.goods_speedClass = goods_speedClass;
	}

	public String getGoods_search_model() {
		return goods_search_model;
	}

	public void setGoods_search_model(String goods_search_model) {
		this.goods_search_model = goods_search_model;
	}
}
