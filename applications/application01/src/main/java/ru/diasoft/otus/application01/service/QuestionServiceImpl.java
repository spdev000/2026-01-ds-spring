package ru.diasoft.otus.application01.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.diasoft.otus.application01.domain.AnswerType;
import ru.diasoft.otus.application01.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.diasoft.otus.application01.domain.AnswerType.NUMBER;
import static ru.diasoft.otus.application01.domain.AnswerType.OPTIONS;
import static ru.diasoft.otus.application01.util.ResourceUtils.readResourceAsLines;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final String questionsFileName;
    private final int countPasses;

    public QuestionServiceImpl(@Value("${questionsFileName:bad.csv}") String questionsFileName,
                               @Value("${countPasses:2}") int countPasses) {
        this.questionsFileName = questionsFileName;
        this.countPasses = countPasses;
    }

    @Override
    public void procQuestion() {
        List<Question> questions = loadQuestions(questionsFileName);
        System.out.println("Take the five-question test.");
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Enter first name: ");
            String nameStudent = input.nextLine().trim();
            System.out.print("Enter last name: ");
            nameStudent = nameStudent + " " + input.nextLine().trim();
            System.out.printf("Student %s, answer the following questions:\n", nameStudent);

            AtomicInteger number = new AtomicInteger(1);
            AtomicInteger currPassed = new AtomicInteger();
            questions.forEach(question -> {
                        String answerOptions = "";
                        String answerType;
                        switch (question.getType()) {
                            case NUMBER:
                                answerType = "type a number";
                                break;
                            case OPTIONS:
                                answerType = "type the option number 1.."+String.valueOf(question.getAnswerOptions().size());
                                answerOptions = ". Answer options: [" + String.join("; ", question.getAnswerOptions()) + "]";
                                break;
                            case TEXT:
                                answerType = "type the text";
                                break;
                            default:
                                answerType = "";
                                break;
                        }
                        ;
                        System.out.printf("Question %d: '%s'%s. Your answer (%s): ", number.getAndIncrement(), question.getText(), answerOptions, answerType);
                        String studentAnswer = input.nextLine().trim();
                        if (question.getAnswer().equals(studentAnswer)) {
                            currPassed.getAndIncrement();
                        }
                    }
            );
            String testResult = "failed";
            if (currPassed.get() >= countPasses) {
                testResult = "passed successfully";
            }
            System.out.printf("Test %s (%d correct answers)\n", testResult, currPassed.get());
        }
    }

    private static List<Question> loadQuestions(String resourceName) {
        List<String> questions = readResourceAsLines(resourceName);

        return questions.stream()
                .skip(1) // пропустить заголовок
                .filter(line -> !line.isBlank())
                .map(String::trim)
                .map(QuestionServiceImpl::parseQuestion)
                .collect(Collectors.toList());
    }

    private static Question parseQuestion(String line) {
        String[] parts = line.split(",");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid CSV string: " + line);
        }

        String text = stripQuotes(parts[0].trim());
        AnswerType type = AnswerType.fromCode(Integer.parseInt(stripQuotes(parts[1].trim())));
        String answer = stripQuotes(parts[2].trim());
        List<String> answerOptions;
        if (type == OPTIONS && parts.length >= 4) {
            answerOptions = Arrays.stream(parts[3].split(";"))
                    .map(answerOption -> stripQuotes(answerOption.trim()))
                    .collect(Collectors.toList());
        } else {
            answerOptions = new ArrayList<>();
        }

        return new Question(text, type, answer, answerOptions);
    }

    private static String stripQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

}
