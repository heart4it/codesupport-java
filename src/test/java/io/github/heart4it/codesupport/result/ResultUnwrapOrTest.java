package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.unwrapOr() method
 */
public class ResultUnwrapOrTest {

    @Test
    void unwrapOr_Ok_ShouldReturnValue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");
        String defaultValue = "default";

        // When
        String result = okResult.unwrapOr(defaultValue);

        // Then
        assertEquals("success", result);
    }

    @Test
    void unwrapOr_Err_ShouldReturnDefaultValue() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        String defaultValue = "default";

        // When
        String result = errResult.unwrapOr(defaultValue);

        // Then
        assertEquals("default", result);
    }

    @Test
    void unwrapOr_WithNullDefaultValue_ShouldWork() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        String defaultValue = null;

        // When
        String result = errResult.unwrapOr(defaultValue);

        // Then
        assertNull(result);
    }

    @Test
    void unwrapOr_WithIntegerTypes_ShouldWork() {
        // Given
        Result<Integer, String> okResult = Result.ok(100);
        Integer defaultValue = 0;

        // When
        Integer result = okResult.unwrapOr(defaultValue);

        // Then
        assertEquals(100, result);
    }

    @Test
    void unwrapOr_WithBooleanTypes_ShouldWork() {
        // Given
        Result<Boolean, String> okResult = Result.ok(true);
        Boolean defaultValue = false;

        // When
        Boolean result = okResult.unwrapOr(defaultValue);

        // Then
        assertTrue(result);
    }


    @Test
    void unwrapOr_WithListTypes_ShouldWork() {
        // Given
        java.util.List<String> expectedList = java.util.List.of("a", "b");
        java.util.List<String> defaultList = java.util.List.of("default");
        Result<java.util.List<String>, Integer> okResult = Result.ok(expectedList);

        // When
        java.util.List<String> result = okResult.unwrapOr(defaultList);

        // Then
        assertEquals(expectedList, result);
    }

    @Test
    void unwrapOr_WithErrAndLists_ShouldReturnDefault() {
        // Given
        java.util.List<String> defaultList = java.util.List.of("default");
        Result<java.util.List<String>, Integer> errResult = Result.err(404);

        // When
        java.util.List<String> result = errResult.unwrapOr(defaultList);

        // Then
        assertEquals(defaultList, result);
    }

    @Test
    void unwrapOr_WithZeroAndNegativeValues_ShouldWork() {
        // Given
        Result<Integer, String> okResult = Result.ok(0);
        Integer defaultValue = -1;

        // When
        Integer result = okResult.unwrapOr(defaultValue);

        // Then
        assertEquals(0, result);
    }
}