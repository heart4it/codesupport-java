package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.flatten() static method
 */
public class ResultFlattenTest {

    @Test
    void flatten_OkOfOk_ShouldReturnInnerOk() {
        // Given
        Result<Result<String, Integer>, Integer> nested = Result.ok(Result.ok("success"));

        // When
        Result<String, Integer> result = Result.flatten(nested);

        // Then
        assertTrue(result.isOk());
        assertEquals("success", result.unwrap());
    }

    @Test
    void flatten_OkOfErr_ShouldReturnInnerErr() {
        // Given
        Result<Result<String, Integer>, Integer> nested = Result.ok(Result.err(404));

        // When
        Result<String, Integer> result = Result.flatten(nested);

        // Then
        assertTrue(result.isErr());
        assertEquals(404, result.unwrapErr());
    }

    @Test
    void flatten_Err_ShouldReturnOuterErr() {
        // Given
        Result<Result<String, Integer>, Integer> nested = Result.err(500);

        // When
        Result<String, Integer> result = Result.flatten(nested);

        // Then
        assertTrue(result.isErr());
        assertEquals(500, result.unwrapErr());
    }

    @Test
    void flatten_WithDifferentTypes_ShouldWork() {
        // Given
        Result<Result<Integer, String>, String> nested = Result.ok(Result.ok(42));

        // When
        Result<Integer, String> result = Result.flatten(nested);

        // Then
        assertTrue(result.isOk());
        assertEquals(42, result.unwrap());
    }


    @Test
    void flatten_WithNullValues_ShouldWork() {
        // Given
        Result<Result<String, Integer>, Integer> nested = Result.ok(Result.ok(null));

        // When
        Result<String, Integer> result = Result.flatten(nested);

        // Then
        assertTrue(result.isOk());
        assertNull(result.unwrap());
    }

    @Test
    void flatten_WithListValues_ShouldWork() {
        // Given
        java.util.List<String> expectedList = java.util.List.of("a", "b");
        Result<Result<java.util.List<String>, Integer>, Integer> nested =
                Result.ok(Result.ok(expectedList));

        // When
        Result<java.util.List<String>, Integer> result = Result.flatten(nested);

        // Then
        assertTrue(result.isOk());
        assertEquals(expectedList, result.unwrap());
    }

    @Test
    void flatten_WithDeeplyNestedResults_ShouldWork() {
        // Given
        Result<Result<Result<String, Integer>, Integer>, Integer> deeplyNested =
                Result.ok(Result.ok(Result.ok("deep success")));

        // When
        Result<Result<String, Integer>, Integer> firstFlatten = Result.flatten(deeplyNested);
        Result<String, Integer> secondFlatten = Result.flatten(firstFlatten);

        // Then
        assertTrue(secondFlatten.isOk());
        assertEquals("deep success", secondFlatten.unwrap());
    }
}