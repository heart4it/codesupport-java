package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for Result.toDebugString() method
 */
public class ResultToDebugStringTest {

    @Test
    void toDebugString_OkWithStringValue_ShouldReturnFormattedString() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When
        String result = okResult.toDebugString();

        // Then
        assertEquals("Ok(success)", result);
    }

    @Test
    void toDebugString_OkWithIntegerValue_ShouldReturnFormattedString() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);

        // When
        String result = okResult.toDebugString();

        // Then
        assertEquals("Ok(42)", result);
    }

    @Test
    void toDebugString_OkWithNullValue_ShouldReturnFormattedString() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);

        // When
        String result = okResult.toDebugString();

        // Then
        assertEquals("Ok(null)", result);
    }

    @Test
    void toDebugString_ErrWithStringError_ShouldReturnFormattedString() {
        // Given
        Result<String, String> errResult = Result.err("error message");

        // When
        String result = errResult.toDebugString();

        // Then
        assertEquals("Err(error message)", result);
    }

    @Test
    void toDebugString_ErrWithIntegerError_ShouldReturnFormattedString() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When
        String result = errResult.toDebugString();

        // Then
        assertEquals("Err(404)", result);
    }

    @Test
    void toDebugString_ErrWithNullError_ShouldReturnFormattedString() {
        // Given
        Result<String, String> errResult = Result.err(null);

        // When
        String result = errResult.toDebugString();

        // Then
        assertEquals("Err(null)", result);
    }

    @Test
    void toDebugString_AfterMapOperations_ShouldReflectCurrentState() {
        // Given
        Result<Integer, String> original = Result.ok(10);

        // When
        Result<String, String> mapped = original.map(n -> "Value: " + n);
        String result = mapped.toDebugString();

        // Then
        assertEquals("Ok(Value: 10)", result);
    }

    @Test
    void toDebugString_AfterOperationsOnErr_ShouldReflectErrorState() {
        // Given
        Result<Integer, String> original = Result.err("original error");

        // When
        Result<String, String> mapped = original.map(n -> "Value: " + n);
        String result = mapped.toDebugString();

        // Then
        assertEquals("Err(original error)", result);
    }

    @Test
    void toDebugString_WithListValues_ShouldWork() {
        // Given
        java.util.List<String> list = java.util.List.of("a", "b", "c");
        Result<java.util.List<String>, Integer> okResult = Result.ok(list);

        // When
        String result = okResult.toDebugString();

        // Then
        assertTrue(result.startsWith("Ok("));
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

    @Test
    void toDebugString_WithBooleanValues_ShouldWork() {
        // Given
        Result<Boolean, String> okResult = Result.ok(true);

        // When
        String result = okResult.toDebugString();

        // Then
        assertEquals("Ok(true)", result);
    }
}