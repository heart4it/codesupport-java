package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.inspectErr() method
 */
public class ResultInspectErrTest {

    @Test
    void inspectErr_Err_ShouldCallActionWithErrorAndReturnItself() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        AtomicReference<Integer> capturedError = new AtomicReference<>();
        Consumer<Integer> action = capturedError::set;

        // When
        Result<String, Integer> result = errResult.inspectErr(action);

        // Then
        assertEquals(404, capturedError.get());
        assertEquals(errResult, result);
    }

    @Test
    void inspectErr_Ok_ShouldNotCallActionAndReturnItself() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");
        boolean[] actionCalled = {false};
        Consumer<Integer> action = e -> actionCalled[0] = true;

        // When
        Result<String, Integer> result = okResult.inspectErr(action);

        // Then
        assertFalse(actionCalled[0]);
        assertEquals(okResult, result);
    }

    @Test
    void inspectErr_WithChaining_ShouldAllowFurtherOperations() {
        // Given
        Result<String, Integer> original = Result.err(500);
        AtomicReference<Integer> capturedError = new AtomicReference<>();
        Consumer<Integer> action = capturedError::set;

        // When
        Result<String, String> result = original
                .inspectErr(action)
                .mapErr(e -> "Error: " + e);

        // Then
        assertEquals(500, capturedError.get());
        assertEquals("Error: 500", result.unwrapErr());
    }

    @Test
    void inspectErr_WithNullError_ShouldCallActionWithNull() {
        // Given
        Result<String, String> errResult = Result.err(null);
        AtomicReference<String> capturedError = new AtomicReference<>("initial");
        Consumer<String> action = capturedError::set;

        // When
        Result<String, String> result = errResult.inspectErr(action);

        // Then
        assertNull(capturedError.get());
        assertEquals(errResult, result);
    }

    @Test
    void inspectErr_WithActionThatThrowsException_ShouldPropagateException() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        Consumer<Integer> action = e -> {
            throw new RuntimeException("Error inspection failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> errResult.inspectErr(action));
    }

    @Test
    void inspectErr_WithMultipleInspectErrCalls_ShouldCallAllActions() {
        // Given
        Result<String, Integer> original = Result.err(500);
        StringBuilder log = new StringBuilder();
        Consumer<Integer> action1 = e -> log.append("First: ").append(e).append("; ");
        Consumer<Integer> action2 = e -> log.append("Second: ").append(e * 2).append("; ");

        // When
        Result<String, Integer> result = original
                .inspectErr(action1)
                .inspectErr(action2);

        // Then
        assertEquals("First: 500; Second: 1000; ", log.toString());
        assertEquals(original, result);
    }

    @Test
    void inspectErr_ShouldNotModifyErrorValue() {
        // Given
        Result<String, String> errResult = Result.err("original error");
        Consumer<String> action = s -> { /* No modification */ };

        // When
        Result<String, String> result = errResult.inspectErr(action);

        // Then
        assertEquals("original error", result.unwrapErr());
    }

    @Test
    void inspectErr_WithListErrors_ShouldCallActionWithList() {
        // Given
        java.util.List<String> expectedErrors = java.util.List.of("err1", "err2");
        Result<String, java.util.List<String>> errResult = Result.err(expectedErrors);
        AtomicReference<java.util.List<String>> capturedErrors = new AtomicReference<>();
        Consumer<java.util.List<String>> action = capturedErrors::set;

        // When
        Result<String, java.util.List<String>> result = errResult.inspectErr(action);

        // Then
        assertEquals(expectedErrors, capturedErrors.get());
        assertEquals(errResult, result);
    }

    @Test
    void inspectErr_ForErrorLogging_ShouldWorkAsExpected() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        StringBuilder logOutput = new StringBuilder();
        Consumer<Integer> logger = error -> logOutput.append("Error occurred: ").append(error);

        // When
        Result<String, Integer> result = errResult.inspectErr(logger);

        // Then
        assertEquals("Error occurred: 404", logOutput.toString());
        assertEquals(404, result.unwrapErr());
    }
}