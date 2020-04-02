package com.world.model.sbms;

import com.world.data.mysql.Bean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/31 21:31
 * @desc
 **/
@Getter
@Setter
public class DataDealerCmIdStatus extends Bean implements Serializable {

    private static final long serialVersionUID = 319290857977779644L;

    private String dealerCmId;

    private String userId;

    private String dealerCode;

    private String userSign;

    private Object year;

    private String thisDate;
}
