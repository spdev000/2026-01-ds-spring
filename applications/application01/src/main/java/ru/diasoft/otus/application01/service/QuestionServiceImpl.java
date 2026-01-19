package ru.diasoft.otus.application01.service;

import ru.diasoft.otus.application01.domain.Question;
import ru.diasoft.otus.application01.domain.QuestionType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.diasoft.otus.application01.domain.QuestionType.OPTIONS;
import static ru.diasoft.otus.application01.util.ResourceUtils.readResourceAsLines;

public class QuestionServiceImpl implements QuestionService {

    private String questionsFileName;

    public void setQuestionsFileName(String questionsFileName) {
        this.questionsFileName = questionsFileName;
    }

    public QuestionServiceImpl() {
    }

    @Override
    public void procQuestion() {
        List<Question> questions = loadQuestions(questionsFileName);
        AtomicInteger number = new AtomicInteger(1);
        questions.forEach(question -> {
                    String answer;
                    if (question.getType() == OPTIONS) {
                        answer = "options (" + String.join("; ", question.getAnswers()) + ")";
                    } else
                        answer = question.getAnswers().stream().findFirst().orElse("");
                    System.out.printf("Question %d: '%s', answer: %s \n", number.getAndIncrement(), question.getText(), answer);
                }
        );
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
        String[] parts = line.split(",", 3);
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid CSV string: " + line);
        }

        String text = stripQuotes(parts[0].trim());
        QuestionType type = QuestionType.fromCode(Integer.parseInt(stripQuotes(parts[1].trim())));
        List<String> answers = Arrays.stream(parts[2].split(";"))
                .map(answer -> stripQuotes(answer.trim()))
                .collect(Collectors.toList());

        return new Question(text, type, answers);
    }

    private static String stripQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

}
