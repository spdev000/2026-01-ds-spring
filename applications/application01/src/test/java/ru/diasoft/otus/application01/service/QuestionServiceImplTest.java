package ru.diasoft.otus.application01.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import ru.diasoft.otus.application01.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
                "What is 2+2?,0,4,4",
                "Choose colors,1,1,Red;Green;Blue"
        );
        List<String> studentAnswers = List.of(
                "Vasya",
                "Pupkin",
                "4",
                "1"
        );

        mockedStatic = mockStatic(ResourceUtils.class);
        mockedStatic.when(() -> ResourceUtils.readResourceAsLines("questions.csv")).thenReturn(lines);

        QuestionServiceImpl svc = new QuestionServiceImpl("questions.csv", 1);

        String input = String.join("\n", studentAnswers);
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        InputStream originalIn = System.in;
        System.setIn(new BufferedInputStream(testIn));

        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(testOut));


        svc.procQuestion();


        System.setOut(originalOut);
        System.setIn(originalIn);
        String output = testOut.toString();
        assertTrue(output.contains("Question 1: 'What is 2+2?'. Your answer:"));
        assertTrue(output.contains("Question 2: 'Choose colors'. Answer options: [Red; Green; Blue], enter the option number. Your answer: Test passed successfully (2 correct answers)"));
    }

    @Test
    void procQuestion_throwsOnInvalidCsvLine() {
        List<String> lines = List.of(
                "header",
                "invalid_line_without_commas"
        );

        mockedStatic = mockStatic(ResourceUtils.class);
        mockedStatic.when(() -> ResourceUtils.readResourceAsLines("bad.csv")).thenReturn(lines);

        QuestionServiceImpl svc = new QuestionServiceImpl("bad.csv", 3);

        Exception ex = assertThrows(IllegalArgumentException.class, svc::procQuestion);
        assertTrue(ex.getMessage().contains("Invalid CSV string"));
    }
}
