package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for Result.isOk() method
 */
public class ResultIsOkTest {

    @Test
    void isOk_WithOkStringResult_ShouldReturnTrue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When
        boolean result = okResult.isOk();

        // Then
        assertTrue(result);
    }

    @Test
    void isOk_WithOkIntegerResult_ShouldReturnTrue() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);

        // When
        boolean result = okResult.isOk();

        // Then
        assertTrue(result);
    }

    @Test
    void isOk_WithOkNullResult_ShouldReturnTrue() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);

        // When
        boolean result = okResult.isOk();

        // Then
        assertTrue(result);
    }

    @Test
    void isOk_WithErrStringResult_ShouldReturnFalse() {
        // Given
        Result<String, String> errResult = Result.err("error");

        // When
        boolean result = errResult.isOk();

        // Then
        assertFalse(result);
    }

    @Test
    void isOk_WithErrIntegerResult_ShouldReturnFalse() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When
        boolean result = errResult.isOk();

        // Then
        assertFalse(result);
    }

    @Test
    void isOk_WithErrNullResult_ShouldReturnFalse() {
        // Given
        Result<String, String> errResult = Result.err(null);

        // When
        boolean result = errResult.isOk();

        // Then
        assertFalse(result);
    }

    @Test
    void isOk_AfterMapOperationOnOk_ShouldReturnTrue() {
        // Given
        Result<String, Integer> originalResult = Result.ok("hello");

        // When
        Result<String, Integer> mappedResult = originalResult.map(String::toUpperCase);
        boolean result = mappedResult.isOk();

        // Then
        assertTrue(result);
    }

    @Test
    void isOk_AfterMapOperationOnErr_ShouldReturnFalse() {
        // Given
        Result<String, Integer> originalResult = Result.err(404);

        // When
        Result<String, Integer> mappedResult = originalResult.map(String::toUpperCase);
        boolean result = mappedResult.isOk();

        // Then
        assertFalse(result);
    }

    @Test
    void isOk_AfterAndThenOperationOnOk_ShouldReturnTrue() {
        // Given
        Result<String, Integer> originalResult = Result.ok("hello");

        // When
        Result<String, Integer> chainedResult = originalResult.andThen(s -> Result.ok(s + " world"));
        boolean result = chainedResult.isOk();

        // Then
        assertTrue(result);
    }

    @Test
    void isOk_AfterErrorTransformation_ShouldReturnFalse() {
        // Given
        Result<String, Integer> originalResult = Result.err(404);

        // When
        Result<String, String> transformedResult = originalResult.mapErr(e -> "Error: " + e);
        boolean result = transformedResult.isOk();

        // Then
        assertFalse(result);
    }
}