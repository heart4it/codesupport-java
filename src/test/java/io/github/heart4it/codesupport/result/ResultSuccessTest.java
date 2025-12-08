package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.success() static factory method
 */
public class ResultSuccessTest {

    @Test
    void success_WithStringValue_ShouldCreateSuccessResult() {
        // Given
        String expectedValue = "success value";

        // When
        Result<String, Integer> result = Result.success(expectedValue);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(expectedValue, result.unwrap());
    }

    @Test
    void success_WithValue_ShouldBeEquivalentToOk() {
        // Given
        String expectedValue = "test value";

        // When
        Result<String, Integer> successResult = Result.success(expectedValue);
        Result<String, Integer> okResult = Result.ok(expectedValue);

        // Then
        assertEquals(successResult.isOk(), okResult.isOk());
        assertEquals(successResult.unwrap(), okResult.unwrap());
    }

    @Test
    void success_WithNullValue_ShouldCreateSuccessWithNull() {
        // Given
        String nullValue = null;

        // When
        Result<String, Integer> result = Result.success(nullValue);

        // Then
        assertTrue(result.isSuccess());
        assertNull(result.unwrap());
    }

    @Test
    void success_WithIntegerValue_ShouldCreateSuccessResult() {
        // Given
        Integer expectedValue = 100;

        // When
        Result<Integer, String> result = Result.success(expectedValue);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(expectedValue, result.unwrap());
    }

    @Test
    void success_WithBooleanTrue_ShouldCreateSuccessResult() {
        // Given
        Boolean expectedValue = true;

        // When
        Result<Boolean, String> result = Result.success(expectedValue);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(expectedValue, result.unwrap());
    }

    @Test
    void success_WithBooleanFalse_ShouldCreateSuccessResult() {
        // Given
        Boolean expectedValue = false;

        // When
        Result<Boolean, String> result = Result.success(expectedValue);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(expectedValue, result.unwrap());
    }

    @Test
    void success_WithListCollection_ShouldCreateSuccessResult() {
        // Given
        java.util.List<Integer> expectedList = java.util.List.of(1, 2, 3);

        // When
        Result<java.util.List<Integer>, String> result = Result.success(expectedList);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(expectedList, result.unwrap());
    }

    @Test
    void success_WithMapCollection_ShouldCreateSuccessResult() {
        // Given
        java.util.Map<String, Integer> expectedMap = java.util.Map.of("a", 1, "b", 2);

        // When
        Result<java.util.Map<String, Integer>, String> result = Result.success(expectedMap);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(expectedMap, result.unwrap());
    }

    @Test
    void success_WithZeroValue_ShouldCreateSuccessResult() {
        // Given
        Integer expectedValue = 0;

        // When
        Result<Integer, String> result = Result.success(expectedValue);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(expectedValue, result.unwrap());
    }

    @Test
    void success_WithNegativeNumber_ShouldCreateSuccessResult() {
        // Given
        Integer expectedValue = -42;

        // When
        Result<Integer, String> result = Result.success(expectedValue);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(expectedValue, result.unwrap());
    }
}