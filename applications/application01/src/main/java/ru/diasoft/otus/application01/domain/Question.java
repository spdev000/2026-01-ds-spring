package ru.diasoft.otus.application01.domain;

import lombok.Data;

import java.util.List;

@Data
public class Question {
    private final String text;
    private final QuestionType type;
    private final List<String> answers;

    public Question(String text, QuestionType type, List<String> answers) {
        this.text = text;
        this.type = type;
        this.answers = List.copyOf(answers);
    }

}
