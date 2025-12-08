package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.unwrapErrUnchecked() method
 */
public class ResultUnwrapErrUncheckedTest {

    @Test
    void unwrapErrUnchecked_ErrWithStringError_ShouldReturnError() {
        // Given
        Result<String, String> errResult = Result.err("error message");

        // When
        String result = errResult.unwrapErrUnchecked();

        // Then
        assertEquals("error message", result);
    }

    @Test
    void unwrapErrUnchecked_ErrWithIntegerError_ShouldReturnError() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When
        Integer result = errResult.unwrapErrUnchecked();

        // Then
        assertEquals(404, result);
    }

    @Test
    void unwrapErrUnchecked_ErrWithNullError_ShouldReturnNull() {
        // Given
        Result<String, String> errResult = Result.err(null);

        // When
        String result = errResult.unwrapErrUnchecked();

        // Then
        assertNull(result);
    }

    @Test
    void unwrapErrUnchecked_Ok_ShouldReturnNull() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When
        Integer result = okResult.unwrapErrUnchecked();

        // Then
        assertNull(result);
    }

    @Test
    void unwrapErrUnchecked_WithOk_ShouldNotThrowException() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When & Then
        assertDoesNotThrow(okResult::unwrapErrUnchecked);
    }


    @Test
    void unwrapErrUnchecked_WithListErrors_ShouldWork() {
        // Given
        java.util.List<String> expectedErrors = java.util.List.of("err1", "err2");
        Result<String, java.util.List<String>> errResult = Result.err(expectedErrors);

        // When
        java.util.List<String> result = errResult.unwrapErrUnchecked();

        // Then
        assertEquals(expectedErrors, result);
    }

    @Test
    void unwrapErrUnchecked_WithBooleanErrors_ShouldWork() {
        // Given
        Result<String, Boolean> errResult = Result.err(false);

        // When
        Boolean result = errResult.unwrapErrUnchecked();

        // Then
        assertFalse(result);
    }

    @Test
    void unwrapErrUnchecked_AfterErrorTransformations_ShouldWork() {
        // Given
        Result<String, Integer> original = Result.err(500);

        // When
        Result<String, String> mapped = original.mapErr(e -> "Status: " + e);
        String result = mapped.unwrapErrUnchecked();

        // Then
        assertEquals("Status: 500", result);
    }
}