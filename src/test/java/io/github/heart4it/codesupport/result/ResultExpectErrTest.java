package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.expectErr() method
 */
public class ResultExpectErrTest {

    @Test
    void expectErr_ErrWithStringError_ShouldReturnError() {
        // Given
        Result<String, String> errResult = Result.err("error message");
        String message = "Should not fail";

        // When
        String result = errResult.expectErr(message);

        // Then
        assertEquals("error message", result);
    }

    @Test
    void expectErr_ErrWithIntegerError_ShouldReturnError() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        String message = "Should not fail";

        // When
        Integer result = errResult.expectErr(message);

        // Then
        assertEquals(404, result);
    }

    @Test
    void expectErr_ErrWithNullError_ShouldReturnNull() {
        // Given
        Result<String, String> errResult = Result.err(null);
        String message = "Should not fail";

        // When
        String result = errResult.expectErr(message);

        // Then
        assertNull(result);
    }

    @Test
    void expectErr_Ok_ShouldThrowNoSuchElementExceptionWithCustomMessage() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");
        String expectedMessage = "Expected error but got success";

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> okResult.expectErr(expectedMessage));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void expectErr_AfterMapErrOperation_ShouldReturnTransformedError() {
        // Given
        Result<String, Integer> original = Result.err(404);
        String message = "Should not fail";

        // When
        Result<String, String> mapped = original.mapErr(e -> "HTTP " + e);
        String result = mapped.expectErr(message);

        // Then
        assertEquals("HTTP 404", result);
    }

    @Test
    void expectErr_AfterOperationsOnOk_ShouldThrow() {
        // Given
        Result<Integer, String> original = Result.ok(42);
        String message = "This should fail";

        // When
        Result<String, String> mapped = original.map(n -> "Value: " + n);

        // Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> mapped.expectErr(message));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void expectErr_WithOk_ShouldUseProvidedMessage() {
        // Given
        Result<String, Integer> okResult = Result.ok("data loaded successfully");
        String userFriendlyMessage = "Expected an error but operation succeeded";

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> okResult.expectErr(userFriendlyMessage));
        assertEquals(userFriendlyMessage, exception.getMessage());
    }

    @Test
    void expectErr_WithListErrors_ShouldWork() {
        // Given
        java.util.List<String> expectedErrors = java.util.List.of("field1 required", "field2 invalid");
        Result<String, java.util.List<String>> errResult = Result.err(expectedErrors);
        String message = "Should not fail";

        // When
        java.util.List<String> result = errResult.expectErr(message);

        // Then
        assertEquals(expectedErrors, result);
    }

    @Test
    void expectErr_WithEmptyMessage_ShouldWork() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");
        String emptyMessage = "";

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> okResult.expectErr(emptyMessage));
        assertEquals(emptyMessage, exception.getMessage());
    }
}