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
 * @date 2020/3/21 10:02
 * @desc 门店扫码入库 ---月统计 格式化后的数据 跟表统一
 **/

@Getter
@Setter
public class WorkDataShopScanInMYFromStats extends Bean implements Serializable {

    private static final long serialVersionUID = 370942596329651283L;

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
     * 大区code(N001--北方大区；S001--南方大区)
     */
    private String largeAreaCode;
    /**
     * 大区名称
     */
    private String largeAreaName;
    /**
     * 年份
     */
    private String scanYear;
    /**
     * 月份
     */
    private String scanMonth;

    /**
     * 扫码数量
     */
    private Integer scanInCount;
    /**
     * 一月扫码入库量
     */
    private Integer shopInQuantityM1;
    /**
     * 二月扫码入库量
     */
    private Integer shopInQuantityM2;
    /**
     * 三月扫码入库量
     */
    private Integer shopInQuantityM3;
    /**
     * 四月扫码入库量
     */
    private Integer shopInQuantityM4;
    /**
     * 五月扫码入库量
     */
    private Integer shopInQuantityM5;
    /**
     * 六月扫码入库量
     */
    private Integer shopInQuantityM6;
    /**
     * 七月扫码入库量
     */
    private Integer shopInQuantityM7;
    /**
     * 八月扫码入库量
     */
    private Integer shopInQuantityM8;
    /**
     * 九月扫码入库量
     */
    private Integer shopInQuantityM9;
    /**
     * 十月扫码入库量
     */
    private Integer shopInQuantityM10;
    /**
     * 十一月扫码入库量
     */
    private Integer shopInQuantityM11;
    /**
     * 十二月扫码入库量
     */
    private Integer shopInQuantityM12;

}
