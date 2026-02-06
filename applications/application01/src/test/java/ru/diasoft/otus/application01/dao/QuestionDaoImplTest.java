package ru.diasoft.otus.application01.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import ru.diasoft.otus.application01.domain.Question;
import ru.diasoft.otus.application01.util.ResourceUtils;

import java.io.UncheckedIOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class QuestionDaoImplTest {

    private MockedStatic<ResourceUtils> mockedStatic;
    QuestionDaoImpl questionDao;

    @AfterEach
    void tearDown() {
        if (mockedStatic != null) {
            mockedStatic.close();
            mockedStatic = null;
        }

    }

    @Test
    void testLoadQuestions() {
        String resourceName = "questions.csv";
        List<String> questionLines = List.of(
                "Question,AnswerType,Answer,AnswerOptions",
                "What is 2+2?,0,4",
                "",
                "Choose colors,1,1,Red;Green;Blue"
        );
        mockedStatic = mockStatic(ResourceUtils.class);
        mockedStatic.when(() -> ResourceUtils.readResourceAsLines(resourceName)).thenReturn(questionLines);
        questionDao = new QuestionDaoImpl(resourceName);


        List<Question> questions = questionDao.loadQuestions();


        assertEquals(2, questions.size());
        assertEquals("What is 2+2?", questions.get(0).getText());
        assertEquals("Choose colors", questions.get(1).getText());
    }

    @Test
    void testLoadQuestionsBad() {
        questionDao = new QuestionDaoImpl("bad.csv");


        Exception ex = assertThrows(UncheckedIOException.class, questionDao::loadQuestions);


        assertTrue(ex.getMessage().contains("Failed to read resource:"));
    }


}