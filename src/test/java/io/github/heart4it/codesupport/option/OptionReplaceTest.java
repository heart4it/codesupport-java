package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionReplaceTest {

    @Test
    void givenSomeOption_whenReplace_thenReturnsSomeWithNewValue() {
        // Given
        Option<String> option = Option.Some("old");
        String newValue = "new";

        // When
        Option<String> result = option.replace(newValue);

        // Then
        assertTrue(result.isSome());
        assertEquals("new", result.unwrap());
    }

    @Test
    void givenNoneOption_whenReplace_thenReturnsNone() {
        // Given
        Option<String> option = Option.None();
        String newValue = "new";

        // When
        Option<String> result = option.replace(newValue);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenSomeOption_whenReplaceWithNullThrow() {
        // Given
        Option<String> option = Option.Some("old");
        String newValue = null;

        assertThrows(NullPointerException.class,
                () -> {
                    option.replace(newValue);
                });

    }

    @Test
    void givenSomeOptionWithInteger_whenReplace_thenReturnsSomeWithNewInteger() {
        // Given
        Option<Integer> option = Option.Some(10);
        Integer newValue = 20;

        // When
        Option<Integer> result = option.replace(newValue);

        // Then
        assertTrue(result.isSome());
        assertEquals(20, result.unwrap());
    }

    @Test
    void givenSomeOption_whenReplaceAfterMap_thenReturnsSomeWithNewValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);
        String newValue = "replaced";

        // When
        Option<String> result = option.replace(newValue);

        // Then
        assertTrue(result.isSome());
        assertEquals("replaced", result.unwrap());
    }

    @Test
    void givenNoneOption_whenReplaceAfterMap_thenReturnsNone() {
        // Given
        Option<String> option = Option.<String>None().map(String::toUpperCase);
        String newValue = "replaced";

        // When
        Option<String> result = option.replace(newValue);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenSomeOption_whenReplaceThenOriginalUnchanged_thenOriginalPreserved() {
        // Given
        Option<String> original = Option.Some("original");
        String newValue = "new";

        // When
        Option<String> result = original.replace(newValue);

        // Then
        assertEquals("new", result.unwrap());
        assertEquals("original", original.unwrap()); // Original unchanged
    }

    @Test
    void givenSomeOption_whenChainedReplace_thenReturnsLastReplacedValue() {
        // Given
        Option<String> option = Option.Some("first");

        // When
        Option<String> result = option
                .replace("second")
                .replace("third");

        // Then
        assertTrue(result.isSome());
        assertEquals("third", result.unwrap());
    }

    @Test
    void givenNoneOption_whenChainedReplace_thenReturnsNone() {
        // Given
        Option<String> option = Option.None();

        // When
        Option<String> result = option
                .replace("first")
                .replace("second");

        // Then
        assertTrue(result.isNone());
    }
}