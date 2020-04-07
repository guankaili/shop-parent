package com.world.model.shop;

import com.world.data.mysql.Bean;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员优惠券(EsMemberCoupon)实体类
 *
 * @author makejava
 * @since 2020-04-07 19:17:50
 */
@Data
public class EsMemberCouponModel  extends Bean implements Serializable {
    private static final long serialVersionUID = -44387738498311118L;
    /**
    * 主键
    */    
    private Integer mcId;
    /**
    * 优惠券表主键
    */    
    private Integer couponId;
    /**
    * 券码
    */    
    private String couponSn;
    /**
    * 会员主键id
    */    
    private Integer memberId;
    /**
    * 会员昵称
    */    
    private String memberName;
    /**
    * 使用时间
    */    
    private Date usedTime;
    /**
    * 领取时间
    */    
    private Date createTime;
    /**
    * 业务产生ID，例如订单ID
    */    
    private Integer busiId;
    /**
    * 业务产生ID，例如订单项ID对应es_order_items
    */    
    private Integer itemId;
    /**
    * 那个订单使用了此条优惠券-订单主键
    */    
    private Integer orderId;
    /**
    * 那个订单使用了此条优惠券-订单编号
    */    
    private String orderSn;
    /**
    * 优惠券名称
    */    
    private String title;
    /**
    * 优惠券面额
    */    
    private Double couponPrice;
    /**
    * 优惠券门槛金额
    */    
    private Double couponThresholdPrice;
    /**
    * 使用起始时间
    */    
    private Date startTime;
    /**
    * 使用截止时间
    */    
    private Date endTime;
    /**
    * 使用状态;0未使用，3-使用中(冻结)，1已使用,2是已过期；4-未激活；5-退货失效；6-退货(只有赠券占用)；
    */    
    private Object usedStatus;
    /**
    * 商家ID
    */    
    private Integer sellerId;
    /**
    * 商家名称
    */    
    private String sellerName;
    /**
    * 扫码入库ID激活
    */    
    private Long scanInId;
    /**
    * 扫码条码
    */    
    private Long barCode;
    /**
    * 扫码退货ID追回
    */    
    private Long scanOutId;
    /**
    * 追回标记：默认0；1-已追回；
    */    
    private Integer recoverFlag;
    /**
    * 绑定商品id本期按照商品编号=es_goods_sku sn字段
    */    
    private String goodsSkuSn;
    /**
    * 绑定商品名称
    */    
    private String goodsName;
    /**
    * 优惠券类型：1-默认原始的；2-赠券；3-代金券
    */    
    private Integer couponType;



}