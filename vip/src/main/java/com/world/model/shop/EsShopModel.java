package com.world.model.shop;

import com.world.data.mysql.Bean;
import lombok.Data;

import java.io.Serializable;

/**
 * 门店信息
 *
 * @author makejava
 * @since 2020-04-07 19:17:50
 */
@Data
public class EsShopModel extends Bean implements Serializable {
    private static final long serialVersionUID = -44387738498311118L;
    /**
     * 门店id
     */
    private Integer shopId;



}