package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.toOptional() method
 */
public class ResultToOptionalTest {

    @Test
    void toOptional_OkWithStringValue_ShouldReturnOptionalWithValue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When
        Optional<String> result = okResult.toOptional();

        // Then
        assertTrue(result.isPresent());
        assertEquals("success", result.get());
    }

    @Test
    void toOptional_OkWithIntegerValue_ShouldReturnOptionalWithValue() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);

        // When
        Optional<Integer> result = okResult.toOptional();

        // Then
        assertTrue(result.isPresent());
        assertEquals(42, result.get().intValue());
    }

    @Test
    void toOptional_OkWithNullValue_ShouldReturnOptionalWithException() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);

        // When
        assertThrows(NullPointerException.class,
                () -> {
                    okResult.toOptional();
                });

    }

    @Test
    void toOptional_Err_ShouldReturnEmptyOptional() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When
        Optional<String> result = errResult.toOptional();

        // Then
        assertTrue(result.isEmpty());
    }


    @Test
    void toOptional_WithListValues_ShouldWork() {
        // Given
        java.util.List<String> expectedList = java.util.List.of("a", "b", "c");
        Result<java.util.List<String>, Integer> okResult = Result.ok(expectedList);

        // When
        Optional<java.util.List<String>> result = okResult.toOptional();

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedList, result.get());
    }

    @Test
    void toOptional_AfterMapOperations_ShouldReturnOptionalWithTransformedValue() {
        // Given
        Result<Integer, String> original = Result.ok(5);

        // When
        Result<String, String> mapped = original.map(n -> "Number: " + n);
        Optional<String> result = mapped.toOptional();

        // Then
        assertTrue(result.isPresent());
        assertEquals("Number: 5", result.get());
    }

    @Test
    void toOptional_AfterOperationsOnErr_ShouldReturnEmpty() {
        // Given
        Result<Integer, String> original = Result.err("error");

        // When
        Result<String, String> mapped = original.map(n -> "Number: " + n);
        Optional<String> result = mapped.toOptional();

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void toOptional_WithBooleanValues_ShouldWork() {
        // Given
        Result<Boolean, String> okResult = Result.ok(true);

        // When
        Optional<Boolean> result = okResult.toOptional();

        // Then
        assertTrue(result.isPresent());
        assertTrue(result.get());
    }

    @Test
    void toOptional_ShouldAllowFurtherOptionalOperations() {
        // Given
        Result<String, Integer> okResult = Result.ok("hello");

        // When
        Optional<String> optional = okResult.toOptional();
        Optional<String> mapped = optional.map(String::toUpperCase);
        Optional<String> filtered = mapped.filter(s -> s.length() > 3);

        // Then
        assertTrue(filtered.isPresent());
        assertEquals("HELLO", filtered.get());
    }
}