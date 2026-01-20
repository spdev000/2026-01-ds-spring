package ru.diasoft.otus.application01.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import ru.diasoft.otus.application01.util.ResourceUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionServiceImplTest {

    private MockedStatic<ResourceUtils> mockedStatic;

    @AfterEach
    void tearDown() {
        if (mockedStatic != null) {
            mockedStatic.close();
            mockedStatic = null;
        }
    }

    @Test
    void procQuestion_printsOptionsAndSingleAnswer() {
        List<String> lines = List.of(
                "Question,AnswerType,Answer",
                "What is 2+2?,0,4",
                "Choose colors,1,Red; Green; Blue"
        );

        mockedStatic = mockStatic(ResourceUtils.class);
        mockedStatic.when(() -> ResourceUtils.readResourceAsLines("questions.csv")).thenReturn(lines);

        QuestionServiceImpl svc = new QuestionServiceImpl();
        svc.setQuestionsFileName("questions.csv");

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(out));

        svc.procQuestion();

        System.setOut(originalOut);
        String output = out.toString();

        assertTrue(output.contains("Question 1: 'What is 2+2?', answer: 4"));
        assertTrue(output.contains("Question 2: 'Choose colors', answer: options (Red; Green; Blue)"));
    }

    @Test
    void procQuestion_throwsOnInvalidCsvLine() {
        List<String> lines = List.of(
                "header",
                "invalid_line_without_commas"
        );

        mockedStatic = mockStatic(ResourceUtils.class);
        mockedStatic.when(() -> ResourceUtils.readResourceAsLines("bad.csv")).thenReturn(lines);

        QuestionServiceImpl svc = new QuestionServiceImpl();
        svc.setQuestionsFileName("bad.csv");

        Exception ex = assertThrows(IllegalArgumentException.class, svc::procQuestion);
        assertTrue(ex.getMessage().contains("Invalid CSV string"));
    }
}
