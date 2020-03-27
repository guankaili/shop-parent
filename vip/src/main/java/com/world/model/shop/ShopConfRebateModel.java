package com.world.model.shop;

import com.world.data.mysql.Bean;

import java.util.Date;

public class ShopConfRebateModel extends Bean {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    private Integer id;

    /**门店类型默认1-CPS;2-CCS;3-CCS+;4-CTS;5-CTS+;*/
    private Integer shop_type;

    /**门店类型名字 1-CPS;2-CCS;3-CCS+;4-CTS;5-CTS+;*/
    private String shop_type_name;

    /**轮胎尺寸*/
    private Integer size;

    /**对应积分值*/
    private Integer score;

    /**开始时间，防止分段*/
    private Date start_time;

    /**结束时间，防止分段*/
    private Date end_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShop_type() {
        return shop_type;
    }

    public void setShop_type(Integer shop_type) {
        this.shop_type = shop_type;
    }

    public String getShop_type_name() {
        return shop_type_name;
    }

    public void setShop_type_name(String shop_type_name) {
        this.shop_type_name = shop_type_name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
}
