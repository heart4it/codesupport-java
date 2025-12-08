package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.map() method
 */
public class ResultMapTest {

    @Test
    void map_OkWithMappingFunction_ShouldReturnNewOk() {
        // Given
        Result<Integer, String> original = Result.ok(5);
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        Result<String, String> result = original.map(mapper);

        // Then
        assertTrue(result.isOk());
        assertEquals("Number: 5", result.unwrap());
    }

    @Test
    void map_ErrWithMappingFunction_ShouldReturnOriginalErr() {
        // Given
        Result<Integer, String> original = Result.err("error");
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        Result<String, String> result = original.map(mapper);

        // Then
        assertTrue(result.isErr());
        assertEquals("error", result.unwrapErr());
    }

    @Test
    void map_WithMultipleChainedOperations_ShouldWork() {
        // Given
        Result<Integer, String> original = Result.ok(10);
        Function<Integer, Integer> doubleIt = n -> n * 2;
        Function<Integer, String> toString = n -> "Result: " + n;

        // When
        Result<String, String> result = original
                .map(doubleIt)
                .map(toString);

        // Then
        assertTrue(result.isOk());
        assertEquals("Result: 20", result.unwrap());
    }

    @Test
    void map_WithNullMappingResult_ShouldWork() {
        // Given
        Result<String, Integer> original = Result.ok("value");
        Function<String, String> mapper = s -> null;

        // When
        Result<String, Integer> result = original.map(mapper);

        // Then
        assertTrue(result.isOk());
        assertNull(result.unwrap());
    }

    @Test
    void map_WithFunctionThatThrowsException_ShouldPropagateException() {
        // Given
        Result<Integer, String> original = Result.ok(5);
        Function<Integer, String> mapper = n -> {
            throw new RuntimeException("Mapping failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> original.map(mapper));
    }

    @Test
    void map_WithDifferentTypes_ShouldWork() {
        // Given
        Result<String, Integer> original = Result.ok("123");
        Function<String, Integer> mapper = Integer::parseInt;

        // When
        Result<Integer, Integer> result = original.map(mapper);

        // Then
        assertTrue(result.isOk());
        assertEquals(123, result.unwrap());
    }

    @Test
    void map_WithErrInChain_ShouldPreserveError() {
        // Given
        Result<Integer, String> original = Result.err("original error");
        Function<Integer, Integer> doubleIt = n -> n * 2;
        Function<Integer, String> toString = Object::toString;

        // When
        Result<String, String> result = original
                .map(doubleIt)
                .map(toString);

        // Then
        assertTrue(result.isErr());
        assertEquals("original error", result.unwrapErr());
    }

    @Test
    void map_WithIdentityFunction_ShouldReturnSameValue() {
        // Given
        Result<String, Integer> original = Result.ok("same");
        Function<String, String> identity = s -> s;

        // When
        Result<String, Integer> result = original.map(identity);

        // Then
        assertTrue(result.isOk());
        assertEquals("same", result.unwrap());
    }

    @Test
    void map_WithBooleanTransformations_ShouldWork() {
        // Given
        Result<String, Integer> original = Result.ok("true");
        Function<String, Boolean> mapper = Boolean::parseBoolean;

        // When
        Result<Boolean, Integer> result = original.map(mapper);

        // Then
        assertTrue(result.isOk());
        assertTrue(result.unwrap());
    }
}