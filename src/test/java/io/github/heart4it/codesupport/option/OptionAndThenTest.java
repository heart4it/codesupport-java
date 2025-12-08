package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionAndThenTest {

    @Test
    void whenSomeAndThenFunctionReturnsSome_thenReturnsSome() {
        // When
        Option<String> option = Option.Some("123");
        Option<String> result = option.andThen(s -> Option.Some(s + "456"));

        // Then
        assertTrue(result.isSome());
        assertEquals("123456", result.unwrap());
    }

    @Test
    void whenSomeAndThenFunctionReturnsNone_thenReturnsNone() {
        // When
        Option<String> option = Option.Some("123");
        Option<String> result = option.andThen(s -> Option.None());

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenNoneAndThenFunction_thenReturnsNone() {
        // When
        Option<String> option = Option.None();
        Option<String> result = option.andThen(s -> Option.Some("mapped"));

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenChainingAndThenOperations_thenReturnsFinalResult() {
        // When
        Option<String> result = Option.Some(" hello ")
                .andThen(s -> Option.Some(s.trim()))
                .andThen(s -> Option.Some(s.toUpperCase()));

        // Then
        assertTrue(result.isSome());
        assertEquals("HELLO", result.unwrap());
    }

    @Test
    void whenChainingAndThenWithNone_thenReturnsNone() {
        // When
        Option<String> result = Option.Some("hello")
                .andThen(s -> Option.None())
                .andThen(s -> Option.Some(s.toUpperCase()));

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenAndThenFunctionReturnsSameOption_thenReturnsOriginal() {
        // When
        Option<String> original = Option.Some("test");
        Option<String> result = original.andThen(s -> original);

        // Then
        assertTrue(result.isSome());
        assertEquals("test", result.unwrap());
    }
}