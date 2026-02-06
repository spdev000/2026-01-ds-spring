package ru.diasoft.otus.application01.dao;

import ru.diasoft.otus.application01.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> loadQuestions();
}
