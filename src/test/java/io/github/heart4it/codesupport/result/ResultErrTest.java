package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.err() static factory method
 */
public class ResultErrTest {

    @Test
    void err_WithStringError_ShouldCreateErrResult() {
        // Given
        String expectedError = "error message";

        // When
        Result<String, String> result = Result.err(expectedError);

        // Then
        assertTrue(result.isErr());
        assertEquals(expectedError, result.unwrapErr());
    }

    @Test
    void err_WithIntegerError_ShouldCreateErrResult() {
        // Given
        Integer expectedError = 404;

        // When
        Result<String, Integer> result = Result.err(expectedError);

        // Then
        assertTrue(result.isErr());
        assertEquals(expectedError, result.unwrapErr());
    }

    @Test
    void err_WithExceptionError_ShouldCreateErrResult() {
        // Given
        RuntimeException expectedError = new RuntimeException("test error");

        // When
        Result<String, RuntimeException> result = Result.err(expectedError);

        // Then
        assertTrue(result.isErr());
        assertEquals(expectedError, result.unwrapErr());
    }

    @Test
    void err_WithNullError_ShouldCreateErrResultWithNull() {
        // Given
        String nullError = null;

        // When
        Result<String, String> result = Result.err(nullError);

        // Then
        assertTrue(result.isErr());
        assertNull(result.unwrapErr());
    }

    @Test
    void err_WithListOfErrors_ShouldCreateErrResult() {
        // Given
        java.util.List<String> expectedErrors = java.util.List.of("error1", "error2");

        // When
        Result<String, java.util.List<String>> result = Result.err(expectedErrors);

        // Then
        assertTrue(result.isErr());
        assertEquals(expectedErrors, result.unwrapErr());
    }

    @Test
    void err_WithBooleanError_ShouldCreateErrResult() {
        // Given
        Boolean expectedError = true;

        // When
        Result<String, Boolean> result = Result.err(expectedError);

        // Then
        assertTrue(result.isErr());
        assertEquals(expectedError, result.unwrapErr());
    }

    @Test
    void err_WithDoubleError_ShouldCreateErrResult() {
        // Given
        Double expectedError = 3.14;

        // When
        Result<String, Double> result = Result.err(expectedError);

        // Then
        assertTrue(result.isErr());
        assertEquals(expectedError, result.unwrapErr());
    }

    @Test
    void err_WithEmptyStringError_ShouldCreateErrResult() {
        // Given
        String expectedError = "";

        // When
        Result<String, String> result = Result.err(expectedError);

        // Then
        assertTrue(result.isErr());
        assertEquals(expectedError, result.unwrapErr());
    }

    @Test
    void err_WithSameErrorMultipleTimes_ShouldCreateDistinctInstances() {
        // Given
        String sameError = "same error";

        // When
        Result<String, String> result1 = Result.err(sameError);
        Result<String, String> result2 = Result.err(sameError);

        // Then
        assertTrue(result1.isErr());
        assertTrue(result2.isErr());
        assertEquals(result1.unwrapErr(), result2.unwrapErr());
    }
}