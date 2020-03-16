package com.world.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
	* @author cuihuofei
	* @date   2019年12月26日 上午10:28:50
	* @version v1.0.0
	* @Description
	* DeliveryType.java 
	* Modification History:
	* Date                         Author          Version          Description
	---------------------------------------------------------------------------------*
	* 2019年12月26日 上午10:28:50       cuihuofei        v1.0.0           Created
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeliveryType {
	//JHFS03  快递,JHFS01_SYS 发货,JHFS02_SYS 自提
	JHFS03("JHFS03", "快递"),
	JHFS01_SYS("JHFS01_SYS", "发货"),
	JHFS02_SYS("JHFS02_SYS", "自提")
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
    DeliveryType(String code, String msg) {
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
    public static DeliveryType getByCode(String code) {
        DeliveryType[] values = DeliveryType.values();

        for (DeliveryType value : values) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
		return null;
    }

}
