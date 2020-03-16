package com.world.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
	* @author cuihuofei
	* @date   2019年12月26日 上午10:28:29
	* @version v1.0.0
	* @Description
	* BusinessType.java 
	* Modification History:
	* Date                         Author          Version          Description
	---------------------------------------------------------------------------------*
	* 2019年12月26日 上午10:28:29       cuihuofei        v1.0.0           Created
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BusinessType {

    DEALER_COMMON(1, "普通销售"),
//    STORE_GROUP(2, "门店拼团订单"),
//    STORE_SPIKE(3, "门店秒杀订单"),
//    STORE_SPEED(4, "门店速配仓订单"),
    ;

    /**
     * 代码
     */
    private final int code;

    /**
     * 内容
     */
    private final String msg;

    /**
     * @param code 代码
     * @param msg  内容
     */
    BusinessType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据代码获取枚举
     * 不要尝试缓存全部的枚举，该方法用到的频率不会太高，且枚举很少，不会造成资源浪费
     *
     * 使用 @JsonCreator 让 jackson 解析 json 的时候能匹配到该枚举
     * 参考：https://segmentfault.com/q/1010000020636087
     * @param code
     * @return
     */
    @JsonCreator
    public static BusinessType getByCode(int code) {
        BusinessType[] values = BusinessType.values();
        for (BusinessType value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
		return null;
    }
}
