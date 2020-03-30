package com.world.model.sbms;

import com.world.data.mysql.Bean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/26 14:44
 * @desc 获取排名辅助类
 **/
@Getter
@Setter
public class DataRoleRankStatsAssistRawStats extends Bean implements Serializable {

    private static final long serialVersionUID = 365039846789107880L;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 经销商code
     */
    private String dealerCode;
    /**
     * 角色
     */
    private String roleId;
    /**
     * 角色类型  1、森麒麟业务员 2、其它
     */
    private Integer roleType;
}
