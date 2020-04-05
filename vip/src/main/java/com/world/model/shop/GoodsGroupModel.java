package com.world.model.shop;

import com.world.data.mysql.Bean;

public class GoodsGroupModel extends Bean {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Integer id;

    /** 分组编号 */
    private String group_code;

    /** 商品编号 */
    private String goods_code;

    /** 商品名称 */
    private String goods_name;

    public String getGroup_code() {
        return group_code;
    }

    public void setGroup_code(String group_code) {
        this.group_code = group_code;
    }

    public String getGoods_code() {
        return goods_code;
    }

    public void setGoods_code(String goods_code) {
        this.goods_code = goods_code;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }
}
