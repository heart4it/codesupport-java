package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionUnwrapUncheckedTest {

    @Test
    void givenSomeOption_whenUnwrapUnchecked_thenReturnsValue() {
        // Given
        Option<String> option = Option.Some("hello");

        // When
        String result = option.unwrapUnchecked();

        // Then
        assertEquals("hello", result);
    }

    @Test
    void givenNoneOption_whenUnwrapUnchecked_thenReturnsNull() {
        // Given
        Option<String> option = Option.None();

        // When
        String result = option.unwrapUnchecked();

        // Then
        assertNull(result);
    }

    @Test
    void givenSomeOptionWithInteger_whenUnwrapUnchecked_thenReturnsInteger() {
        // Given
        Option<Integer> option = Option.Some(42);

        // When
        Integer result = option.unwrapUnchecked();

        // Then
        assertEquals(42, result);
    }

    @Test
    void givenNoneOptionWithInteger_whenUnwrapUnchecked_thenReturnsNull() {
        // Given
        Option<Integer> option = Option.None();

        // When
        Integer result = option.unwrapUnchecked();

        // Then
        assertNull(result);
    }

    @Test
    void givenSomeOptionAfterMap_whenUnwrapUnchecked_thenReturnsMappedValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);

        // When
        String result = option.unwrapUnchecked();

        // Then
        assertEquals("HELLO", result);
    }

    @Test
    void givenNoneOptionAfterMap_whenUnwrapUnchecked_thenReturnsNull() {
        // Given
        Option<String> option = Option.<String>None().map(String::toUpperCase);

        // When
        String result = option.unwrapUnchecked();

        // Then
        assertNull(result);
    }

    @Test
    void givenSomeOptionAfterFilter_whenUnwrapUnchecked_thenReturnsValue() {
        // Given
        Option<Integer> option = Option.Some(10).filter(n -> n > 5);

        // When
        Integer result = option.unwrapUnchecked();

        // Then
        assertEquals(10, result);
    }

    @Test
    void givenNoneOptionAfterFilter_whenUnwrapUnchecked_thenReturnsNull() {
        // Given
        Option<Integer> option = Option.Some(3).filter(n -> n > 5);

        // When
        Integer result = option.unwrapUnchecked();

        // Then
        assertNull(result);
    }

    @Test
    void givenSomeOptionWithObject_whenUnwrapUnchecked_thenReturnsSameObject() {
        // Given
        Object expected = new Object();
        Option<Object> option = Option.Some(expected);

        // When
        Object result = option.unwrapUnchecked();

        // Then
        assertSame(expected, result);
    }
}