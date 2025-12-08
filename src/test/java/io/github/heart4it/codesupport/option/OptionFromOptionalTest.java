package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OptionFromOptionalTest {

    @Test
    void whenOptionalContainsValue_thenReturnsSome() {
        // When
        Optional<String> javaOptional = Optional.of("test");
        Option<String> option = Option.fromOptional(javaOptional);

        // Then
        assertTrue(option.isSome());
        assertEquals("test", option.unwrap());
    }

    @Test
    void whenOptionalIsEmpty_thenReturnsNone() {
        // When
        Optional<String> javaOptional = Optional.empty();
        Option<String> option = Option.fromOptional(javaOptional);

        // Then
        assertTrue(option.isNone());
    }

    @Test
    void whenOptionalContainsNull_thenReturnsNone() {
        // When
        Optional<String> javaOptional = Optional.ofNullable(null);
        Option<String> option = Option.fromOptional(javaOptional);
        // Then
        assertTrue(option.isNone());
    }

    @Test
    void whenOptionalContainsInteger_thenReturnsSomeWithInteger() {
        // When
        Optional<Integer> javaOptional = Optional.of(123);
        Option<Integer> option = Option.fromOptional(javaOptional);

        // Then
        assertTrue(option.isSome());
        assertEquals(123, option.unwrap());
    }

    @Test
    void whenOptionalContainsList_thenReturnsSomeWithList() {
        // When
        Optional<java.util.List<String>> javaOptional = Optional.of(java.util.List.of("a", "b"));
        Option<java.util.List<String>> option = Option.fromOptional(javaOptional);

        // Then
        assertTrue(option.isSome());
        assertEquals(2, option.unwrap().size());
    }

    @Test
    void whenOptionalFromNullableWithValue_thenReturnsSome() {
        // When
        Optional<String> javaOptional = Optional.ofNullable("value");
        Option<String> option = Option.fromOptional(javaOptional);

        // Then
        assertTrue(option.isSome());
        assertEquals("value", option.unwrap());
    }

    @Test
    void whenOptionalFromNullableWithNull_thenReturnsNone() {
        // When
        Optional<String> javaOptional = Optional.ofNullable(null);
        Option<String> option = Option.fromOptional(javaOptional);

        // Then
        assertTrue(option.isNone());
    }

    @Test
    void whenMultipleOptionalsConverted_thenCorrectOptionsReturned() {
        // When
        Option<String> someFromFull = Option.fromOptional(Optional.of("full"));
        Option<String> noneFromEmpty = Option.fromOptional(Optional.empty());

        // Then
        assertTrue(someFromFull.isSome());
        assertTrue(noneFromEmpty.isNone());
    }

    @Test
    void whenOptionalContainsBoolean_thenReturnsSomeWithBoolean() {
        // When
        Optional<Boolean> javaOptional = Optional.of(true);
        Option<Boolean> option = Option.fromOptional(javaOptional);

        // Then
        assertTrue(option.isSome());
        assertTrue(option.unwrap());
    }

    @Test
    void whenOptionalContainsObject_thenReturnsSomeWithObject() {
        // When
        Object obj = new Object();
        Optional<Object> javaOptional = Optional.of(obj);
        Option<Object> option = Option.fromOptional(javaOptional);

        // Then
        assertTrue(option.isSome());
        assertSame(obj, option.unwrap());
    }
}