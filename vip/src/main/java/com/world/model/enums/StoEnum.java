package com.world.model.enums;

/**
 * @author gehaichao
 * @version v1.0.0
 * @date 2019/12/25$ 16:03$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2019/12/25$ 16:03$        gehaichao          v1.0.0           Created
 */
public enum StoEnum {
    SHORTAGE("库存缺货"),
    TIGHT("库存紧张"),
    MORE("库存充足");
    private final String code;
    StoEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

}
