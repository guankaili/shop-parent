package com.world.model.shop;

import com.world.data.mysql.Bean;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/4/10 14:10
 * @desc
 **/
@Data
public class ActivityGoodsModel extends Bean implements Serializable {
    private static final long serialVersionUID = -44387738498311118L;

    private Integer id;
    private String activity_name;
    private String activity_title;
    private Integer activity_purchase_limits;
    private Integer activity_open_flag;
    private Date activity_start_datetime;
    private Date activity_end_datetime;


}
