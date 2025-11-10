package com.example.NYA_calculation.enums;

public enum Step {
    NOT_APPLIED(0, "未申請"),
    ACCOUNTING(1, "経理部"),
    ACCOUNTING_MANAGER(2, "経理部長"),
    SUPERVISOR(3, "所属上長"),
    APPROVED(4, "承認済み");

    private final int code;
    private final String label;

    Step(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static Step fromCode(int code) {
        for (Step s : values()) {
            if (s.code == code) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown Step code: " + code);
    }
}