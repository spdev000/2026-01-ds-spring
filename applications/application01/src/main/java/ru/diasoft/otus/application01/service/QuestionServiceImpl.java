package ru.diasoft.otus.application01.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.diasoft.otus.application01.dao.QuestionDao;
import ru.diasoft.otus.application01.domain.Question;
import ru.diasoft.otus.application01.io.IOService;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final int countPasses;
    private final QuestionDao questionDao;
    private final IOService ioService;
    private final MessageSource messageSource;

    public QuestionServiceImpl(@Value("${countPasses:2}") int countPasses,
                               QuestionDao questionDao,
                               IOService ioService, MessageSource messageSource) {
        this.countPasses = countPasses;
        this.questionDao = questionDao;
        this.ioService = ioService;
        this.messageSource = messageSource;
    }

    @Override
    public void procQuestion() {
        List<Question> questions = questionDao.loadQuestions();
        ioService.printf(messageSource.getMessage("QuestionService.welcome", null, "Take the %s questions test.\n", Locale.getDefault()), questions.size());
        ioService.print(messageSource.getMessage("QuestionService.first", null,"Enter first name: ", Locale.getDefault()));
        String nameStudent = ioService.readLine().trim();
        ioService.print(messageSource.getMessage("QuestionService.last", null,"Enter last name: ", Locale.getDefault()));
        nameStudent = (nameStudent + " " + ioService.readLine().trim()).trim();
        ioService.printf(messageSource.getMessage("QuestionService.student",null, "Student %s, answer the following questions:\n", Locale.getDefault()), nameStudent);

        AtomicInteger number = new AtomicInteger(1);
        AtomicInteger currPassed = new AtomicInteger();
        questions.forEach(question -> {
                    String answerOptions = "";
                    String answerType = "";
                    switch (question.getType()) {
                        case NUMBER:
                            answerType = messageSource.getMessage("QuestionService.typenumber",null, "type a number", Locale.getDefault());
                            break;
                        case OPTIONS:
                            answerType = messageSource.getMessage("QuestionService.typeoptionnumber",null, "type the option number 1..", Locale.getDefault()) + question.getAnswerOptions().size();
                            answerOptions = messageSource.getMessage("QuestionService.answeroptions",null, ". Answer options: [", Locale.getDefault()) + String.join("; ", question.getAnswerOptions()) + "]";
                            break;
                        case TEXT:
                            answerType = messageSource.getMessage("QuestionService.typetext",null, "type the text", Locale.getDefault());
                    }
                    ioService.printf(messageSource.getMessage("QuestionService.question",null, "Question %d: '%s'%s. Your answer (%s): ", Locale.getDefault()), number.getAndIncrement(), question.getText(), answerOptions, answerType);
                    String studentAnswer = ioService.readLine().trim();
                    if (question.getAnswer().equals(studentAnswer)) {
                        currPassed.getAndIncrement();
                    }
                }
        );
        String testResult = messageSource.getMessage("QuestionService.failed",null, "failed", Locale.getDefault());
        if (currPassed.get() >= countPasses) {
            testResult = messageSource.getMessage("QuestionService.passed",null, "passed successfully", Locale.getDefault());
        }
        ioService.printf(messageSource.getMessage("QuestionService.test",null, "Test %s (%d correct answers)\n", Locale.getDefault()), testResult, currPassed.get());
    }

}
