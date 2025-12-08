package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.toOptionalOrThrow() method without arguments
 */
public class ResultToOptionalOrThrowNoArgTest {

    @Test
    void toOptionalOrThrow_OkWithStringValue_ShouldReturnOptionalWithValue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When
        Optional<String> result = okResult.toOptionalOrThrow();

        // Then
        assertTrue(result.isPresent());
        assertEquals("success", result.get());
    }

    @Test
    void toOptionalOrThrow_OkWithIntegerValue_ShouldReturnOptionalWithValue() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);

        // When
        Optional<Integer> result = okResult.toOptionalOrThrow();

        // Then
        assertTrue(result.isPresent());
        assertEquals(42, result.get().intValue());
    }

    @Test
    void toOptionalOrThrow_OkWithNullValue_ShouldReturnOptionalWithNull() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);

        // When
        assertThrows(NullPointerException.class,
                () -> {
                    okResult.toOptionalOrThrow();
                });

    }

    @Test
    void toOptionalOrThrow_Err_ShouldThrowIllegalStateException() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                errResult::toOptionalOrThrow);
        assertTrue(exception.getMessage().contains("Cannot convert Err to Optional"));
        assertTrue(exception.getMessage().contains("404"));
    }


    @Test
    void toOptionalOrThrow_WithStringErrors_ShouldIncludeErrorInMessage() {
        // Given
        Result<String, String> errResult = Result.err("database connection failed");

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                errResult::toOptionalOrThrow);
        assertTrue(exception.getMessage().contains("database connection failed"));
    }

    @Test
    void toOptionalOrThrow_AfterMapOperations_ShouldWork() {
        // Given
        Result<Integer, String> original = Result.ok(5);

        // When
        Result<String, String> mapped = original.map(n -> "Number: " + n);
        Optional<String> result = mapped.toOptionalOrThrow();

        // Then
        assertTrue(result.isPresent());
        assertEquals("Number: 5", result.get());
    }

    @Test
    void toOptionalOrThrow_AfterOperationsOnErr_ShouldThrow() {
        // Given
        Result<Integer, String> original = Result.err("original error");

        // When
        Result<String, String> mapped = original.map(n -> "Number: " + n);

        // Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                mapped::toOptionalOrThrow);
        assertTrue(exception.getMessage().contains("original error"));
    }

    @Test
    void toOptionalOrThrow_WithListValues_ShouldWork() {
        // Given
        java.util.List<String> expectedList = java.util.List.of("a", "b");
        Result<java.util.List<String>, Integer> okResult = Result.ok(expectedList);

        // When
        Optional<java.util.List<String>> result = okResult.toOptionalOrThrow();

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedList, result.get());
    }
}