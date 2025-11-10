package com.example.NYA_calculation.enums;

public enum Status {

    TEMPORARY(0, "一時保存"),
    APPLIED(1, "申請中"),
    SENT_BACK(2, "差し戻し"),
    APPROVED(3, "承認済み");

    private final Integer code;
    private final String label;

    Status(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public Integer getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    // DBから数値→Enumへ変換
    public static Status fromCode(int code) {
        for (Status status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown SlipStatus code: " + code);
    }
}
