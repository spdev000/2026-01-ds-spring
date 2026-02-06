package ru.diasoft.otus.application01.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Question {
    private final String text;
    private final AnswerType type;
    private final String answer;
    private final List<String> answerOptions;
}
