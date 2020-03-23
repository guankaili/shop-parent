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
 * @date 2020/3/22 15:22
 * @desc 扫码出库详细列表 数据
 **/
@Getter
@Setter
public class WorkDataShopScanOutDetailDStats extends Bean implements Serializable {

    private static final long serialVersionUID = -6143514101265936566L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 省份编号
     */
    private Long shopProvinceId;
    /**
     * 省份名称
     */
    private String shopProvince;
    /**
     * 经销商code
     */
    private String dealerCode;
    /**
     * 经销商名称
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
    private String largeArea;
    /**
     * 出库扫码门店数量
     */
    private long shopOutQuantity;
    /**
     * 出库签约门店数
     */
    private long shopSignQuantity;
    /**
     * 出库门店参与量
     */
    private long shopJoinOutQuantity;
    /**
     * 出库门店参与率
     */
    private String shopJoinOutRate;
    /**
     * 记录日期
     */
    private Date shopScanOutDetailDate;

}
