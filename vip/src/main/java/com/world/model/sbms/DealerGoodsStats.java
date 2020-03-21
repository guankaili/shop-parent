package com.world.model.sbms;

import com.world.data.mysql.Bean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 年进货量统计(DataDealerIgoodsStats)实体类
 *
 * @author gkl
 * @since 2020-03-21 08:48:46
 */
@Getter
@Setter
public class DealerGoodsStats extends Bean implements Serializable {
    private static final long serialVersionUID = -76901772912913851L;
    /**
     * 客户编号
     */
    private String dealerCode;
    /**
     * 客户名称
     */
    private String dealerName;
    /**
     * 省份编号
     */
    private Object provinceCode;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 品牌编号
     */
    private String brandCode;
    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 一月进货量
     */
    private Object goodsNum;
    /**
     * 月份
     */
    private Object m;

}