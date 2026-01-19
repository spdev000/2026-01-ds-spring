package ru.diasoft.otus.application01.domain;

public enum QuestionType {
    NUMBER(0),
    OPTIONS(1),
    TEXT(2);

    private final int code;

    QuestionType(int code) {
        this.code = code;
    }

    public static QuestionType fromCode(int code) {
        for (QuestionType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown question type: " + code);
    }
}
