package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionAndTest {

    @Test
    void whenSomeAndSome_thenReturnsSecondSome() {
        // When
        Option<String> first = Option.Some("first");
        Option<String> second = Option.Some("second");
        Option<String> result = first.and(second);

        // Then
        assertTrue(result.isSome());
        assertEquals("second", result.unwrap());
    }

    @Test
    void whenSomeAndNone_thenReturnsNone() {
        // When
        Option<String> first = Option.Some("first");
        Option<String> second = Option.None();
        Option<String> result = first.and(second);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenNoneAndSome_thenReturnsNone() {
        // When
        Option<String> first = Option.None();
        Option<String> second = Option.Some("second");
        Option<String> result = first.and(second);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenNoneAndNone_thenReturnsNone() {
        // When
        Option<String> first = Option.None();
        Option<String> second = Option.None();
        Option<String> result = first.and(second);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenChainingAndOperations_thenReturnsLastSome() {
        // When
        Option<String> result = Option.Some("start")
                .and(Option.Some("middle"))
                .and(Option.Some("end"));

        // Then
        assertTrue(result.isSome());
        assertEquals("end", result.unwrap());
    }

    @Test
    void whenChainingAndWithNone_thenReturnsNone() {
        // When
        Option<String> result = Option.Some("start")
                .and(Option.None())
                .and(Option.Some("end"));

        // Then
        assertTrue(result.isNone());
    }

}