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
 * @date 2020/3/22 11:28
 * @desc
 **/
@Getter
@Setter
public class DataShopAddAFromStats extends Bean implements Serializable {

    private static final long serialVersionUID = 365039848885107880L;
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
     * 年份
     */
    private String ShopAddYear;
    /**
     * 一月新增门店数
     */
    private Integer shopAddQuantityM1;
    /**
     * 二月新增门店数
     */
    private Integer shopAddQuantityM2;
    /**
     * 三月新增门店数
     */
    private Integer shopAddQuantityM3;
    /**
     * 四月新增门店数
     */
    private Integer shopAddQuantityM4;
    /**
     * 五月新增门店数
     */
    private Integer shopAddQuantityM5;
    /**
     * 六月新增门店数
     */
    private Integer shopAddQuantityM6;
    /**
     * 七月新增门店数
     */
    private Integer shopAddQuantityM7;
    /**
     * 八月新增门店数
     */
    private Integer shopAddQuantityM8;
    /**
     * 九月新增门店数
     */
    private Integer shopAddQuantityM9;
    /**
     * 十月新增门店数
     */
    private Integer shopAddQuantityM10;
    /**
     * 十一月新增门店数
     */
    private Integer shopAddQuantityM11;
    /**
     * 十二月新增门店数
     */
    private Integer shopAddQuantityM12;

}
