package ru.diasoft.otus.application01.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.diasoft.otus.application01.dao.QuestionDao;
import ru.diasoft.otus.application01.domain.AnswerType;
import ru.diasoft.otus.application01.domain.Question;
import ru.diasoft.otus.application01.io.IOService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static ru.diasoft.otus.application01.util.ResourceUtils.readResourceAsLines;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionDao questionDao;
    @Mock
    private IOService ioService;

    @Test
    void procQuestion_printsOptionsAndSingleAnswer() {
        List<String> studentAnswers = readResourceAsLines("answers.csv");

        List<Question> testQuestions = List.of(
                new Question("What is 2+2?", AnswerType.NUMBER, "4", List.of()),
                new Question("Choose colors", AnswerType.OPTIONS, "1", List.of("Red", "Green", "Blue")),
                new Question("What is the capital of France?", AnswerType.TEXT, "Paris", List.of())
        );
        when(questionDao.loadQuestions()).thenReturn(testQuestions);
        when(ioService.readLine()).thenReturn(
                studentAnswers.get(0),
                studentAnswers.get(1),
                studentAnswers.get(2),
                studentAnswers.get(3),
                studentAnswers.get(4));

        QuestionServiceImpl questionService = new QuestionServiceImpl(2, questionDao, ioService);


        questionService.procQuestion();


        verify(ioService, times(1)).printf("Take the %s questions test.\n", testQuestions.size());
        verify(ioService, times(2)).print(anyString());
        verify(ioService, times(5)).readLine();
        verify(ioService, atLeastOnce()).printf(anyString(), any());
    }

    @Test
    void procQuestion_throwsOnInvalidCsvLine() {
        when(questionDao.loadQuestions()).thenThrow(new IllegalArgumentException("Invalid CSV string: invalid_line_without_commas"));

        QuestionServiceImpl questionService = new QuestionServiceImpl(1, questionDao, ioService);

        Exception ex = assertThrows(IllegalArgumentException.class, questionService::procQuestion);
        assertTrue(ex.getMessage().contains("Invalid CSV string"));
    }
}
