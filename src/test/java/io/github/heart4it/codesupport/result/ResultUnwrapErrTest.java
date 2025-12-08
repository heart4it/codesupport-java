package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.unwrapErr() method
 */
public class ResultUnwrapErrTest {

    @Test
    void unwrapErr_ErrWithStringError_ShouldReturnError() {
        // Given
        Result<String, String> errResult = Result.err("error message");

        // When
        String result = errResult.unwrapErr();

        // Then
        assertEquals("error message", result);
    }

    @Test
    void unwrapErr_ErrWithIntegerError_ShouldReturnError() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When
        Integer result = errResult.unwrapErr();

        // Then
        assertEquals(404, result);
    }

    @Test
    void unwrapErr_ErrWithNullError_ShouldReturnNull() {
        // Given
        Result<String, String> errResult = Result.err(null);

        // When
        String result = errResult.unwrapErr();

        // Then
        assertNull(result);
    }

    @Test
    void unwrapErr_Ok_ShouldThrowNoSuchElementException() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When & Then
        assertThrows(NoSuchElementException.class, okResult::unwrapErr);
    }

    @Test
    void unwrapErr_Ok_ShouldThrowWithDescriptiveMessage() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, okResult::unwrapErr);
        assertTrue(exception.getMessage().contains("unwrapErr() on Ok"));
    }

    @Test
    void unwrapErr_AfterMapErrOperation_ShouldReturnTransformedError() {
        // Given
        Result<String, Integer> original = Result.err(404);

        // When
        Result<String, String> mapped = original.mapErr(e -> "Error: " + e);
        String result = mapped.unwrapErr();

        // Then
        assertEquals("Error: 404", result);
    }


    @Test
    void unwrapErr_WithListErrors_ShouldWork() {
        // Given
        java.util.List<String> expectedErrors = java.util.List.of("error1", "error2");
        Result<String, java.util.List<String>> errResult = Result.err(expectedErrors);

        // When
        java.util.List<String> result = errResult.unwrapErr();

        // Then
        assertEquals(expectedErrors, result);
    }

    @Test
    void unwrapErr_WithBooleanErrors_ShouldWork() {
        // Given
        Result<String, Boolean> errResult = Result.err(true);

        // When
        Boolean result = errResult.unwrapErr();

        // Then
        assertTrue(result);
    }

    @Test
    void unwrapErr_WithOkAfterTransformations_ShouldThrow() {
        // Given
        Result<String, Integer> original = Result.ok("success");

        // When
        Result<String, String> mapped = original.mapErr(e -> "Error: " + e);

        // Then
        assertThrows(NoSuchElementException.class, mapped::unwrapErr);
    }
}