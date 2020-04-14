package com.world.model.sbms;

import com.world.data.mysql.Bean;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/4/10 13:57
 * @desc
 **/
@Data
public class DataShopActivityStats extends Bean implements Serializable {

    private static final long serialVersionUID = 365039848885107880L;

    private String province_code;
    private String province_name;
    private String city_name;
    private String area_name;
    private String dealer_cm_id;
    /**
     * 店主姓名
     */
    private String shop_keeper;

    /**
     * 门店信息
     */
    private int shop_id;
    private String shop_name;

    /**
     * 订单信息
     */
    private int item_id;


}
