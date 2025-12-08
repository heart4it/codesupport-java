package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.mapOr() method
 */
public class ResultMapOrTest {

    @Test
    void mapOr_OkWithMappingFunction_ShouldReturnMappedValue() {
        // Given
        Result<Integer, String> okResult = Result.ok(5);
        String defaultValue = "default";
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        String result = okResult.mapOr(defaultValue, mapper);

        // Then
        assertEquals("Number: 5", result);
    }

    @Test
    void mapOr_Err_ShouldReturnDefaultValue() {
        // Given
        Result<Integer, String> errResult = Result.err("error");
        String defaultValue = "default";
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        String result = errResult.mapOr(defaultValue, mapper);

        // Then
        assertEquals("default", result);
    }

    @Test
    void mapOr_WithNullDefaultValue_ShouldWork() {
        // Given
        Result<String, Integer> okResult = Result.ok("value");
        String defaultValue = null;
        Function<String, String> mapper = String::toUpperCase;

        // When
        String result = okResult.mapOr(defaultValue, mapper);

        // Then
        assertEquals("VALUE", result);
    }

    @Test
    void mapOr_WithNullMappingResult_ShouldWork() {
        // Given
        Result<String, Integer> okResult = Result.ok("value");
        String defaultValue = "default";
        Function<String, String> mapper = s -> null;

        // When
        String result = okResult.mapOr(defaultValue, mapper);

        // Then
        assertNull(result);
    }

    @Test
    void mapOr_WithIntegerTypes_ShouldWork() {
        // Given
        Result<String, Integer> okResult = Result.ok("123");
        Integer defaultValue = 0;
        Function<String, Integer> mapper = Integer::parseInt;

        // When
        Integer result = okResult.mapOr(defaultValue, mapper);

        // Then
        assertEquals(123, result);
    }

    @Test
    void mapOr_WithBooleanTypes_ShouldWork() {
        // Given
        Result<String, Integer> okResult = Result.ok("true");
        Boolean defaultValue = false;
        Function<String, Boolean> mapper = Boolean::parseBoolean;

        // When
        Boolean result = okResult.mapOr(defaultValue, mapper);

        // Then
        assertTrue(result);
    }

    @Test
    void mapOr_WithFunctionThatThrowsException_ShouldPropagateException() {
        // Given
        Result<Integer, String> okResult = Result.ok(5);
        String defaultValue = "default";
        Function<Integer, String> mapper = n -> {
            throw new RuntimeException("Mapping failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> okResult.mapOr(defaultValue, mapper));
    }


    @Test
    void mapOr_WithListTransformations_ShouldWork() {
        // Given
        Result<String, Integer> okResult = Result.ok("a,b,c");
        java.util.List<String> defaultValue = java.util.List.of("default");
        Function<String, java.util.List<String>> mapper = s -> java.util.List.of(s.split(","));

        // When
        java.util.List<String> result = okResult.mapOr(defaultValue, mapper);

        // Then
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }
}