package com.eightthreads.backend.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VoucherType {
    PERCENTAGE("percent"),
    FIXED_AMOUNT("fixed");

    private final String databaseValue;

    VoucherType(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    @JsonValue
    public String getDatabaseValue() {
        return databaseValue;
    }

    public static VoucherType fromDatabaseValue(String value) {
        if (value == null) {
            return null;
        }
        for (VoucherType type : values()) {
            if (type.databaseValue.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Voucher type khong hop le: " + value);
    }
}
