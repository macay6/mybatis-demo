package com.macay.entity;

/**
 * @ClassName: GenderEnum
 * @Description:
 * @Author: Macay
 * @Date: 2021/9/25 10:41 下午
 */
public enum GenderEnum {

    MALE(1, "男性"),
    FEMALE(2, "女性");

    private int code;

    private String description;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    GenderEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static GenderEnum getGenderEnum(Integer val) {
        for (GenderEnum genderEnum : GenderEnum.values()) {
            if (genderEnum.getCode() == val) {
                return genderEnum;
            }
        }
        return null;
    }
}
