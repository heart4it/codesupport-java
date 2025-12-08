package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OptionToOptionalTest {

    @Test
    void givenSomeOption_whenToOptional_thenReturnsOptionalWithValue() {
        // Given
        Option<String> option = Option.Some("hello");

        // When
        Optional<String> result = option.toOptional();

        // Then
        assertTrue(result.isPresent());
        assertEquals("hello", result.get());
    }

    @Test
    void givenNoneOption_whenToOptional_thenReturnsEmptyOptional() {
        // Given
        Option<String> option = Option.None();

        // When
        Optional<String> result = option.toOptional();

        // Then
        assertTrue(result.isEmpty());
    }


    @Test
    void givenSomeOptionWithInteger_whenToOptional_thenReturnsOptionalWithInteger() {
        // Given
        Option<Integer> option = Option.Some(42);

        // When
        Optional<Integer> result = option.toOptional();

        // Then
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    void givenSomeOptionAfterMap_whenToOptional_thenReturnsOptionalWithMappedValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);

        // When
        Optional<String> result = option.toOptional();

        // Then
        assertTrue(result.isPresent());
        assertEquals("HELLO", result.get());
    }

    @Test
    void givenNoneOptionAfterMap_whenToOptional_thenReturnsEmptyOptional() {
        // Given
        Option<String> option = Option.<String>None().map(String::toUpperCase);

        // When
        Optional<String> result = option.toOptional();

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenSomeOptionAfterFilter_whenToOptional_thenReturnsOptionalWithFilteredValue() {
        // Given
        Option<Integer> option = Option.Some(10).filter(n -> n > 5);

        // When
        Optional<Integer> result = option.toOptional();

        // Then
        assertTrue(result.isPresent());
        assertEquals(10, result.get());
    }

    @Test
    void givenNoneOptionAfterFilter_whenToOptional_thenReturnsEmptyOptional() {
        // Given
        Option<Integer> option = Option.Some(3).filter(n -> n > 5);

        // When
        Optional<Integer> result = option.toOptional();

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenSomeOptionWithObject_whenToOptional_thenReturnsOptionalWithSameObject() {
        // Given
        Object expected = new Object();
        Option<Object> option = Option.Some(expected);

        // When
        Optional<Object> result = option.toOptional();

        // Then
        assertTrue(result.isPresent());
        assertSame(expected, result.get());
    }

    @Test
    void givenRoundTripFromOptional_whenToOptional_thenReturnsEquivalent() {
        // Given
        Optional<String> originalOptional = Optional.of("test");
        Option<String> option = Option.fromOptional(originalOptional);

        // When
        Optional<String> result = option.toOptional();

        // Then
        assertEquals(originalOptional, result);
        assertEquals(originalOptional.isPresent(), result.isPresent());
        if (originalOptional.isPresent()) {
            assertEquals(originalOptional.get(), result.get());
        }
    }
}