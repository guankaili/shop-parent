package com.world.model.sbms;

import com.world.data.mysql.Bean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 业务通门店任务统计(WorkHomeStats)实体类
 *
 * @author gkl
 * @since 2020-03-19 15:05:28
 */
@Getter
@Setter
public class WorkHomeYStats extends Bean implements Serializable {
    private static final long serialVersionUID = 370942512679651283L;
    /**
    * 客户编号
    */    
    private String dealerCode;
    /**
    * 客户名称
    */    
    private String dealerName;
    /**
    * 经销商业务员id
    */    
    private String dealerCmId;
    /**
    * 门店id
    */    
    private Integer shopId;
    /**
    * 门店类型默认
    */    
    private Integer shopType;
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
    private Long provinceCode;
    /**
    * 省份名称
    */    
    private String provinceName;
    /**
    * 城市编号
    */    
    private Long cityCode;
    /**
    * 城市名称
    */    
    private String cityName;
    /**
    * 区域编号
    */    
    private Long areaCode;
    /**
    * 区域名称
    */    
    private String areaName;
    /**
    * 品牌code
    */    
    private String brandCode;
    /**
    * 品牌名
    */    
    private String brandName;
    /**
    * 签约门店数量-月度
    */    
    private Integer signShopQuantityY;
    /**
    * 签约门店任务数量-月度
    */    
    private Long signShopTaskQuantityY;
    /**
    * 签约门店任务完成数量-月度
    */    
    private Long signShopTaskCquantityY;
    /**
    * 签约门店任务未完成数量-月度
    */    
    private Long signShopTaskNquantityY;

}