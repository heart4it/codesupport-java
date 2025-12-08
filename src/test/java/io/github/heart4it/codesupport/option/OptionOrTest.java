package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionOrTest {

    @Test
    void whenSomeOrSome_thenReturnsFirstSome() {
        // When
        Option<String> first = Option.Some("first");
        Option<String> second = Option.Some("second");
        Option<String> result = first.or(second);

        // Then
        assertTrue(result.isSome());
        assertEquals("first", result.unwrap());
    }

    @Test
    void whenSomeOrNone_thenReturnsFirstSome() {
        // When
        Option<String> first = Option.Some("first");
        Option<String> second = Option.None();
        Option<String> result = first.or(second);

        // Then
        assertTrue(result.isSome());
        assertEquals("first", result.unwrap());
    }

    @Test
    void whenNoneOrSome_thenReturnsSecondSome() {
        // When
        Option<String> first = Option.None();
        Option<String> second = Option.Some("second");
        Option<String> result = first.or(second);

        // Then
        assertTrue(result.isSome());
        assertEquals("second", result.unwrap());
    }

    @Test
    void whenNoneOrNone_thenReturnsNone() {
        // When
        Option<String> first = Option.None();
        Option<String> second = Option.None();
        Option<String> result = first.or(second);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenChainingOrOperations_thenReturnsFirstSome() {
        // When
        Option<String> result = Option.<String>None()
                .or(Option.None())
                .or(Option.Some("first-valid"))
                .or(Option.Some("second-valid"));

        // Then
        assertTrue(result.isSome());
        assertEquals("first-valid", result.unwrap());
    }

    @Test
    void whenAllNoneInChain_thenReturnsNone() {
        // When
        Option<String> result = Option.<String>None()
                .or(Option.None())
                .or(Option.None());

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenOrWithDifferentTypes_thenReturnsFirstSome() {
        // When
        Option<String> stringOption = Option.Some("string");
        Option<Integer> intOption = Option.Some(42);
        Option<String> result = stringOption.or(intOption.map(Object::toString));

        // Then
        assertTrue(result.isSome());
        assertEquals("string", result.unwrap());
    }


    @Test
    void whenOrAfterAndOperation_thenReturnsCorrectValue() {
        // When
        Option<String> result = Option.<String>None()
                .and(Option.Some("and-result"))
                .or(Option.Some("or-fallback"));

        // Then
        assertTrue(result.isSome());
        assertEquals("or-fallback", result.unwrap());
    }
}