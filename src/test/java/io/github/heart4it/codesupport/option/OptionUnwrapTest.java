package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class OptionUnwrapTest {

    @Test
    void givenSomeOption_whenUnwrap_thenReturnsValue() {
        // Given
        Option<String> option = Option.Some("hello");

        // When
        String result = option.unwrap();

        // Then
        assertEquals("hello", result);
    }

    @Test
    void givenSomeOptionWithInteger_whenUnwrap_thenReturnsInteger() {
        // Given
        Option<Integer> option = Option.Some(42);

        // When
        Integer result = option.unwrap();

        // Then
        assertEquals(42, result);
    }

    @Test
    void givenNoneOption_whenUnwrap_thenThrowsException() {
        // Given
        Option<String> option = Option.None();

        // When & Then
        assertThrows(NoSuchElementException.class, option::unwrap);
    }

    @Test
    void givenNoneOption_whenUnwrap_thenExceptionHasCorrectMessage() {
        // Given
        Option<String> option = Option.None();

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, option::unwrap);
        assertTrue(exception.getMessage().contains("called unwrap() on None"));
    }

    @Test
    void givenSomeOptionAfterMap_whenUnwrap_thenReturnsMappedValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);

        // When
        String result = option.unwrap();

        // Then
        assertEquals("HELLO", result);
    }

    @Test
    void givenSomeOptionAfterFilter_whenUnwrap_thenReturnsFilteredValue() {
        // Given
        Option<Integer> option = Option.Some(10).filter(n -> n > 5);

        // When
        Integer result = option.unwrap();

        // Then
        assertEquals(10, result);
    }

    @Test
    void givenSomeOptionAfterAndThen_whenUnwrap_thenReturnsFinalValue() {
        // Given
        Option<String> option = Option.Some("123")
                .andThen(s -> Option.Some(s + "456"));

        // When
        String result = option.unwrap();

        // Then
        assertEquals("123456", result);
    }

    @Test
    void givenNoneOptionAfterOperations_whenUnwrap_thenThrowsException() {
        // Given
        Option<String> option = Option.<String>None()
                .map(String::toUpperCase)
                .filter(s -> s.length() > 10);

        // When & Then
        assertThrows(NoSuchElementException.class, option::unwrap);
    }

    @Test
    void givenSomeOptionWithObject_whenUnwrap_thenReturnsSameObject() {
        // Given
        Object expected = new Object();
        Option<Object> option = Option.Some(expected);

        // When
        Object result = option.unwrap();

        // Then
        assertSame(expected, result);
    }
}