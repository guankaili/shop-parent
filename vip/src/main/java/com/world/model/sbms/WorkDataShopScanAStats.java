package com.world.model.sbms;

import com.world.data.mysql.Bean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/20 22:04
 * @desc 扫码首页表
 **/
@Getter
@Setter
public class WorkDataShopScanAStats extends Bean implements Serializable {

    private static final long serialVersionUID = 562920809096267822L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 经销商code
     */
    private String dealerCode;
    /**
     * 经销商名称
     */
    private String dealerName;
    /**
     * 经销商业务员Id
     */
    private String dealerCmId;
    /**
     * 所属门店编号
     */
    private String shopCode;
    /**
     * 所属门店名称
     */
    private String shopName;
    /**
     * 大区code(N001--北方大区；S001--南方大区)
     */
    private String largeAreaCode;
    /**
     * 大区名称
     */
    private String largeArea;
    /**
     * 省份编号
     */
    private String provinceCode;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 城市编号
     */
    private String cityCode;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 区域编号
     */
    private String areaCode;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 品牌编号
     */
    private String brandCode;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 入库量
     */
    private long shopScanInQuantity;
    /**
     * 入库签约门店数
     */
    private long shopSignQuantity;
    /**
     * 入库门店参与量
     */
    private long shopJoinInQuantity;
    /**
     * 入库门店参与率
     */
    private String shopJoinInRate;
    /**
     * 出库量
     */
    private long shopScanOutQuantity;
    /**
     * 出库门店参与量
     */
    private long shopJoinOutQuantity;
    /**
     * 出库门店参与率
     */
    private String shopJoinOutRate;
    /**
     * 创建时间
     */
    private Date createDate;

}
