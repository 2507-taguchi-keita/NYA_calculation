package com.example.NYA_calculation.enums;

public enum Department {

    ACCOUNTING(1,"経理部"),
    HUMAN_RESOURCES(2,"人事部"),
    SALES(3,"営業部");

    private final Integer code;
    private final String label;

    Department(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    public Integer getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    // --- DBから読み込むとき用のユーティリティ ---
    public static Department fromCode(Integer code) {
        for (Department d : Department.values()) {
            if (d.code == code) {
                return d;
            }
        }
        throw new IllegalArgumentException("Invalid Department code: " + code);
    }

}
