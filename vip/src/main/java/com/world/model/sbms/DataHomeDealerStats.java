package com.world.model.sbms;

import com.world.data.mysql.Bean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 业务通经销商进出货物统计(DataHomeDealerStats)实体类
 *
 * @author gkl
 * @since 2020-03-20 15:19:10
 */
@Getter
@Setter
public class DataHomeDealerStats extends Bean implements Serializable {
    private static final long serialVersionUID = 319290857907629644L;
    /**
     * 主键自增
     */
    private Long id;
    /**
     * 客户编号
     */
    private String dealerCode;
    /**
     * 客户名称
     */
    private String dealerName;
    /**
     * 经销商业务员
     */
    private String dealerCmId;
    /**
     * 大区code(N001--北方大区；S001--南方大区)
     */
    private String largeAreaCode;
    /**
     * 大区名称
     */
    private String largeAreaName;
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
     * 经销商进货量-年度
     */
    private Object dealerInQuantityY;
    /**
     * 经销商出货量-年度
     */
    private Object dealerOutQuantityY;
    /**
     * 经销商扫码退货量-年度
     */
    private Object dealerScanRquantityY;
    /**
     * 经销商进货量-月度
     */
    private Object dealerInQuantitytM;
    /**
     * 经销商进货量较上月率-月度
     */
    private BigDecimal dealerInRateM;
    /**
     * 经销商出货量-月度
     */
    private Object dealerOutQuantityM;
    /**
     * 经销商出货量较上月率-月度
     */
    private BigDecimal dealerOutRateM;
    /**
     * 抽取保存时间
     */
    private Date createDate;

}