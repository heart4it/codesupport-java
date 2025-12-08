package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.unwrapUnchecked() method
 */
public class ResultUnwrapUncheckedTest {

    @Test
    void unwrapUnchecked_OkWithStringValue_ShouldReturnValue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When
        String result = okResult.unwrapUnchecked();

        // Then
        assertEquals("success", result);
    }

    @Test
    void unwrapUnchecked_OkWithIntegerValue_ShouldReturnValue() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);

        // When
        Integer result = okResult.unwrapUnchecked();

        // Then
        assertEquals(42, result);
    }

    @Test
    void unwrapUnchecked_OkWithNullValue_ShouldReturnNull() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);

        // When
        String result = okResult.unwrapUnchecked();

        // Then
        assertNull(result);
    }

    @Test
    void unwrapUnchecked_Err_ShouldReturnNull() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When
        String result = errResult.unwrapUnchecked();

        // Then
        assertNull(result);
    }

    @Test
    void unwrapUnchecked_WithErr_ShouldNotThrowException() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When & Then
        assertDoesNotThrow(errResult::unwrapUnchecked);
    }

    @Test
    void unwrapUnchecked_WithBooleanValues_ShouldWork() {
        // Given
        Result<Boolean, String> okResult = Result.ok(true);

        // When
        Boolean result = okResult.unwrapUnchecked();

        // Then
        assertTrue(result);
    }

    @Test
    void unwrapUnchecked_WithListValues_ShouldWork() {
        // Given
        java.util.List<String> expectedList = java.util.List.of("a", "b");
        Result<java.util.List<String>, Integer> okResult = Result.ok(expectedList);

        // When
        java.util.List<String> result = okResult.unwrapUnchecked();

        // Then
        assertEquals(expectedList, result);
    }

    @Test
    void unwrapUnchecked_WithErrAndListValues_ShouldReturnNull() {
        // Given
        Result<java.util.List<String>, Integer> errResult = Result.err(404);

        // When
        java.util.List<String> result = errResult.unwrapUnchecked();

        // Then
        assertNull(result);
    }
}