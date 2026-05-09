package com.eightthreads.backend.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventStatus {
    DRAFT("draft"),
    UPCOMING("upcoming"),
    SELLING("selling"),
    ENDED("ended"),
    CANCELLED("cancelled"),
    ACTIVE("active");

    private final String value;

    EventStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EventStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (EventStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Trạng thái sự kiện không hợp lệ: " + value);
    }
}

