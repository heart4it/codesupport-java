package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.mapErr() method
 */
public class ResultMapErrTest {

    @Test
    void mapErr_ErrWithMappingFunction_ShouldReturnNewErr() {
        // Given
        Result<String, Integer> original = Result.err(404);
        Function<Integer, String> mapper = e -> "Error: " + e;

        // When
        Result<String, String> result = original.mapErr(mapper);

        // Then
        assertTrue(result.isErr());
        assertEquals("Error: 404", result.unwrapErr());
    }

    @Test
    void mapErr_OkWithMappingFunction_ShouldReturnOriginalOk() {
        // Given
        Result<String, Integer> original = Result.ok("success");
        Function<Integer, String> mapper = e -> "Error: " + e;

        // When
        Result<String, String> result = original.mapErr(mapper);

        // Then
        assertTrue(result.isOk());
        assertEquals("success", result.unwrap());
    }

    @Test
    void mapErr_WithMultipleChainedOperations_ShouldWork() {
        // Given
        Result<String, Integer> original = Result.err(404);
        Function<Integer, String> addPrefix = e -> "Code: " + e;
        Function<String, String> toUpperCase = s -> s.toUpperCase();

        // When
        Result<String, String> result = original
                .mapErr(addPrefix)
                .mapErr(toUpperCase);

        // Then
        assertTrue(result.isErr());
        assertEquals("CODE: 404", result.unwrapErr());
    }

    @Test
    void mapErr_WithNullMappingResult_ShouldWork() {
        // Given
        Result<String, String> original = Result.err("error");
        Function<String, String> mapper = s -> null;

        // When
        Result<String, String> result = original.mapErr(mapper);

        // Then
        assertTrue(result.isErr());
        assertNull(result.unwrapErr());
    }

    @Test
    void mapErr_WithFunctionThatThrowsException_ShouldPropagateException() {
        // Given
        Result<String, Integer> original = Result.err(404);
        Function<Integer, String> mapper = e -> {
            throw new RuntimeException("Error mapping failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> original.mapErr(mapper));
    }

    @Test
    void mapErr_WithDifferentErrorTypes_ShouldWork() {
        // Given
        Result<String, Integer> original = Result.err(404);
        Function<Integer, String> mapper = Object::toString;

        // When
        Result<String, String> result = original.mapErr(mapper);

        // Then
        assertTrue(result.isErr());
        assertEquals("404", result.unwrapErr());
    }

    @Test
    void mapErr_WithOkInChain_ShouldPreserveOkValue() {
        // Given
        Result<String, Integer> original = Result.ok("success");
        Function<Integer, String> addPrefix = e -> "Error: " + e;
        Function<String, String> toUpperCase = s -> s.toUpperCase();

        // When
        Result<String, String> result = original
                .mapErr(addPrefix)
                .mapErr(toUpperCase);

        // Then
        assertTrue(result.isOk());
        assertEquals("success", result.unwrap());
    }

    @Test
    void mapErr_WithIdentityFunction_ShouldReturnSameError() {
        // Given
        Result<String, String> original = Result.err("same error");
        Function<String, String> identity = s -> s;

        // When
        Result<String, String> result = original.mapErr(identity);

        // Then
        assertTrue(result.isErr());
        assertEquals("same error", result.unwrapErr());
    }


    @Test
    void mapErr_WithBooleanErrorTransformations_ShouldWork() {
        // Given
        Result<String, Boolean> original = Result.err(true);
        Function<Boolean, String> mapper = b -> b ? "TRUE_ERROR" : "FALSE_ERROR";

        // When
        Result<String, String> result = original.mapErr(mapper);

        // Then
        assertTrue(result.isErr());
        assertEquals("TRUE_ERROR", result.unwrapErr());
    }
}