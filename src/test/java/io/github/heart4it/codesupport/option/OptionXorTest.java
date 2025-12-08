package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionXorTest {

    @Test
    void whenSomeXorNone_thenReturnsSome() {
        // When
        Option<String> some = Option.Some("value");
        Option<String> none = Option.None();
        Option<String> result = some.xor(none);

        // Then
        assertTrue(result.isSome());
        assertEquals("value", result.unwrap());
    }

    @Test
    void whenNoneXorSome_thenReturnsSome() {
        // When
        Option<String> none = Option.None();
        Option<String> some = Option.Some("value");
        Option<String> result = none.xor(some);

        // Then
        assertTrue(result.isSome());
        assertEquals("value", result.unwrap());
    }

    @Test
    void whenSomeXorSome_thenReturnsNone() {
        // When
        Option<String> first = Option.Some("first");
        Option<String> second = Option.Some("second");
        Option<String> result = first.xor(second);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenNoneXorNone_thenReturnsNone() {
        // When
        Option<String> first = Option.None();
        Option<String> second = Option.None();
        Option<String> result = first.xor(second);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenSomeXorSomeWithDifferentTypes_thenReturnsNone() {
        // When
        Option<String> stringSome = Option.Some("hello");
        Option<Integer> intSome = Option.Some(42);
        Option<String> result = stringSome.xor(intSome.map(Object::toString));

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenChainingXorOperations_thenReturnsExclusiveSome() {
        // When
        Option<String> result = Option.<String>None()
                .xor(Option.None())  // None xor None = None
                .xor(Option.Some("exclusive")); // None xor Some = Some

        // Then
        assertTrue(result.isSome());
        assertEquals("exclusive", result.unwrap());
    }

    @Test
    void whenMultipleSomeInChain_thenReturnsNone() {
        // When
        Option<String> result = Option.Some("first")
                .xor(Option.Some("second")) // Some xor Some = None
                .xor(Option.Some("third")); // None xor Some = Some

        // Then
        assertTrue(result.isSome());
        assertEquals("third", result.unwrap());
    }

    @Test
    void whenXorWithSameSomeValues_thenReturnsNone() {
        // When
        Option<String> first = Option.Some("same");
        Option<String> second = Option.Some("same");
        Option<String> result = first.xor(second);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenXorAfterOtherOperations_thenReturnsCorrectResult() {
        // When
        Option<String> result = Option.Some("hello")
                .map(String::toUpperCase) // Some("HELLO")
                .xor(Option.<String>None().or(Option.Some("fallback"))); // Some("fallback")

        // Then
        assertTrue(result.isNone()); // Both are Some
    }
}