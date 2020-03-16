package com.world.model.enums;

import com.world.model.entity.SysEnum;

public enum BonusEnum implements SysEnum{

    LEVEL_BONUS(1, "奖励",1);




    private BonusEnum(int key, String value,int type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }


    private int key;
    private String value;
    private int type;

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static String getValue(int key) {
        for (BonusEnum bonusEnum : BonusEnum.values()) {
            if (bonusEnum.getKey() == key) {
                return bonusEnum.getValue();
            }
        }
        return "";
    }

    public static int getType(int key) {
        for (BonusEnum bonusEnum : BonusEnum.values()) {
            if (bonusEnum.getKey() == key) {
                return bonusEnum.getType();
            }
        }
        return 1;
    }


}
