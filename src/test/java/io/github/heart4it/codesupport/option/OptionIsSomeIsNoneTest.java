package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionIsSomeIsNoneTest {

    @Test
    void whenSomeCreated_thenIsSomeReturnsTrue() {
        // When
        Option<String> option = Option.Some("value");

        // Then
        assertTrue(option.isSome());
        assertFalse(option.isNone());
    }

    @Test
    void whenNoneCreated_thenIsNoneReturnsTrue() {
        // When
        Option<String> option = Option.None();

        // Then
        assertTrue(option.isNone());
        assertFalse(option.isSome());
    }


    @Test
    void whenSomeWithZero_thenIsSomeReturnsTrue() {
        // When
        Option<Integer> option = Option.Some(0);

        // Then
        assertTrue(option.isSome());
        assertFalse(option.isNone());
    }

    @Test
    void whenSomeWithFalse_thenIsSomeReturnsTrue() {
        // When
        Option<Boolean> option = Option.Some(false);

        // Then
        assertTrue(option.isSome());
        assertFalse(option.isNone());
    }

    @Test
    void whenSomeWithEmptyString_thenIsSomeReturnsTrue() {
        // When
        Option<String> option = Option.Some("");

        // Then
        assertTrue(option.isSome());
        assertFalse(option.isNone());
    }

    @Test
    void whenNoneForDifferentTypes_thenIsNoneReturnsTrue() {
        // When
        Option<String> stringNone = Option.None();
        Option<Integer> intNone = Option.None();
        Option<Object> objNone = Option.None();

        // Then
        assertTrue(stringNone.isNone());
        assertTrue(intNone.isNone());
        assertTrue(objNone.isNone());
    }

    @Test
    void whenSomeAfterMapOperation_thenIsSomeReturnsTrue() {
        // When
        Option<String> option = Option.Some("hello").map(String::toUpperCase);

        // Then
        assertTrue(option.isSome());
        assertFalse(option.isNone());
    }

    @Test
    void whenNoneAfterMapOperation_thenIsNoneReturnsTrue() {
        // When
        Option<String> option = Option.<String>None().map(String::toUpperCase);

        // Then
        assertTrue(option.isNone());
        assertFalse(option.isSome());
    }

    @Test
    void whenSomeAfterFilter_thenIsSomeReturnsTrue() {
        // When
        Option<Integer> option = Option.Some(10).filter(n -> n > 5);

        // Then
        assertTrue(option.isSome());
        assertFalse(option.isNone());
    }
}