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
 * @desc 门店统计 --签约 抢购 柱状图统计
 **/
@Getter
@Setter
public class DataShopSignBuyAStats extends Bean implements Serializable {

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
     * 月份
     */
    private String shopSignBuyMonth;
    /**
     * 认证数量
     */
    private Long shopSignCount;
    /**
     * 抢购数量
     */
    private Long shopBuyCount;

}
