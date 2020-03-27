package com.world.model.shop;

import com.world.data.mysql.Bean;

public class GoodsSkuModel extends Bean {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer sku_id;
    /**
     * 商品id
     */
    private Integer goods_id;
    /**
     * 商品名称
     */
    private String goods_name;
    /**
     * 商品编号
     */
    private String sn;
    /**
     * 库存
     */
    private Integer quantity;
    /**
     * 可用库存
     */
    private Integer enable_quantity;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 规格信息json
     */
    private String specs;
    /**
     * 成本价格
     */
    private Double cost;
    /**
     * 重量
     */
    private Double weight;
    /**
     * 卖家id
     */
    private Integer seller_id;
    /**
     * 卖家名称
     */
    private String seller_name;
    /**
     * 分类id
     */
    private Integer category_id;
    /**
     * 分类名称
     * xian	2020-03-07
     */
    private Integer category_name;
    /**
     * 缩略图
     */
    private String thumbnail;

    private Integer hash_code;

    /**
     * 增加活动字段
     * xian 2020-03-12
     */
    //活动ID
    private Integer activity_id;

    //活动价格
    private double activity_price;

    /**
     * 品牌ID
     */
    private Integer brand_id;

    public Integer getSku_id() {
        return sku_id;
    }

    public void setSku_id(Integer sku_id) {
        this.sku_id = sku_id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getEnable_quantity() {
        return enable_quantity;
    }

    public void setEnable_quantity(Integer enable_quantity) {
        this.enable_quantity = enable_quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(Integer seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Integer getCategory_name() {
        return category_name;
    }

    public void setCategory_name(Integer category_name) {
        this.category_name = category_name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getHash_code() {
        return hash_code;
    }

    public void setHash_code(Integer hash_code) {
        this.hash_code = hash_code;
    }

    public Integer getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(Integer activity_id) {
        this.activity_id = activity_id;
    }

    public double getActivity_price() {
        return activity_price;
    }

    public void setActivity_price(double activity_price) {
        this.activity_price = activity_price;
    }

    public Integer getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Integer brand_id) {
        this.brand_id = brand_id;
    }
}
