package com.world.model.shop;

import com.world.data.mysql.Bean;

public class ShopDetailModel extends Bean {
    private static final long serialVersionUID = 1L;

    /**
     * 门店类型
     */
    private Integer shop_type;

    /**
     * 会员ID
     */
    private Integer member_id;

    public Integer getShop_type() {
        return shop_type;
    }

    public void setShop_type(Integer shop_type) {
        this.shop_type = shop_type;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }
}
