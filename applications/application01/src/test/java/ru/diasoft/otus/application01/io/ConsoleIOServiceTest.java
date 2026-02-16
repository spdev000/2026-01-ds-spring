package ru.diasoft.otus.application01.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleIOServiceTest {

    IOService ioService;

    @Test
    void print() {
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(testOut));

        ioService = new ConsoleIOService();
        ioService.print("test");

        System.setOut(originalOut);
        String output = testOut.toString();
        assertEquals("test", output);
    }

    @Test
    void println() {
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(testOut));

        ioService = new ConsoleIOService();
        ioService.println("test");

        System.setOut(originalOut);
        String output = testOut.toString();
        assertEquals("test\r\n", output);
    }

    @Test
    void printf() {
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(testOut));

        ioService = new ConsoleIOService();
        ioService.printf("test %s", "test");

        System.setOut(originalOut);
        String output = testOut.toString();
        assertEquals("test test", output);
        //testOut.close();
    }

    @Test
    void readLine() {
        String inputLine = "test ioService.readLine()";
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputLine.getBytes(StandardCharsets.UTF_8));
        InputStream originalIn = System.in;
        System.setIn(testIn);


        ioService = new ConsoleIOService();
        String readLine = ioService.readLine();


        System.setIn(originalIn);
        assertEquals("test ioService.readLine()", readLine);
    }
}