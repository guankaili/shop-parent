package com.world.model.shop;

import com.world.data.mysql.Bean;

import java.util.Date;

public class ScanBatchRecordDetailModel extends Bean {
    private static final long serialVersionUID = 1L;

    /**主键*/
    private long id;

    /**
     * 批次编号
     */
    private String batch_no;

    /**
     * 轮胎趾口编码
     */
    private long bar_code;

    /**
     * 所属经销商编号
     */
    private String dealer_code;

    /**
     * 所属经销商名称
     */
    private String dealer_name;

    /**
     * 所属门店编号
     */
    private int shop_id;

    /**
     * 所属经门店名称
     */
    private String shop_name;

    /**
     * 品牌编号
     */
    private String brand_code;

    /**
     * 品牌名称
     */
    private String brand_name;

    /**
     * 商品编号
     */
    private String goods_code;

    /**
     * 商品名称
     */
    private String goods_name;

    /**
     * 商品名称检索使用
     */
    private String goods_search_name;

    /**
     * 规格型号
     */
    private String goods_model;

    /**
     * 尺寸
     */
    private String goods_size;

    /**
     * 花纹
     */
    private String goods_pattern;

    /**
     * 速级
     */
    private String goods_speedClass;

    /**
     * 规格型号搜索用
     */
    private String goods_search_model;

    /**
     * 轮胎状态：1-正常;2-此编码不属于当前的经销商;3-此编码没有对应的经销商;4-已退货
     */
    private int check_state;

    /**
     * 扫码种类：1-经销商出库;2-经销商退货扫码;3-门店入库;4-门店出库
     */
    private int scan_type;

    /**
     * 订单状态：1-正常;2-门店入库退货;3-门店未入库退货;
     */
    private int flow_state;

    /**
     * 创建人
     */
    private String create_user_id;

    /**
     * 创建人姓名
     */
    private String create_user_name;

    /**
     * 创建时间扫码时间
     */
    private Date create_datetime;

    /**
     * 预展示处理标志：默认0；1-已处理成功；2-处理失败；
     */
    private int show_deal_flag;

    /**
     * 预展示处理信息
     */
    private String show_deal_msg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public long getBar_code() {
        return bar_code;
    }

    public void setBar_code(long bar_code) {
        this.bar_code = bar_code;
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

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getBrand_code() {
        return brand_code;
    }

    public void setBrand_code(String brand_code) {
        this.brand_code = brand_code;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
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

    public String getGoods_search_name() {
        return goods_search_name;
    }

    public void setGoods_search_name(String goods_search_name) {
        this.goods_search_name = goods_search_name;
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

    public int getCheck_state() {
        return check_state;
    }

    public void setCheck_state(int check_state) {
        this.check_state = check_state;
    }

    public int getScan_type() {
        return scan_type;
    }

    public void setScan_type(int scan_type) {
        this.scan_type = scan_type;
    }

    public int getFlow_state() {
        return flow_state;
    }

    public void setFlow_state(int flow_state) {
        this.flow_state = flow_state;
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

    public Date getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    public int getShow_deal_flag() {
        return show_deal_flag;
    }

    public void setShow_deal_flag(int show_deal_flag) {
        this.show_deal_flag = show_deal_flag;
    }

    public String getShow_deal_msg() {
        return show_deal_msg;
    }

    public void setShow_deal_msg(String show_deal_msg) {
        this.show_deal_msg = show_deal_msg;
    }
}
