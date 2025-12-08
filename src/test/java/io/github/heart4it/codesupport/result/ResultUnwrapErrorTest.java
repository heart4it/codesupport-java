package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.unwrapError() method
 */
public class ResultUnwrapErrorTest {

    @Test
    void unwrapError_ErrWithStringError_ShouldReturnError() {
        // Given
        Result<String, String> errResult = Result.err("error message");

        // When
        String result = errResult.unwrapError();

        // Then
        assertEquals("error message", result);
    }

    @Test
    void unwrapError_ErrWithIntegerError_ShouldReturnError() {
        // Given
        Result<String, Integer> errResult = Result.err(500);

        // When
        Integer result = errResult.unwrapError();

        // Then
        assertEquals(500, result);
    }

    @Test
    void unwrapError_ShouldBeEquivalentToUnwrapErr() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When
        Integer viaUnwrapError = errResult.unwrapError();
        Integer viaUnwrapErr = errResult.unwrapErr();

        // Then
        assertEquals(viaUnwrapError, viaUnwrapErr);
    }

    @Test
    void unwrapError_ErrWithNullError_ShouldReturnNull() {
        // Given
        Result<String, String> errResult = Result.err(null);

        // When
        String result = errResult.unwrapError();

        // Then
        assertNull(result);
    }

    @Test
    void unwrapError_Ok_ShouldThrowNoSuchElementException() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When & Then
        assertThrows(NoSuchElementException.class, okResult::unwrapError);
    }

    @Test
    void unwrapError_AfterMapErrOperation_ShouldReturnTransformedError() {
        // Given
        Result<String, Integer> original = Result.err(404);

        // When
        Result<String, String> mapped = original.mapErr(e -> "HTTP " + e);
        String result = mapped.unwrapError();

        // Then
        assertEquals("HTTP 404", result);
    }

    @Test
    void unwrapError_WithExceptionErrors_ShouldWork() {
        // Given
        RuntimeException expectedException = new RuntimeException("test exception");
        Result<String, RuntimeException> errResult = Result.err(expectedException);

        // When
        RuntimeException result = errResult.unwrapError();

        // Then
        assertEquals(expectedException, result);
    }


    @Test
    void unwrapError_ConsistencyWithUnwrapErr_ShouldBeMaintained() {
        // Given
        Result<String, Integer> errResult = Result.err(503);

        // When
        Integer error1 = errResult.unwrapError();
        Integer error2 = errResult.unwrapErr();

        // Then
        assertEquals(error1, error2);
        assertEquals(503, error1);
    }
}