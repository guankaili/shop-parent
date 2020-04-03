package com.world.model.sbms;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/26 14:44
 * @desc 获取排名辅助类
 **/
@Getter
@Setter
public class DataRoleRankStats extends Bean implements Serializable {

    private static final long serialVersionUID = 378039046789107880L;

    /**
     * 排名
     */
    private Double countryRank;
    /**
     * 1、经销商业务员 2、森麒麟业务员 3、其它
     */
    private Integer roleType;
    /**
     * 经销商code 或用户id
     */
    private String userSign;
    /**
     * 经销商名称
     */
    private String userSignName;
    /**
     * 排名总数量
     */
    private Long countryQuantity;
    /**
     * 店铺数量
     */
    private Object shopQuantity;



}
