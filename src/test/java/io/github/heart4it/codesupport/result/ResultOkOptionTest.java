package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Option;
import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.ok() method that returns Option
 */
public class ResultOkOptionTest {

    @Test
    void ok_Ok_ShouldReturnSomeWithValue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When
        Option<String> result = okResult.ok();

        // Then
        assertTrue(result.isSome());
        assertEquals("success", result.unwrap());
    }

    @Test
    void ok_Err_ShouldReturnNone() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When
        Option<String> result = errResult.ok();

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void ok_WithIntegerValues_ShouldWork() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);

        // When
        Option<Integer> result = okResult.ok();

        // Then
        assertTrue(result.isSome());
        assertEquals(42, result.unwrap().intValue());
    }

    @Test
    void ok_WithNullValue_ShouldThrowWhenNullIsConverted() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);

        assertThrows(NullPointerException.class,
                () -> {
                    okResult.ok();
                });
    }

    @Test
    void ok_WithListValues_ShouldWork() {
        // Given
        java.util.List<String> expectedList = java.util.List.of("a", "b");
        Result<java.util.List<String>, Integer> okResult = Result.ok(expectedList);

        // When
        Option<java.util.List<String>> result = okResult.ok();

        // Then
        assertTrue(result.isSome());
        assertEquals(expectedList, result.unwrap());
    }

    @Test
    void ok_AfterMapOperations_ShouldReturnSomeWithTransformedValue() {
        // Given
        Result<Integer, String> original = Result.ok(5);

        // When
        Result<String, String> mapped = original.map(n -> "Number: " + n);
        Option<String> result = mapped.ok();

        // Then
        assertTrue(result.isSome());
        assertEquals("Number: 5", result.unwrap());
    }

    @Test
    void ok_AfterOperationsOnErr_ShouldReturnNone() {
        // Given
        Result<Integer, String> original = Result.err("error");

        // When
        Result<String, String> mapped = original.map(n -> "Number: " + n);
        Option<String> result = mapped.ok();

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void ok_WithBooleanValues_ShouldWork() {
        // Given
        Result<Boolean, String> okResult = Result.ok(false);

        // When
        Option<Boolean> result = okResult.ok();

        // Then
        assertTrue(result.isSome());
        assertFalse(result.unwrap());
    }

    @Test
    void ok_ShouldAllowFurtherOptionOperations() {
        // Given
        Result<String, Integer> okResult = Result.ok("hello");

        // When
        Option<String> option = okResult.ok();
        Option<String> mapped = option.map(String::toUpperCase);

        // Then
        assertTrue(mapped.isSome());
        assertEquals("HELLO", mapped.unwrap());
    }
}