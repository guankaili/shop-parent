package com.world.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author cuihuofei
 * @date 2019年12月19日 下午7:03:24
 * @version v1.0.0
 * @Description OrderThreePacks.java Modification History: Date Author Version
 *              Description
 *              ---------------------------------------------------------------------------------*
 *              2019年12月19日 下午7:03:24 cuihuofei v1.0.0 Created
 */
public enum OrderThreePacks {
	// 提供类型 麒麟保：QLB;三包：SOC
	QLB("QLB", "麒麟保"), 
	SOC("SOC", "三包"),
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
	OrderThreePacks(String code, String msg) {
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
	 * 根据代码获取枚举 不要尝试缓存全部的枚举，该方法用到的频率不会太高，且枚举很少，不会造成资源浪费
	 *
	 * 使用 @JsonCreator 让 jackson 解析 json 的时候能匹配到该枚举
	 * 参考：https://segmentfault.com/q/1010000020636087
	 * 
	 * @param code
	 * @return
	 */
	@JsonCreator
	public static OrderThreePacks getByCode(String code) {
		OrderThreePacks[] values = OrderThreePacks.values();

		for (OrderThreePacks value : values) {
			if (code.equals(value.getCode())) {
				return value;
			}
		}

		return null;
	}
}
