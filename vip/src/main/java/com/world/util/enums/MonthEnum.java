package com.world.util.enums;

/**
 * @author guankaili
 * @version v1.0.0
 * @date 2020/3/14$ 10:58$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/3/14$ 10:58$     guankaili          v1.0.0           Created
 */
public enum MonthEnum {
    M1("1","一月"),
    M2("2","二月"),
    M3("3","三月"),
    M4("4","四月"),
    M5("5","五月"),
    M6("6","六月"),
    M7("7","七月"),
    M8("8","八月"),
    M9("9","九月"),
    M10("10","十月"),
    M11("11","十一月"),
    M12("12","十二月"),
    ;
    private final String code;

    private final String name;

    MonthEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
