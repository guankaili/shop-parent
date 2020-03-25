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
 * @date 2020/3/20 11:25
 * @desc 数据首页表
 **/
@Getter
@Setter
public class DataMainMStats extends Bean implements Serializable {

    private static final long serialVersionUID = 392688472729974442L;

    /**
     * 经销商id
     */
    private String dealerCode;
    /**
     * 经销商编号
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
     * 门店总数
     */
    private long shopQuantity;
    /**
     * 活跃门店数
     */
    private long activeShopQuantity;
    /**
     * 新增门店数
     */
    private long addShopQuantity;
    /**
     * 扫码入库数
     */
    private long scanInQuantity;
    /**
     * 扫码出库数
     */
    private long scanOutQuantity;
    /**
     * 扫码参与门店数
     */
    private long scanJoinShopQuantity;
    /**
     * 特价轮胎抢购数量
     */
    private long specialTyreScrambleQuantity;
    /**
     * 特价轮胎抢购参与门店数量
     */
    private long specialJoinShopQuantity;
    /**
     * 特价轮胎店均抢购数量
     */
    private long specialTyreScrambleAquantity;
    /**
     * 申请数量
     */
    private long qlbApplyQuantity;
    /**
     * 理赔数量
     */
    private long qlbClaimQuantity;
    /**
     * 理赔率
     */
    private long qlbClaimRate;
    /**
     * 抽取保存时间
     */
    private Date createDate;


}
