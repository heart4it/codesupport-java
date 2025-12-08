package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Option;
import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.transpose() method
 */
public class ResultTransposeTest {

    @Test
    void transpose_OkWithValue_ShouldReturnOkOfSome() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When
        Result<Option<String>, Integer> result = okResult.transpose();

        // Then
        assertTrue(result.isOk());
        assertTrue(result.unwrap().isSome());
        assertEquals("success", result.unwrap().unwrap());
    }

    @Test
    void transpose_Err_ShouldReturnErrWithSameError() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When
        Result<Option<String>, Integer> result = errResult.transpose();

        // Then
        assertTrue(result.isErr());
        assertEquals(404, result.unwrapErr());
    }

    @Test
    void transpose_WithIntegerValues_ShouldWork() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);

        // When
        Result<Option<Integer>, String> result = okResult.transpose();

        // Then
        assertTrue(result.isOk());
        assertTrue(result.unwrap().isSome());
        assertEquals(42, result.unwrap().unwrap().intValue());
    }

    @Test
    void transpose_WithNullValue_ShouldNotWorkWithNull() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);
        assertThrows(NullPointerException.class,
                () -> {
                    okResult.transpose();
                });
    }

    @Test
    void transpose_WithListValues_ShouldWork() {
        // Given
        java.util.List<String> expectedList = java.util.List.of("x", "y", "z");
        Result<java.util.List<String>, Integer> okResult = Result.ok(expectedList);

        // When
        Result<Option<java.util.List<String>>, Integer> result = okResult.transpose();

        // Then
        assertTrue(result.isOk());
        assertTrue(result.unwrap().isSome());
        assertEquals(expectedList, result.unwrap().unwrap());
    }

    @Test
    void transpose_WithBooleanValues_ShouldWork() {
        // Given
        Result<Boolean, String> okResult = Result.ok(true);

        // When
        Result<Option<Boolean>, String> result = okResult.transpose();

        // Then
        assertTrue(result.isOk());
        assertTrue(result.unwrap().isSome());
        assertTrue(result.unwrap().unwrap());
    }

    @Test
    void transpose_ShouldAllowFurtherOperationsOnOption() {
        // Given
        Result<String, Integer> okResult = Result.ok("test");

        // When
        Result<Option<String>, Integer> transposed = okResult.transpose();
        Option<String> innerOption = transposed.unwrap();
        Option<String> mapped = innerOption.map(String::toUpperCase);

        // Then
        assertTrue(mapped.isSome());
        assertEquals("TEST", mapped.unwrap());
    }

    @Test
    void transpose_AfterTransformations_ShouldWork() {
        // Given
        Result<Integer, String> original = Result.ok(10);

        // When
        Result<String, String> mapped = original.map(n -> "Value: " + n);
        Result<Option<String>, String> result = mapped.transpose();

        // Then
        assertTrue(result.isOk());
        assertTrue(result.unwrap().isSome());
        assertEquals("Value: 10", result.unwrap().unwrap());
    }
}