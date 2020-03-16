package com.world.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 线下付款的情况使用到
 *
 * @author zhouqi
 * @date 2019/12/2 10:21
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019/12/2 10:21     zhouqi          v1.0.0           Created
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UnderPayNumType {

    BY_ORDER_NUM(1, "按订单数量付款"),
    BY_SIGN_NUM(2, "按实际确认数量付款"),

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
    UnderPayNumType(int code, String msg) {
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
    public static UnderPayNumType getByCode(int code) {

        UnderPayNumType[] values = UnderPayNumType.values();

        for (UnderPayNumType value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }

        return null;
    }
}
