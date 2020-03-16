package com.world.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
	* @author cuihuofei
	* @date   2019年12月19日 下午6:31:56
	* @version v1.0.0
	* @Description
	* OrderBillType.java 
	* Modification History:
	* Date                         Author          Version          Description
	---------------------------------------------------------------------------------*
	* 2019年12月19日 下午6:31:56       cuihuofei        v1.0.0           Created
*/
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderBillType {
	//提供类型标准订单：XSDD01_SYS；三包订单：XSDD001
	STANDARD_ORDER("XSDD01_SYS", "标准销售订单"),
	THREE_PACKS_ORDER("XSDD001", "三包订单"),
    ;
	
	/**
     * 代码
     */
    private final String code;

    /**
     * 内容
     */
    private final String msg;
    
    /**
     * @param code 代码
     * @param msg  内容
     */
    OrderBillType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
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
    public static OrderBillType getByCode(String code) {
    	OrderBillType[] values = OrderBillType.values();

        for (OrderBillType value : values) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
		return null;
    }
}
