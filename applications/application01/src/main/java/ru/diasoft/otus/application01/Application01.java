package ru.diasoft.otus.application01;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import ru.diasoft.otus.application01.service.QuestionService;

@PropertySource("classpath:application.properties")
@ComponentScan
public class Application01 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application01.class);

        QuestionService service = context.getBean(QuestionService.class);

        service.procQuestion();

    }

}
