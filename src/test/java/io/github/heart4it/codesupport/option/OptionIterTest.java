package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionIterTest {

    @Test
    void givenSomeOption_whenIter_thenReturnsListWithOneElement() {
        // Given
        Option<String> option = Option.Some("hello");

        // When
        List<String> result = option.iter();

        // Then
        assertEquals(1, result.size());
        assertEquals("hello", result.get(0));
    }

    @Test
    void givenNoneOption_whenIter_thenReturnsEmptyList() {
        // Given
        Option<String> option = Option.None();

        // When
        List<String> result = option.iter();

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenSomeOptionWithInteger_whenIter_thenReturnsListWithInteger() {
        // Given
        Option<Integer> option = Option.Some(42);

        // When
        List<Integer> result = option.iter();

        // Then
        assertEquals(1, result.size());
        assertEquals(42, result.get(0));
    }

    @Test
    void givenSomeOptionAfterMap_whenIter_thenReturnsListWithMappedValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);

        // When
        List<String> result = option.iter();

        // Then
        assertEquals(1, result.size());
        assertEquals("HELLO", result.get(0));
    }

    @Test
    void givenNoneOptionAfterMap_whenIter_thenReturnsEmptyList() {
        // Given
        Option<String> option = Option.<String>None().map(String::toUpperCase);

        // When
        List<String> result = option.iter();

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenSomeOptionAfterFilter_whenIter_thenReturnsListWithFilteredValue() {
        // Given
        Option<Integer> option = Option.Some(10).filter(n -> n > 5);

        // When
        List<Integer> result = option.iter();

        // Then
        assertEquals(1, result.size());
        assertEquals(10, result.get(0));
    }

    @Test
    void givenNoneOptionAfterFilter_whenIter_thenReturnsEmptyList() {
        // Given
        Option<Integer> option = Option.Some(3).filter(n -> n > 5);

        // When
        List<Integer> result = option.iter();

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenIterResult_whenUsedInForLoop_thenIteratesCorrectly() {
        // Given
        Option<String> option = Option.Some("test");
        List<String> collected = new ArrayList<>();

        // When
        for (String value : option.iter()) {
            collected.add(value);
        }

        // Then
        assertEquals(1, collected.size());
        assertEquals("test", collected.get(0));
    }

    @Test
    void givenNoneIterResult_whenUsedInForLoop_thenNoIteration() {
        // Given
        Option<String> option = Option.None();
        List<String> collected = new ArrayList<>();

        // When
        for (String value : option.iter()) {
            collected.add(value);
        }

        // Then
        assertTrue(collected.isEmpty());
    }
}