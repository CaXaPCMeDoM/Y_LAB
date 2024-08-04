package com.y_lab.y_lab.entity.enums;

public enum Role {
    ADMIN ("Admin"),
    MANAGER ("Manager"),
    USER ("User");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
