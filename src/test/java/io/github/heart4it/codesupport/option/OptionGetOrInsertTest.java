package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionGetOrInsertTest {

    @Test
    void givenSomeOption_whenGetOrInsert_thenReturnsExistingValue() {
        // Given
        Option<String> option = Option.Some("existing");
        String newValue = "new";

        // When
        String result = option.getOrInsert(newValue);

        // Then
        assertEquals("existing", result);
    }

    @Test
    void givenNoneOption_whenGetOrInsert_thenReturnsNewValue() {
        // Given
        Option<String> option = Option.None();
        String newValue = "new";

        // When
        String result = option.getOrInsert(newValue);

        // Then
        assertEquals("new", result);
    }


    @Test
    void givenNoneOption_whenGetOrInsertWithNull_thenReturnsNull() {
        // Given
        Option<String> option = Option.None();
        String newValue = null;

        // When
        String result = option.getOrInsert(newValue);

        // Then
        assertNull(result);
    }

    @Test
    void givenSomeOptionWithInteger_whenGetOrInsert_thenReturnsExistingInteger() {
        // Given
        Option<Integer> option = Option.Some(42);
        Integer newValue = 100;

        // When
        Integer result = option.getOrInsert(newValue);

        // Then
        assertEquals(42, result);
    }

    @Test
    void givenNoneOptionWithInteger_whenGetOrInsert_thenReturnsNewInteger() {
        // Given
        Option<Integer> option = Option.None();
        Integer newValue = 100;

        // When
        Integer result = option.getOrInsert(newValue);

        // Then
        assertEquals(100, result);
    }

    @Test
    void givenSomeOptionAfterMap_whenGetOrInsert_thenReturnsMappedValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);
        String newValue = "new";

        // When
        String result = option.getOrInsert(newValue);

        // Then
        assertEquals("HELLO", result);
    }

    @Test
    void givenNoneOptionAfterMap_whenGetOrInsert_thenReturnsNewValue() {
        // Given
        Option<String> option = Option.<String>None().map(String::toUpperCase);
        String newValue = "new";

        // When
        String result = option.getOrInsert(newValue);

        // Then
        assertEquals("new", result);
    }

    @Test
    void givenSomeOption_whenGetOrInsertThenOptionUnchanged_thenOriginalPreserved() {
        // Given
        Option<String> original = Option.Some("original");
        String newValue = "new";

        // When
        String result = original.getOrInsert(newValue);

        // Then
        assertEquals("original", result);
        assertEquals("original", original.unwrap()); // Original unchanged
    }

    @Test
    void givenNoneOption_whenGetOrInsertThenOptionUnchanged_thenStillNone() {
        // Given
        Option<String> original = Option.None();
        String newValue = "new";

        // When
        String result = original.getOrInsert(newValue);

        // Then
        assertEquals("new", result);
        assertTrue(original.isNone()); // Still None (immutable)
    }
}