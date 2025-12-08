package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.unwrap() method
 */
public class ResultUnwrapTest {

    @Test
    void unwrap_OkWithStringValue_ShouldReturnValue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When
        String result = okResult.unwrap();

        // Then
        assertEquals("success", result);
    }

    @Test
    void unwrap_OkWithIntegerValue_ShouldReturnValue() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);

        // When
        Integer result = okResult.unwrap();

        // Then
        assertEquals(42, result);
    }

    @Test
    void unwrap_OkWithBooleanValue_ShouldReturnValue() {
        // Given
        Result<Boolean, String> okResult = Result.ok(true);

        // When
        Boolean result = okResult.unwrap();

        // Then
        assertTrue(result);
    }

    @Test
    void unwrap_OkWithNullValue_ShouldReturnNull() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);

        // When
        String result = okResult.unwrap();

        // Then
        assertNull(result);
    }

    @Test
    void unwrap_OkWithListValue_ShouldReturnList() {
        // Given
        java.util.List<String> expectedList = java.util.List.of("a", "b", "c");
        Result<java.util.List<String>, Integer> okResult = Result.ok(expectedList);

        // When
        java.util.List<String> result = okResult.unwrap();

        // Then
        assertEquals(expectedList, result);
    }

    @Test
    void unwrap_Err_ShouldThrowNoSuchElementException() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When & Then
        assertThrows(NoSuchElementException.class, errResult::unwrap);
    }

    @Test
    void unwrap_Err_ShouldThrowWithDescriptiveMessage() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, errResult::unwrap);
        assertTrue(exception.getMessage().contains("unwrap() on Err"));
    }

    @Test
    void unwrap_AfterMapOperation_ShouldReturnTransformedValue() {
        // Given
        Result<Integer, String> original = Result.ok(5);

        // When
        Result<String, String> mapped = original.map(n -> "Number: " + n);
        String result = mapped.unwrap();

        // Then
        assertEquals("Number: 5", result);
    }


    @Test
    void unwrap_WithArrayValue_ShouldReturnArray() {
        // Given
        int[] expectedArray = {1, 2, 3};
        Result<int[], String> okResult = Result.ok(expectedArray);

        // When
        int[] result = okResult.unwrap();

        // Then
        assertArrayEquals(expectedArray, result);
    }
}