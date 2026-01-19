package ru.diasoft.otus.application01.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceUtils {

    public static List<String> readResourceAsLines(String path) {
        try (InputStream stream = getResourceStream(path)) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream, StandardCharsets.UTF_8))) {

                return reader.lines()
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read resource: " + path, e);
        }
    }

    private static InputStream getResourceStream(String path) throws IOException {
        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);
        if (stream == null) {
            throw new IOException("Resource not found: " + path);
        }
        return stream;
    }

    public static String readResourceAsString(String path) {
        try (InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path)) {

            if (stream == null) {
                throw new IOException("Resource not found: " + path);
            }

            return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read resource: " + path, e);
        }
    }

    public static List<List<String>> readCsvResource(String path) {
        return readResourceAsLines(path).stream()
                .filter(trim -> !trim.isBlank())
                .map(String::trim)
                .map(line -> Arrays.asList(line.split(",")))
                .collect(Collectors.toList());
    }

}