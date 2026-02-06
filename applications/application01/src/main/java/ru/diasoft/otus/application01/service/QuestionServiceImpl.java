package ru.diasoft.otus.application01.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.diasoft.otus.application01.dao.QuestionDao;
import ru.diasoft.otus.application01.domain.Question;
import ru.diasoft.otus.application01.io.IOService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final int countPasses;
    private final QuestionDao questionDao;
    private final IOService ioService;

    public QuestionServiceImpl(@Value("${countPasses:2}") int countPasses,
                               QuestionDao questionDao,
                               IOService ioService) {
        this.countPasses = countPasses;
        this.questionDao = questionDao;
        this.ioService = ioService;
    }

    @Override
    public void procQuestion() {
        List<Question> questions = questionDao.loadQuestions();
        ioService.printf("Take the %s questions test.\n", questions.size());
        ioService.print("Enter first name: ");
        String nameStudent = ioService.readLine().trim();
        ioService.print("Enter last name: ");
        nameStudent = nameStudent + " " + ioService.readLine().trim();
        ioService.printf("Student %s, answer the following questions:\n", nameStudent);

        AtomicInteger number = new AtomicInteger(1);
        AtomicInteger currPassed = new AtomicInteger();
        questions.forEach(question -> {
                    String answerOptions = "";
                    String answerType = "";
                    switch (question.getType()) {
                        case NUMBER:
                            answerType = "type a number";
                            break;
                        case OPTIONS:
                            answerType = "type the option number 1.." + question.getAnswerOptions().size();
                            answerOptions = ". Answer options: [" + String.join("; ", question.getAnswerOptions()) + "]";
                            break;
                        case TEXT:
                            answerType = "type the text";
                    }
                    ioService.printf("Question %d: '%s'%s. Your answer (%s): ", number.getAndIncrement(), question.getText(), answerOptions, answerType);
                    String studentAnswer = ioService.readLine().trim();
                    if (question.getAnswer().equals(studentAnswer)) {
                        currPassed.getAndIncrement();
                    }
                }
        );
        String testResult = "failed";
        if (currPassed.get() >= countPasses) {
            testResult = "passed successfully";
        }
        ioService.printf("Test %s (%d correct answers)\n", testResult, currPassed.get());
    }

}
