package ru.diasoft.otus.application01;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.diasoft.otus.application01.service.QuestionService;

public class Application01 {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        QuestionService service = context.getBean(QuestionService.class);
        service.procQuestion();

    }

}
