package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for Result.and() method
 */
public class ResultAndTest {

    @Test
    void and_OkAndOk_ShouldReturnSecondOk() {
        // Given
        Result<String, Integer> first = Result.ok("first");
        Result<String, Integer> second = Result.ok("second");

        // When
        Result<String, Integer> result = first.and(second);

        // Then
        assertTrue(result.isOk());
        assertEquals("second", result.unwrap());
    }

    @Test
    void and_OkAndErr_ShouldReturnErr() {
        // Given
        Result<String, Integer> first = Result.ok("first");
        Result<String, Integer> second = Result.err(404);

        // When
        Result<String, Integer> result = first.and(second);

        // Then
        assertTrue(result.isErr());
        assertEquals(404, result.unwrapErr());
    }

    @Test
    void and_ErrAndOk_ShouldReturnFirstErr() {
        // Given
        Result<String, Integer> first = Result.err(500);
        Result<String, Integer> second = Result.ok("second");

        // When
        Result<String, Integer> result = first.and(second);

        // Then
        assertTrue(result.isErr());
        assertEquals(500, result.unwrapErr());
    }

    @Test
    void and_ErrAndErr_ShouldReturnFirstErr() {
        // Given
        Result<String, Integer> first = Result.err(500);
        Result<String, Integer> second = Result.err(404);

        // When
        Result<String, Integer> result = first.and(second);

        // Then
        assertTrue(result.isErr());
        assertEquals(500, result.unwrapErr());
    }

    @Test
    void and_WithDifferentValueTypes_ShouldWork() {
        // Given
        Result<String, Integer> first = Result.ok("hello");
        Result<Integer, Integer> second = Result.ok(42);

        // When
        Result<Integer, Integer> result = first.and(second);

        // Then
        assertTrue(result.isOk());
        assertEquals(42, result.unwrap());
    }


    @Test
    void and_OkWithNullAndOk_ShouldWork() {
        // Given
        Result<String, Integer> first = Result.ok(null);
        Result<String, Integer> second = Result.ok("valid");

        // When
        Result<String, Integer> result = first.and(second);

        // Then
        assertTrue(result.isOk());
        assertEquals("valid", result.unwrap());
    }

    @Test
    void and_WithChainedOperations_ShouldWorkCorrectly() {
        // Given
        Result<String, Integer> start = Result.ok("start");
        Result<String, Integer> middle = Result.ok("middle");
        Result<String, Integer> end = Result.ok("end");

        // When
        Result<String, Integer> result = start.and(middle).and(end);

        // Then
        assertTrue(result.isOk());
        assertEquals("end", result.unwrap());
    }

    @Test
    void and_WithFirstErrInChain_ShouldShortCircuit() {
        // Given
        Result<String, Integer> start = Result.err(500);
        Result<String, Integer> middle = Result.ok("middle");
        Result<String, Integer> end = Result.ok("end");

        // When
        Result<String, Integer> result = start.and(middle).and(end);

        // Then
        assertTrue(result.isErr());
        assertEquals(500, result.unwrapErr());
    }

    @Test
    void and_WithMiddleErrInChain_ShouldShortCircuit() {
        // Given
        Result<String, Integer> start = Result.ok("start");
        Result<String, Integer> middle = Result.err(404);
        Result<String, Integer> end = Result.ok("end");

        // When
        Result<String, Integer> result = start.and(middle).and(end);

        // Then
        assertTrue(result.isErr());
        assertEquals(404, result.unwrapErr());
    }
}