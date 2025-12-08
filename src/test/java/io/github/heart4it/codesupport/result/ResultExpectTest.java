package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.expect() method
 */
public class ResultExpectTest {

    @Test
    void expect_OkWithStringValue_ShouldReturnValue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");
        String message = "Should not fail";

        // When
        String result = okResult.expect(message);

        // Then
        assertEquals("success", result);
    }

    @Test
    void expect_OkWithIntegerValue_ShouldReturnValue() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);
        String message = "Should not fail";

        // When
        Integer result = okResult.expect(message);

        // Then
        assertEquals(42, result);
    }

    @Test
    void expect_OkWithNullValue_ShouldReturnNull() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);
        String message = "Should not fail";

        // When
        String result = okResult.expect(message);

        // Then
        assertNull(result);
    }

    @Test
    void expect_Err_ShouldThrowNoSuchElementExceptionWithCustomMessage() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        String expectedMessage = "Custom error message";

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> errResult.expect(expectedMessage));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void expect_AfterMapOperation_ShouldReturnTransformedValue() {
        // Given
        Result<Integer, String> original = Result.ok(5);
        String message = "Mapping should not fail";

        // When
        Result<String, String> mapped = original.map(n -> "Number: " + n);
        String result = mapped.expect(message);

        // Then
        assertEquals("Number: 5", result);
    }

    @Test
    void expect_AfterMapOperationOnErr_ShouldThrow() {
        // Given
        Result<Integer, String> original = Result.err("original error");
        String message = "This should fail";

        // When
        Result<String, String> mapped = original.map(n -> "Number: " + n);

        // Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> mapped.expect(message));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void expect_WithListValue_ShouldReturnList() {
        // Given
        java.util.List<String> expectedList = java.util.List.of("a", "b", "c");
        Result<java.util.List<String>, Integer> okResult = Result.ok(expectedList);
        String message = "Should not fail";

        // When
        java.util.List<String> result = okResult.expect(message);

        // Then
        assertEquals(expectedList, result);
    }

    @Test
    void expect_WithErr_ShouldUseProvidedMessage() {
        // Given
        Result<String, Integer> errResult = Result.err(500);
        String userFriendlyMessage = "Failed to load user data";

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> errResult.expect(userFriendlyMessage));
        assertEquals(userFriendlyMessage, exception.getMessage());
    }

    @Test
    void expect_WithEmptyMessage_ShouldWork() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        String emptyMessage = "";

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> errResult.expect(emptyMessage));
        assertEquals(emptyMessage, exception.getMessage());
    }
}