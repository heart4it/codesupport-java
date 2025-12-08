package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.andThen() method
 */
public class ResultAndThenTest {

    @Test
    void andThen_OkWithFunctionReturningOk_ShouldReturnNewOk() {
        // Given
        Result<Integer, String> original = Result.ok(5);
        Function<Integer, Result<String, String>> mapper = n -> Result.ok("Number: " + n);

        // When
        Result<String, String> result = original.andThen(mapper);

        // Then
        assertTrue(result.isOk());
        assertEquals("Number: 5", result.unwrap());
    }

    @Test
    void andThen_OkWithFunctionReturningErr_ShouldReturnErr() {
        // Given
        Result<Integer, String> original = Result.ok(5);
        Function<Integer, Result<String, String>> mapper = n -> Result.err("Invalid number");

        // When
        Result<String, String> result = original.andThen(mapper);

        // Then
        assertTrue(result.isErr());
        assertEquals("Invalid number", result.unwrapErr());
    }

    @Test
    void andThen_ErrWithAnyFunction_ShouldReturnOriginalErr() {
        // Given
        Result<Integer, String> original = Result.err("Original error");
        Function<Integer, Result<String, String>> mapper = n -> Result.ok("Should not happen");

        // When
        Result<String, String> result = original.andThen(mapper);

        // Then
        assertTrue(result.isErr());
        assertEquals("Original error", result.unwrapErr());
    }

    @Test
    void andThen_WithMultipleChainedOperations_ShouldWorkCorrectly() {
        // Given
        Result<Integer, String> original = Result.ok(2);
        Function<Integer, Result<Integer, String>> doubleIt = n -> Result.ok(n * 2);
        Function<Integer, Result<Integer, String>> addFive = n -> Result.ok(n + 5);
        Function<Integer, Result<String, String>> toString = n -> Result.ok("Result: " + n);

        // When
        Result<String, String> result = original
                .andThen(doubleIt)
                .andThen(addFive)
                .andThen(toString);

        // Then
        assertTrue(result.isOk());
        assertEquals("Result: 9", result.unwrap());
    }

    @Test
    void andThen_WithErrInMiddleOfChain_ShouldShortCircuit() {
        // Given
        Result<Integer, String> original = Result.ok(10);
        Function<Integer, Result<Integer, String>> doubleIt = n -> Result.ok(n * 2);
        Function<Integer, Result<Integer, String>> failIfOverTen = n -> n > 15 ? Result.err("Too big") : Result.ok(n);
        Function<Integer, Result<String, String>> toString = n -> Result.ok("Result: " + n);

        // When
        Result<String, String> result = original
                .andThen(doubleIt) // 20
                .andThen(failIfOverTen) // Err: "Too big"
                .andThen(toString); // Should not execute

        // Then
        assertTrue(result.isErr());
        assertEquals("Too big", result.unwrapErr());
    }

    @Test
    void andThen_WithNullValues_ShouldWork() {
        // Given
        Result<String, String> original = Result.ok(null);
        Function<String, Result<String, String>> mapper = s -> Result.ok("Processed: " + s);

        // When
        Result<String, String> result = original.andThen(mapper);

        // Then
        assertTrue(result.isOk());
        assertEquals("Processed: null", result.unwrap());
    }

    @Test
    void andThen_WithFunctionThatThrowsException_ShouldPropagateException() {
        // Given
        Result<Integer, String> original = Result.ok(5);
        Function<Integer, Result<String, String>> mapper = n -> {
            throw new RuntimeException("Function failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> original.andThen(mapper));
    }


    @Test
    void andThen_WithErrAndDifferentTypes_ShouldPreserveError() {
        // Given
        Result<Integer, String> original = Result.err("Database error");
        Function<Integer, Result<Boolean, String>> mapper = n -> Result.ok(n > 0);

        // When
        Result<Boolean, String> result = original.andThen(mapper);

        // Then
        assertTrue(result.isErr());
        assertEquals("Database error", result.unwrapErr());
    }

}