package com.world.model.shop;

import com.world.data.mysql.Bean;

public class OrderModel extends Bean {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Integer order_id;

    /** * 交易编号 */
    private String trade_sn;

    /** 订单编号 */
    private String sn;

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getTrade_sn() {
        return trade_sn;
    }

    public void setTrade_sn(String trade_sn) {
        this.trade_sn = trade_sn;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
