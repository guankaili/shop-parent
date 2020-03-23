package com.world.model.sbms;

import com.world.data.mysql.Bean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/22 11:28
 * @desc 门店统计 --新增门店统计
 **/
@Getter
@Setter
public class DataShopAddAStats extends Bean implements Serializable {

    private static final long serialVersionUID = 365039848885107880L;
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
    private String largeArea;
    /**
     * 年份
     */
    private String ShopAddYear;
    /**
     * 月份
     */
    private String shopAddMonth;
    /**
     * 扫码数量
     */
    private Long shopAddCount;

}
