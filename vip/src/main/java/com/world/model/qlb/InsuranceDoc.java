package com.world.model.qlb;

import com.world.data.mysql.Bean;

import java.io.Serializable;
import java.util.Date;

/**
* 保单表
* @author gkl
* @date 2020-01-11 10:27:40
**/

public class InsuranceDoc  extends Bean implements Serializable {

	private static final long serialVersionUID = 1L;


    /**
     * 主键自增
     */
    private Long id;
    /**
     * 用户的唯一标识
     */
    private String unionId;
    /**
     * 车牌号
     */
    private String carNum;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 保单状态
     */
    private Integer status;
    /**
     * 理赔状态
     */
    private Integer claimStatus;
    /**
     * 轮胎类型  0：冬季胎  1：普通胎
     */
    private Integer tyreType;
    /**
     * 趾口编码
     */
    private String toeMouthCode;
    /**
     * 商品编号
     */
    private String goodsCode;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 商品名称检索时使用
     */
    private String goodsSearchName;
    /**
     * 商品规格
     */
    private String goodsModel;
    /**
     * 尺寸
     */
    private String goodsSize;
    /**
     * 商品花纹
     */
    private String goodsPattern;
    /**
     * 速级
     */
    private String goodsSpeedclass;
    /**
     * 商品规格检索时使用
     */
    private String goodsSearchModel;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 失效时间
     */
    private Date lapseTime;
    /**
     * 鼓包理赔最多2次申请
     */
    private Integer claimApplyNum;

    /**
     * 用户所在城市编码
     */
    private String cityCode;
    /**
     * 用户所在省份编码
     */
    private String provinceCode;
    /**
     * 用户所在国家编码
     */
    private String countryCode;
    /**
     * 用户所在城市
     */
    private String cityName;
    /**
     * 用户所在省份
     */
    private String provinceName;
    /**
     * 用户所在国家
     */
    private String countryName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(Integer claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Integer getTyreType() {
        return tyreType;
    }

    public void setTyreType(Integer tyreType) {
        this.tyreType = tyreType;
    }

    public String getToeMouthCode() {
        return toeMouthCode;
    }

    public void setToeMouthCode(String toeMouthCode) {
        this.toeMouthCode = toeMouthCode;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGoodsSearchName() {
        return goodsSearchName;
    }

    public void setGoodsSearchName(String goodsSearchName) {
        this.goodsSearchName = goodsSearchName;
    }

    public String getGoodsModel() {
        return goodsModel;
    }

    public void setGoodsModel(String goodsModel) {
        this.goodsModel = goodsModel;
    }

    public String getGoodsSize() {
        return goodsSize;
    }

    public void setGoodsSize(String goodsSize) {
        this.goodsSize = goodsSize;
    }

    public String getGoodsPattern() {
        return goodsPattern;
    }

    public void setGoodsPattern(String goodsPattern) {
        this.goodsPattern = goodsPattern;
    }

    public String getGoodsSpeedclass() {
        return goodsSpeedclass;
    }

    public void setGoodsSpeedclass(String goodsSpeedclass) {
        this.goodsSpeedclass = goodsSpeedclass;
    }

    public String getGoodsSearchModel() {
        return goodsSearchModel;
    }

    public void setGoodsSearchModel(String goodsSearchModel) {
        this.goodsSearchModel = goodsSearchModel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLapseTime() {
        return lapseTime;
    }

    public void setLapseTime(Date lapseTime) {
        this.lapseTime = lapseTime;
    }

    public Integer getClaimApplyNum() {
        return claimApplyNum;
    }

    public void setClaimApplyNum(Integer claimApplyNum) {
        this.claimApplyNum = claimApplyNum;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}