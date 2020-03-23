package com.world.model.sbms;

import com.world.data.mysql.Bean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/21 10:02
 * @desc 门店扫码入库 ---月统计
 **/

@Getter
@Setter
public class WorkDataShopScanOutMYStats extends Bean implements Serializable {

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
    private Long scanOutCount;

}
