package ru.diasoft.otus.application01.util;

import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ResourceUtilsTest {

    @Test
    void readResourceAsLines_shouldReadExistingFile() {
        List<String> lines = ResourceUtils.readResourceAsLines("answers.csv");
        
        assertThat(lines)
                .isNotNull()
                .hasSize(5)
                .containsExactly("Vasya", "Pupkin", "4", "1", "Paris");
    }

    @Test
    void readResourceAsLines_shouldThrowExceptionForNonExistentFile() {
        UncheckedIOException exception = assertThrows(
                UncheckedIOException.class,
                () -> ResourceUtils.readResourceAsLines("non-existent-file.txt")
        );
        
        assertTrue(exception.getMessage().contains("Failed to read resource"));
        assertTrue(exception.getMessage().contains("non-existent-file.txt"));
    }

}