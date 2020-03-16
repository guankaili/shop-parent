package com.world.model.qly;

import com.world.data.mysql.Bean;

import java.math.BigDecimal;
import java.util.Date;

public class OrderReceivable extends Bean {
    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    private Long id;

    /**
     * 应收单编号
     */
    private String receivableCode;

    /**
     * 单据类型编号
     */
    private String receivableTypeCode;

    /**
     * 单据类型名称
     */
    private String receivableTypeName;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 客户编号
     */
    private String dealerCode;

    /**
     * 客户名称
     */
    private String dealerName;

    /**
     * 结算币别编号
     */
    private String settleCurrencyCode;

    /**
     * 结算币别名称
     */
    private String settleCurrencyName;

    /**
     * 出库日期
     */
    private Date receivableDate;

    /**
     * 整单折扣
     */
    private BigDecimal orderDiscount;

    /**
     * 订单附加费用
     */
    private BigDecimal orderAddCostAmount;

    /**
     * 订单使用返利金额
     */
    private BigDecimal orderRebateAmount;

    /**
     * 税率
     */
    private BigDecimal tax;

    /**
     * 订单未税金额
     */
    private BigDecimal orderNtaxAmount;

    /**
     * 订单含税金额
     */
    private BigDecimal orderHtaxAmount;

    /**
     * 抽取保存时间
     */
    private Date createDate;
}
