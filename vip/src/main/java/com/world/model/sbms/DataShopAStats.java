package com.world.model.sbms;

import com.world.data.mysql.Bean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/22 10:22
 * @desc 门店统计主页数据
 **/
@Getter
@Setter
public class DataShopAStats extends Bean implements Serializable {

    private static final long serialVersionUID = 5629208090999997822L;

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
     * 经销商业务员id
     */
    private String dealerCmId;
    /**
     * 大区code(N001--北方大区；S001--南方大区)
     */
    private String largeAreaCode;
    /**
     * 大区名称
     */
    private String largeArea;
    /**
     * 品牌编号
     */
    private String brandCode;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 省code
     */
    private Long shopProvinceId;
    /**
     * 省名
     */
    private String shopProvince;
    /**
     * 累计门店数
     */
    private Long shopTotalQuantity;
    /**
     * 抢购门店
     */
    private Long shopPanicQuantity;
    /**
     * 新增门店
     */
    private Long shopAddQuantity;
    /**
     * 新增抢购门店
     */
    private Long shopAddPanicQuantity;
    /**
     * 大区排名
     */
    private Long areaRank;
    /**
     * 大区总数量
     */
    private Long areaQuantity;
    /**
     * 全国排名
     */
    private Long countryRank;
    /**
     * 全国总数量
     */
    private Long countryQuantity;
    /**
     * CTS+占比
     */
    private float ctsARate;
    /**
     * CTS占比
     */
    private float ctsRate;
    /**
     * CCS+占比
     */
    private float ccsARate;
    /**
     * CCS占比
     */
    private float ccsRate;
    /**
     * CPS占比
     */
    private float cpsRate;

}
