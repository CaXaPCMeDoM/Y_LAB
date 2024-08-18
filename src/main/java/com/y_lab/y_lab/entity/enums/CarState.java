package com.y_lab.y_lab.entity.enums;

import lombok.Getter;

@Getter
public enum CarState {
    NEW("NEW"),
    USED("USED");
    private final String value;

    CarState(String value) {
        this.value = value;
    }
}
