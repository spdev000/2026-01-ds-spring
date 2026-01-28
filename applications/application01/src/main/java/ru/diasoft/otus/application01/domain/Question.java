package ru.diasoft.otus.application01.domain;

import lombok.Data;

import java.util.List;

@Data
public class Question {
    private final String text;
    private final AnswerType type;
    private final String answer;
    private final List<String> answerOptions;

    public Question(String text, AnswerType type, String answer, List<String> answerOptions) {
        this.text = text;
        this.type = type;
        this.answer = answer;
        this.answerOptions = List.copyOf(answerOptions);
    }

}
