package ru.diasoft.otus.application01.io;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleIOService implements IOService {

    private final Scanner scanner;

    public ConsoleIOService() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void print(String text) {
        System.out.print(text);
    }

    @Override
    public void println(String text) {
        System.out.println(text);
    }

    @Override
    public void printf(String text, Object... args) {
        System.out.printf(text, args);

    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

}
