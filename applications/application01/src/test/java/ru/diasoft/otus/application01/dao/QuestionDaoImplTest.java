package ru.diasoft.otus.application01.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.diasoft.otus.application01.domain.Question;
import ru.diasoft.otus.application01.util.ResourceUtils;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionDaoImplTest {

    private MockedStatic<ResourceUtils> mockedStatic;

    @Mock
    MessageSource messageSource;

    @InjectMocks
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
        when(messageSource.getMessage(resourceName, null, resourceName, Locale.getDefault())).thenReturn(resourceName);
        questionDao = new QuestionDaoImpl(resourceName, messageSource);


        List<Question> questions = questionDao.loadQuestions();


        assertEquals(2, questions.size());
        assertEquals("What is 2+2?", questions.get(0).getText());
        assertEquals("Choose colors", questions.get(1).getText());
    }

    @Test
    void testLoadQuestionsBad() {
        when(messageSource.getMessage("bad.csv", null, "bad.csv", Locale.getDefault())).thenReturn("bad.csv");
        questionDao = new QuestionDaoImpl("bad.csv", messageSource);


        Exception ex = assertThrows(UncheckedIOException.class, questionDao::loadQuestions);


        assertTrue(ex.getMessage().contains("Failed to read resource:"));
    }


}