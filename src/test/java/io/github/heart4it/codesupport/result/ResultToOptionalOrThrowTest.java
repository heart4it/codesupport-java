package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.toOptionalOrThrow() method with exception supplier
 */
public class ResultToOptionalOrThrowTest {

    @Test
    void toOptionalOrThrow_OkWithStringValue_ShouldReturnOptionalWithValue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");
        Function<Integer, RuntimeException> exceptionSupplier = error ->
                new RuntimeException("Error: " + error);

        // When
        Optional<String> result = okResult.toOptionalOrThrow(exceptionSupplier);

        // Then
        assertTrue(result.isPresent());
        assertEquals("success", result.get());
    }

    @Test
    void toOptionalOrThrow_Err_ShouldThrowExceptionFromSupplier() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        Function<Integer, RuntimeException> exceptionSupplier = error ->
                new RuntimeException("HTTP Error: " + error);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> errResult.toOptionalOrThrow(exceptionSupplier));
        assertEquals("HTTP Error: 404", exception.getMessage());
    }

    @Test
    void toOptionalOrThrow_WithCustomExceptionTypes_ShouldWork() {
        // Given
        Result<String, String> errResult = Result.err("validation failed");
        Function<String, IllegalArgumentException> exceptionSupplier =
                IllegalArgumentException::new;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> errResult.toOptionalOrThrow(exceptionSupplier));
        assertEquals("validation failed", exception.getMessage());
    }

    @Test
    void toOptionalOrThrow_WithNullValue_ShouldReturnEmptyOptional() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);
        Function<Integer, RuntimeException> exceptionSupplier =
                error -> new RuntimeException("Error: " + error);

        // When
        Optional<String> result = okResult.toOptionalOrThrow(exceptionSupplier);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void toOptionalOrThrow_WithNonNullValue_ShouldReturnPresentOptional() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");
        Function<Integer, RuntimeException> exceptionSupplier =
                error -> new RuntimeException("Error: " + error);

        // When
        Optional<String> result = okResult.toOptionalOrThrow(exceptionSupplier);

        // Then
        assertTrue(result.isPresent());
        assertEquals("success", result.get());
    }

    @Test
    void toOptionalOrThrow_WithErrValue_ShouldThrowException() {
        // Given
        Result<String, String> result = Result.err("error");
        Function<String, RuntimeException> exceptionSupplier =
                error -> new RuntimeException("Error: " + error);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> result.toOptionalOrThrow(exceptionSupplier));
        assertEquals("Error: error", exception.getMessage());
    }

    @Test
    void toOptionalOrThrow_WithCustomException_ShouldThrowCustomException() {
        // Given
        Result<String, Integer> result = Result.err(404);
        Function<Integer, IllegalStateException> exceptionSupplier =
                error -> new IllegalStateException("Status: " + error);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> result.toOptionalOrThrow(exceptionSupplier));
        assertEquals("Status: 404", exception.getMessage());
    }

    @Test
    void toOptionalOrThrow_WithNullError_ShouldHandleNullError() {
        // Given
        Result<String, String> result = Result.err(null);
        Function<String, RuntimeException> exceptionSupplier =
                error -> new RuntimeException("Error: " + error);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> result.toOptionalOrThrow(exceptionSupplier));
        assertEquals("Error: null", exception.getMessage());
    }

    @Test
    void toOptionalOrThrow_WithIntegerValues_ShouldWork() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);
        Function<String, RuntimeException> exceptionSupplier =
                error -> new RuntimeException(error);

        // When
        Optional<Integer> result = okResult.toOptionalOrThrow(exceptionSupplier);

        // Then
        assertTrue(result.isPresent());
        assertEquals(42, result.get().intValue());
    }


    @Test
    void toOptionalOrThrow_AfterMapOperations_ShouldWork() {
        // Given
        Result<Integer, String> original = Result.ok(10);
        Function<String, RuntimeException> exceptionSupplier =
                error -> new RuntimeException(error);

        // When
        Result<String, String> mapped = original.map(n -> "Value: " + n);
        Optional<String> result = mapped.toOptionalOrThrow(exceptionSupplier);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Value: 10", result.get());
    }

    @Test
    void toOptionalOrThrow_AfterOperationsOnErr_ShouldThrow() {
        // Given
        Result<Integer, String> original = Result.err("original error");
        Function<String, RuntimeException> exceptionSupplier =
                error -> new RuntimeException("Mapped: " + error);

        // When
        Result<String, String> mapped = original.map(n -> "Value: " + n);

        // Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> mapped.toOptionalOrThrow(exceptionSupplier));
        assertEquals("Mapped: original error", exception.getMessage());
    }
}