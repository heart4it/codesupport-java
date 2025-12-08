package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionUnwrapOrTest {

    @Test
    void givenSomeOption_whenUnwrapOr_thenReturnsValue() {
        // Given
        Option<String> option = Option.Some("hello");
        String defaultValue = "default";

        // When
        String result = option.unwrapOr(defaultValue);

        // Then
        assertEquals("hello", result);
    }

    @Test
    void givenNoneOption_whenUnwrapOr_thenReturnsDefault() {
        // Given
        Option<String> option = Option.None();
        String defaultValue = "default";

        // When
        String result = option.unwrapOr(defaultValue);

        // Then
        assertEquals("default", result);
    }

    @Test
    void givenSomeOptionWithNone_whenUnwrapOr_thenReturnsDefault() {
        // Given
        Option<String> option = Option.None();
        String defaultValue = "default";

        // When
        String result = option.unwrapOr(defaultValue);

        // Then
        assertEquals(defaultValue, result);
    }

    @Test
    void givenNoneOption_whenUnwrapOrWithNullDefault_thenReturnsNull() {
        // Given
        Option<String> option = Option.None();
        String defaultValue = null;

        // When
        String result = option.unwrapOr(defaultValue);

        // Then
        assertNull(result);
    }

    @Test
    void givenSomeOption_whenUnwrapOrWithDifferentType_thenReturnsValue() {
        // Given
        Option<Integer> option = Option.Some(42);
        Integer defaultValue = -1;

        // When
        Integer result = option.unwrapOr(defaultValue);

        // Then
        assertEquals(42, result);
    }

    @Test
    void givenNoneOption_whenUnwrapOrWithDifferentType_thenReturnsDefault() {
        // Given
        Option<Integer> option = Option.None();
        Integer defaultValue = -1;

        // When
        Integer result = option.unwrapOr(defaultValue);

        // Then
        assertEquals(-1, result);
    }

    @Test
    void givenSomeOptionAfterFilter_whenUnwrapOr_thenReturnsValue() {
        // Given
        Option<Integer> option = Option.Some(10).filter(n -> n > 5);
        Integer defaultValue = 0;

        // When
        Integer result = option.unwrapOr(defaultValue);

        // Then
        assertEquals(10, result);
    }

    @Test
    void givenNoneOptionAfterFilter_whenUnwrapOr_thenReturnsDefault() {
        // Given
        Option<Integer> option = Option.Some(3).filter(n -> n > 5);
        Integer defaultValue = 0;

        // When
        Integer result = option.unwrapOr(defaultValue);

        // Then
        assertEquals(0, result);
    }

    @Test
    void givenSomeOption_whenUnwrapOrWithObject_thenReturnsSameObject() {
        // Given
        Object expected = new Object();
        Option<Object> option = Option.Some(expected);
        Object defaultValue = new Object();

        // When
        Object result = option.unwrapOr(defaultValue);

        // Then
        assertSame(expected, result);
    }

    @Test
    void givenNoneOption_whenUnwrapOrWithObject_thenReturnsDefaultObject() {
        // Given
        Option<Object> option = Option.None();
        Object defaultValue = new Object();

        // When
        Object result = option.unwrapOr(defaultValue);

        // Then
        assertSame(defaultValue, result);
    }
}