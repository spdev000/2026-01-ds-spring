package ru.diasoft.otus.application01.domain;

public enum AnswerType {
    NUMBER(0),
    OPTIONS(1),
    TEXT(2);

    private final int code;

    AnswerType(int code) {
        this.code = code;
    }

    public static AnswerType fromCode(int code) {
        for (AnswerType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown answer type: " + code);
    }
}
