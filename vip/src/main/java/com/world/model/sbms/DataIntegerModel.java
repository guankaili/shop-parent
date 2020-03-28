package com.world.model.sbms;

import com.world.data.mysql.Bean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/27 22:27
 * @desc
 **/
@Getter
@Setter
public class DataIntegerModel extends Bean implements Serializable {
    private static final long serialVersionUID = 319290857907611144L;

    //获取数量通用
    private Object count;

}
