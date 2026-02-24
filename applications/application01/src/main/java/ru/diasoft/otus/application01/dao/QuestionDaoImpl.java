package ru.diasoft.otus.application01.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.diasoft.otus.application01.domain.AnswerType;
import ru.diasoft.otus.application01.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static ru.diasoft.otus.application01.domain.AnswerType.OPTIONS;
import static ru.diasoft.otus.application01.util.ResourceUtils.readResourceAsLines;

@Component
public class QuestionDaoImpl implements QuestionDao {
    private final String questionsFileName;
    private final MessageSource messageSource;

    public QuestionDaoImpl(@Value("${questionsFileName:bad.csv}") String questionsFileName, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.questionsFileName = messageSource.getMessage(questionsFileName, null, questionsFileName, Locale.getDefault());
    }

    private static List<Question> loadQuestions(String resourceName) {
        List<String> questions = readResourceAsLines(resourceName);

        return questions.stream()
                .skip(1) // пропустить заголовок
                .filter(line -> !line.isBlank())
                .map(String::trim)
                .map(QuestionDaoImpl::parseQuestion)
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

    @Override
    public List<Question> loadQuestions() {
        return loadQuestions(questionsFileName);
    }
}
