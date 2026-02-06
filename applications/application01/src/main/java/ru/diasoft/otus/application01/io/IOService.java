package ru.diasoft.otus.application01.io;

public interface IOService {
    void print(String text);

    void println(String text);

    void printf(String text, Object... args);

    String readLine();

}
