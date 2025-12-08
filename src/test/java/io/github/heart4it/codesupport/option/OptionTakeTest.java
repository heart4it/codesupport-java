package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionTakeTest {

    @Test
    void givenSomeOption_whenTake_thenReturnsSomeWithValue() {
        // Given
        Option<String> option = Option.Some("value");

        // When
        Option<String> result = option.take();

        // Then
        assertTrue(result.isSome());
        assertEquals("value", result.unwrap());
    }

    @Test
    void givenNoneOption_whenTake_thenReturnsNone() {
        // Given
        Option<String> option = Option.None();

        // When
        Option<String> result = option.take();

        // Then
        assertTrue(result.isNone());
    }


    @Test
    void givenSomeOptionWithInteger_whenTake_thenReturnsSomeWithInteger() {
        // Given
        Option<Integer> option = Option.Some(42);

        // When
        Option<Integer> result = option.take();

        // Then
        assertTrue(result.isSome());
        assertEquals(42, result.unwrap());
    }

    @Test
    void givenSomeOptionAfterMap_whenTake_thenReturnsSomeWithMappedValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);

        // When
        Option<String> result = option.take();

        // Then
        assertTrue(result.isSome());
        assertEquals("HELLO", result.unwrap());
    }

    @Test
    void givenNoneOptionAfterMap_whenTake_thenReturnsNone() {
        // Given
        Option<String> option = Option.<String>None().map(String::toUpperCase);

        // When
        Option<String> result = option.take();

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenSomeOptionAfterFilter_whenTake_thenReturnsSomeWithFilteredValue() {
        // Given
        Option<Integer> option = Option.Some(10).filter(n -> n > 5);

        // When
        Option<Integer> result = option.take();

        // Then
        assertTrue(result.isSome());
        assertEquals(10, result.unwrap());
    }

    @Test
    void givenNoneOptionAfterFilter_whenTake_thenReturnsNone() {
        // Given
        Option<Integer> option = Option.Some(3).filter(n -> n > 5);

        // When
        Option<Integer> result = option.take();

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenSomeOption_whenTakeThenOriginalUnchanged_thenOriginalPreserved() {
        // Given
        Option<String> original = Option.Some("original");

        // When
        Option<String> result = original.take();

        // Then
        assertEquals("original", result.unwrap());
        assertEquals("original", original.unwrap()); // Original unchanged (immutable)
    }

    @Test
    void givenChainedTakeOperations_thenReturnsOriginalValue() {
        // Given
        Option<String> option = Option.Some("value");

        // When
        Option<String> result = option
                .take()
                .take()
                .take();

        // Then
        assertTrue(result.isSome());
        assertEquals("value", result.unwrap());
    }
}