package ru.diasoft.otus.application01.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.diasoft.otus.application01.service.QuestionService;

@ShellComponent
@AllArgsConstructor
public class QuestionCommands {

    private final QuestionService service;

    @ShellMethod(value = "Запуск тестирования", key = {"r", "run", "test", "старт"})
    public String runTest() {
        service.procQuestion();
        return null;
    }

}
