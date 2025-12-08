package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class OptionSomeNoneTest {

    @Test
    void whenSomeCreatedWithNonNullValue_thenValueIsStored() {
        // When
        Option<String> option = Option.Some("test");

        // Then
        assertTrue(option.isSome());
        assertEquals("test", option.unwrap());
    }

    @Test
    void whenSomeCreatedWithNullValue_thenThrows() {
        assertThrows(NullPointerException.class,
                () -> {
                    Option.Some(null);
                });
    }

    @Test
    void whenSomeCreatedWithInteger_thenValueIsStored() {
        // When
        Option<Integer> option = Option.Some(42);

        // Then
        assertTrue(option.isSome());
        assertEquals(42, option.unwrap());
    }

    @Test
    void whenSomeCreatedWithBoolean_thenValueIsStored() {
        // When
        Option<Boolean> option = Option.Some(true);

        // Then
        assertTrue(option.isSome());
        assertTrue(option.unwrap());
    }

    @Test
    void whenSomeCreatedWithList_thenValueIsStored() {
        // When
        Option<java.util.List<String>> option = Option.Some(java.util.List.of("a", "b"));

        // Then
        assertTrue(option.isSome());
        assertEquals(2, option.unwrap().size());
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
    void whenNoneCreated_thenUnwrapThrowsException() {
        // When
        Option<String> option = Option.None();

        // Then
        assertThrows(NoSuchElementException.class, option::unwrap);
    }

    @Test
    void whenNoneCreatedWithDifferentTypes_thenAllAreNone() {
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
    void whenSomeAndNoneCompared_thenTheyAreDifferent() {
        // When
        Option<String> some = Option.Some("value");
        Option<String> none = Option.None();

        // Then
        assertNotEquals(some, none);
        assertNotEquals(some.hashCode(), none.hashCode());
    }

    @Test
    void whenMultipleSomeCreated_thenTheyContainCorrectValues() {
        // When
        Option<String> some1 = Option.Some("first");
        Option<String> some2 = Option.Some("second");

        // Then
        assertEquals("first", some1.unwrap());
        assertEquals("second", some2.unwrap());
    }
}