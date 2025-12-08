package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.or() method
 */
public class ResultOrTest {

    @Test
    void or_OkOrOk_ShouldReturnFirstOk() {
        // Given
        Result<String, Integer> first = Result.ok("first");
        Result<String, Integer> second = Result.ok("second");

        // When
        Result<String, Integer> result = first.or(second);

        // Then
        assertTrue(result.isOk());
        assertEquals("first", result.unwrap());
    }

    @Test
    void or_OkOrErr_ShouldReturnFirstOk() {
        // Given
        Result<String, Integer> first = Result.ok("success");
        Result<String, Integer> second = Result.err(404);

        // When
        Result<String, Integer> result = first.or(second);

        // Then
        assertTrue(result.isOk());
        assertEquals("success", result.unwrap());
    }

    @Test
    void or_ErrOrOk_ShouldReturnSecondOk() {
        // Given
        Result<String, Integer> first = Result.err(500);
        Result<String, Integer> second = Result.ok("recovered");

        // When
        Result<String, Integer> result = first.or(second);

        // Then
        assertTrue(result.isOk());
        assertEquals("recovered", result.unwrap());
    }

    @Test
    void or_ErrOrErr_ShouldReturnSecondErr() {
        // Given
        Result<String, Integer> first = Result.err(500);
        Result<String, Integer> second = Result.err(404);

        // When
        Result<String, Integer> result = first.or(second);

        // Then
        assertTrue(result.isErr());
        assertEquals(404, result.unwrapErr());
    }

    @Test
    void or_WithMultipleChainedOperations_ShouldWork() {
        // Given
        Result<String, Integer> first = Result.err(100);
        Result<String, Integer> second = Result.err(200);
        Result<String, Integer> third = Result.ok("finally success");

        // When
        Result<String, Integer> result = first.or(second).or(third);

        // Then
        assertTrue(result.isOk());
        assertEquals("finally success", result.unwrap());
    }

    @Test
    void or_WithFirstSuccessInChain_ShouldReturnFirstSuccess() {
        // Given
        Result<String, Integer> first = Result.err(100);
        Result<String, Integer> second = Result.ok("second success");
        Result<String, Integer> third = Result.ok("third success");

        // When
        Result<String, Integer> result = first.or(second).or(third);

        // Then
        assertTrue(result.isOk());
        assertEquals("second success", result.unwrap());
    }

    @Test
    void or_WithNullValues_ShouldWork() {
        // Given
        Result<String, Integer> first = Result.ok(null);
        Result<String, Integer> second = Result.ok("not null");

        // When
        Result<String, Integer> result = first.or(second);

        // Then
        assertTrue(result.isOk());
        assertNull(result.unwrap());
    }

    @Test
    void or_WithDifferentOkTypes_ShouldWork() {
        // Given
        Result<String, Integer> first = Result.err(404);
        Result<String, Integer> second = Result.ok("string value");

        // When
        Result<String, Integer> result = first.or(second);

        // Then
        assertTrue(result.isOk());
        assertEquals("string value", result.unwrap());
    }

    @Test
    void or_WithFirstOk_ShouldNotEvaluateSecond() {
        // Given
        Result<String, Integer> first = Result.ok("first");
        Result<String, Integer> second = Result.err(404);

        // When
        Result<String, Integer> result = first.or(second);

        // Then
        assertTrue(result.isOk());
        assertEquals("first", result.unwrap());
        // The second result is not used but the method should handle it correctly
    }
}