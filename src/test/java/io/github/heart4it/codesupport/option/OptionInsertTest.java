package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionInsertTest {

    @Test
    void givenSomeOption_whenInsert_thenReturnsSomeWithNewValue() {
        // Given
        Option<String> option = Option.Some("old");
        String newValue = "new";

        // When
        Option<String> result = option.insert(newValue);

        // Then
        assertTrue(result.isSome());
        assertEquals("new", result.unwrap());
    }

    @Test
    void givenNoneOption_whenInsert_thenReturnsSomeWithNewValue() {
        // Given
        Option<String> option = Option.None();
        String newValue = "new";

        // When
        Option<String> result = option.insert(newValue);

        // Then
        assertTrue(result.isSome());
        assertEquals("new", result.unwrap());
    }

    @Test
    void givenNoneOption_whenInsertWithNull_ShouldThrow() {
        // Given
        Option<String> option = Option.None();
        String newValue = null;

        // When
        assertThrows(NullPointerException.class,
                () -> {
                    option.insert(newValue);
                });

    }

    @Test
    void givenSomeOptionWithInteger_whenInsert_thenReturnsSomeWithNewInteger() {
        // Given
        Option<Integer> option = Option.Some(10);
        Integer newValue = 20;

        // When
        Option<Integer> result = option.insert(newValue);

        // Then
        assertTrue(result.isSome());
        assertEquals(20, result.unwrap());
    }

    @Test
    void givenNoneOptionWithInteger_whenInsert_thenReturnsSomeWithNewInteger() {
        // Given
        Option<Integer> option = Option.None();
        Integer newValue = 20;

        // When
        Option<Integer> result = option.insert(newValue);

        // Then
        assertTrue(result.isSome());
        assertEquals(20, result.unwrap());
    }

    @Test
    void givenSomeOption_whenInsertAfterMap_thenReturnsSomeWithNewValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);
        String newValue = "inserted";

        // When
        Option<String> result = option.insert(newValue);

        // Then
        assertTrue(result.isSome());
        assertEquals("inserted", result.unwrap());
    }

    @Test
    void givenNoneOption_whenInsertAfterMap_thenReturnsSomeWithNewValue() {
        // Given
        Option<String> option = Option.<String>None().map(String::toUpperCase);
        String newValue = "inserted";

        // When
        Option<String> result = option.insert(newValue);

        // Then
        assertTrue(result.isSome());
        assertEquals("inserted", result.unwrap());
    }

    @Test
    void givenSomeOption_whenInsertWithDifferentType_thenReturnsSomeWithNewType() {
        // Given
        Option<String> option = Option.Some("old");
        String newValue = "new";

        // When
        Option<String> result = option.insert(newValue);

        // Then
        assertTrue(result.isSome());
        assertEquals(newValue, result.unwrap());
    }

    @Test
    void givenChainedInsertOperations_thenReturnsLastInsertedValue() {
        // Given
        Option<String> option = Option.Some("first");

        // When
        Option<String> result = option
                .insert("second")
                .insert("third");

        // Then
        assertTrue(result.isSome());
        assertEquals("third", result.unwrap());
    }
}