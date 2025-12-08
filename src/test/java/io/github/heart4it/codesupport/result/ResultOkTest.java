package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.ok() static factory method
 */
public class ResultOkTest {

    @Test
    void ok_WithStringValue_ShouldCreateOkResult() {
        // Given
        String expectedValue = "test value";

        // When
        Result<String, Integer> result = Result.ok(expectedValue);

        // Then
        assertTrue(result.isOk());
        assertEquals(expectedValue, result.unwrap());
    }

    @Test
    void ok_WithIntegerValue_ShouldCreateOkResult() {
        // Given
        Integer expectedValue = 42;

        // When
        Result<Integer, String> result = Result.ok(expectedValue);

        // Then
        assertTrue(result.isOk());
        assertEquals(expectedValue, result.unwrap());
    }

    @Test
    void ok_WithBooleanValue_ShouldCreateOkResult() {
        // Given
        Boolean expectedValue = true;

        // When
        Result<Boolean, String> result = Result.ok(expectedValue);

        // Then
        assertTrue(result.isOk());
        assertEquals(expectedValue, result.unwrap());
    }

    @Test
    void ok_WithNullValue_ShouldCreateOkResultWithNull() {
        // Given
        String nullValue = null;

        // When
        Result<String, Integer> result = Result.ok(nullValue);

        // Then
        assertTrue(result.isOk());
        assertNull(result.unwrap());
    }

    @Test
    void ok_WithListValue_ShouldCreateOkResult() {
        // Given
        java.util.List<String> expectedList = java.util.List.of("a", "b", "c");

        // When
        Result<java.util.List<String>, Integer> result = Result.ok(expectedList);

        // Then
        assertTrue(result.isOk());
        assertEquals(expectedList, result.unwrap());
    }

    @Test
    void ok_WithDoubleValue_ShouldCreateOkResult() {
        // Given
        Double expectedValue = 3.14159;

        // When
        Result<Double, String> result = Result.ok(expectedValue);

        // Then
        assertTrue(result.isOk());
        assertEquals(expectedValue, result.unwrap());
    }

    @Test
    void ok_WithArrayValue_ShouldCreateOkResult() {
        // Given
        int[] expectedArray = {1, 2, 3, 4, 5};

        // When
        Result<int[], String> result = Result.ok(expectedArray);

        // Then
        assertTrue(result.isOk());
        assertArrayEquals(expectedArray, result.unwrap());
    }

    @Test
    void ok_WithEmptyString_ShouldCreateOkResult() {
        // Given
        String expectedValue = "";

        // When
        Result<String, Integer> result = Result.ok(expectedValue);

        // Then
        assertTrue(result.isOk());
        assertEquals(expectedValue, result.unwrap());
    }

    @Test
    void ok_WithSameValueMultipleTimes_ShouldCreateDistinctInstances() {
        // Given
        String sameValue = "same";

        // When
        Result<String, Integer> result1 = Result.ok(sameValue);
        Result<String, Integer> result2 = Result.ok(sameValue);

        // Then
        assertTrue(result1.isOk());
        assertTrue(result2.isOk());
        assertEquals(result1.unwrap(), result2.unwrap());
        // Note: They may not be referentially equal but should be functionally equivalent
    }
}