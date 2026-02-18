package ru.diasoft.otus.application01;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.diasoft.otus.application01.service.QuestionService;

@SpringBootApplication
public class Application01Application {

    public static void main(String[] args) {
        SpringApplication.run(Application01Application.class, args);
    }

    @Bean
    public CommandLineRunner run(QuestionService service) {
        return args -> {
            service.procQuestion();
        };
    }

}
