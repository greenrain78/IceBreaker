package com.example.IceBreaking.domain;

import lombok.Getter;

@Getter
public enum TeamTypeEnum {
    BASIC("basic"),
    WELCOME("welcome");

    private final String val;

    TeamTypeEnum(String val) {
        this.val = val;
    }
}
