package com.world.util.enums;

/**
 * @author guankaili
 * @version v1.0.0
 * @date 2020/3/10$ 10:25$
 * @Description Modification History: Date Author Version Description
 *              ---------------------------------------------------------------------------------*
 *              2020/3/10$ 10:25$ guankaili v1.0.0 Created
 */
public enum BrandEnum {
	SENTURY_LH("LH", "路航", 1),
	SENTURY_DL("DL", "德林特", 2),
	SENTURY_ST("ST", "森麒麟", 3),
	;

	private String code;
	private String name;
	private Integer value;

	BrandEnum(String code, String name, Integer value) {
		this.code = code;
		this.name = name;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Integer getValue() {
		return value;
	}

	public static BrandEnum getByCode(String code) {

		BrandEnum[] values = BrandEnum.values();

		for (BrandEnum value : values) {
			if (value.getCode().equals(code)) {
				return value;
			}
		}

		throw new RuntimeException(String.format("未找到 [%s] 对应的类型!", code));
	}

	/**
	 * 
	 * @Author:weilai
	 * @Data:2020年3月20日下午12:30:39
	 * @Desc:
	 *        <li>根据code返回对应value
	 *
	 */
	public static Integer getValue(String code) {
		Integer i = 100;
		BrandEnum[] values = BrandEnum.values();
		for (BrandEnum e : values) {
			if (e.getCode().equals(code)) {
				i = e.getValue();
				break;
			}
		}
		return i;
	}
}
