package com.world.model.shop;

import com.world.data.mysql.Bean;

public class ShopConfTaskMModel extends Bean {
    private static final long serialVersionUID = 1L;

    /**主键*/
    private long id;

    /**门店类型默认1-CPS;2-CCS;3-CCS+;4-CTS;5-CTS+;*/
    private Integer shop_type;

    /**门店月度任务量(每个月都一样)*/
    private Integer shop_task_m;

    /**是否可升级：0-不可升级 ； 1-可升级*/
    private Integer is_upgrade;

    /**门店类型名字 1-CPS;2-CCS;3-CCS+;4-CTS;5-CTS+;*/
    private String shop_type_name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getShop_type() {
        return shop_type;
    }

    public void setShop_type(Integer shop_type) {
        this.shop_type = shop_type;
    }

    public Integer getShop_task_m() {
        return shop_task_m;
    }

    public void setShop_task_m(Integer shop_task_m) {
        this.shop_task_m = shop_task_m;
    }

    public Integer getIs_upgrade() {
        return is_upgrade;
    }

    public void setIs_upgrade(Integer is_upgrade) {
        this.is_upgrade = is_upgrade;
    }

    public String getShop_type_name() {
        return shop_type_name;
    }

    public void setShop_type_name(String shop_type_name) {
        this.shop_type_name = shop_type_name;
    }
}
