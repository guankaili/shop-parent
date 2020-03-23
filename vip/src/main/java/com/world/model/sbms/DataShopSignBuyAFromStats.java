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
public class DataShopSignBuyAFromStats extends Bean implements Serializable {

    private static final long serialVersionUID = 120766989737942174L;
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
    private String ShopSignBuyYear;
    /**
     * 一月签约门店数
     */
    private Integer shopSignQuantityM1;
    /**
     * 一月抢购门店数
     */
    private Integer shopBuyQuantityM1;
    /**
     * 二月签约门店数
     */
    private Integer shopSignQuantityM2;
    /**
     * 二月抢购门店数
     */
    private Integer shopBuyQuantityM2;
    /**
     * 三月签约抢购门店数
     */
    private Integer shopSignQuantityM3;
    /**
     * 三月抢购门店数
     */
    private Integer shopBuyQuantityM3;
    /**
     * 四月签约门店数
     */
    private Integer shopSignQuantityM4;
    /**
     * 四月抢购门店数
     */
    private Integer shopBuyQuantityM4;
    /**
     * 五月签约门店数
     */
    private Integer shopSignQuantityM5;
    /**
     * 五月抢购门店数
     */
    private Integer shopBuyQuantityM5;
    /**
     * 六月签约门店数
     */
    private Integer shopSignQuantityM6;
    /**
     * 六月抢购门店数
     */
    private Integer shopBuyQuantityM6;
    /**
     * 七月签约门店数
     */
    private Integer shopSignQuantityM7;
    /**
     * 七月抢购门店数
     */
    private Integer shopBuyQuantityM7;
    /**
     * 八月签约门店数
     */
    private Integer shopSignQuantityM8;
    /**
     * 八月抢购门店数
     */
    private Integer shopBuyQuantityM8;
    /**
     * 九月签约门店数
     */
    private Integer shopSignQuantityM9;
    /**
     * 九月抢购门店数
     */
    private Integer shopBuyQuantityM9;
    /**
     * 十月签约门店数
     */
    private Integer shopSignQuantityM10;
    /**
     * 十月抢购门店数
     */
    private Integer shopBuyQuantityM10;
    /**
     * 十一月签约门店数
     */
    private Integer shopSignQuantityM11;
    /**
     * 十一月抢购门店数
     */
    private Integer shopBuyQuantityM11;
    /**
     * 十二月签约门店数
     */
    private Integer shopSignQuantityM12;
    /**
     * 十二月抢购门店数
     */
    private Integer shopBuyQuantityM12;

}
