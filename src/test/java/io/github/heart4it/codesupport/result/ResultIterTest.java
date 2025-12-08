package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.iter() method
 */
public class ResultIterTest {

    @Test
    void iter_Ok_ShouldReturnSingletonListWithValue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");

        // When
        List<String> result = okResult.iter();

        // Then
        assertEquals(1, result.size());
        assertEquals("success", result.get(0));
    }

    @Test
    void iter_Err_ShouldReturnEmptyList() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When
        List<String> result = errResult.iter();

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void iter_WithIntegerValues_ShouldWork() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);

        // When
        List<Integer> result = okResult.iter();

        // Then
        assertEquals(1, result.size());
        assertEquals(42, result.get(0).intValue());
    }

    @Test
    void iter_WithNullValue_ShouldReturnSingletonListWithNull() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);

        // When
        List<String> result = okResult.iter();

        // Then
        assertEquals(1, result.size());
        assertNull(result.get(0));
    }


    @Test
    void iter_WithListValues_ShouldWork() {
        // Given
        List<String> expectedList = List.of("a", "b", "c");
        Result<List<String>, Integer> okResult = Result.ok(expectedList);

        // When
        List<List<String>> result = okResult.iter();

        // Then
        assertEquals(1, result.size());
        assertEquals(expectedList, result.get(0));
    }

    @Test
    void iter_ShouldAllowIterationOverOkValues() {
        // Given
        Result<String, Integer> okResult = Result.ok("hello");

        // When
        Iterator<String> iterator = okResult.iter().iterator();

        // Then
        assertTrue(iterator.hasNext());
        assertEquals("hello", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void iter_WithErr_ShouldHaveNoElements() {
        // Given
        Result<String, Integer> errResult = Result.err(404);

        // When
        Iterator<String> iterator = errResult.iter().iterator();

        // Then
        assertFalse(iterator.hasNext());
    }

    @Test
    void iter_WithBooleanValues_ShouldWork() {
        // Given
        Result<Boolean, String> okResult = Result.ok(true);

        // When
        List<Boolean> result = okResult.iter();

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0));
    }

    @Test
    void iter_ShouldBeUsableInForEachLoopsForOk() {
        // Given
        Result<String, Integer> okResult = Result.ok("item");
        StringBuilder builder = new StringBuilder();

        // When
        for (String item : okResult.iter()) {
            builder.append(item);
        }

        // Then
        assertEquals("item", builder.toString());
    }

    @Test
    void iter_ShouldBeEmptyInForEachLoopsForErr() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        StringBuilder builder = new StringBuilder();

        // When
        for (String item : errResult.iter()) {
            builder.append(item);
        }

        // Then
        assertEquals("", builder.toString());
    }
}