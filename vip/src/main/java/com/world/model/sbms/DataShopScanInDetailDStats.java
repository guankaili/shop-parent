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
 * @date 2020/3/22 15:21
 * @desc 扫码入库详细列表 数据
 **/
@Getter
@Setter
public class DataShopScanInDetailDStats extends Bean implements Serializable {

    private static final long serialVersionUID = -6143614101265911566L;

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
     * 入库数量
     */
    private long shopInQuantity;
    /**
     * 入库签约门店数
     */
    private long shopSignQuantity;
    /**
     * 入库门店参与量
     */
    private long shopJoinInQuantity;
    /**
     * 入库门店参与率
     */
    private String shopJoinInRate;
    /**
     * 记录日期
     */
    private String shopScanInDetailDate;

}
