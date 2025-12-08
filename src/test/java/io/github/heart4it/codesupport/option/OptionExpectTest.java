package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class OptionExpectTest {

    @Test
    void givenSomeOption_whenExpect_thenReturnsContainedValue() {
        // Given
        Option<String> option = Option.Some("success");
        String message = "Value should be present";

        // When
        String result = option.expect(message);

        // Then
        assertEquals("success", result);
    }


    @Test
    void givenNoneOption_whenExpect_thenThrowsNoSuchElementExceptionWithMessage() {
        // Given
        Option<String> option = Option.None();
        String message = "Data should be present";

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> option.expect(message));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void givenSomeOptionWithInteger_whenExpect_thenReturnsIntegerValue() {
        // Given
        Option<Integer> option = Option.Some(42);
        String message = "Number should be present";

        // When
        Integer result = option.expect(message);

        // Then
        assertEquals(42, result);
    }

    @Test
    void givenSomeOptionAfterMapOperation_whenExpect_thenReturnsMappedValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);
        String message = "Value should be present";

        // When
        String result = option.expect(message);

        // Then
        assertEquals("HELLO", result);
    }

    @Test
    void givenSomeOptionAfterFilterOperation_whenExpect_thenReturnsFilteredValue() {
        // Given
        Option<Integer> option = Option.Some(10).filter(n -> n > 5);
        String message = "Value should be present";

        // When
        Integer result = option.expect(message);

        // Then
        assertEquals(10, result);
    }

    @Test
    void givenNoneOptionAfterFilterOperation_whenExpect_thenThrowsExceptionWithMessage() {
        // Given
        Option<Integer> option = Option.Some(3).filter(n -> n > 5);
        String message = "Filtered value should be present";

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> option.expect(message));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void givenSomeOptionWithCustomObject_whenExpect_thenReturnsSameObject() {
        // Given
        CustomObject expected = new CustomObject("test");
        Option<CustomObject> option = Option.Some(expected);
        String message = "Object should be present";

        // When
        CustomObject result = option.expect(message);

        // Then
        assertSame(expected, result);
    }

    @Test
    void givenSomeOptionAfterAndThenOperation_whenExpect_thenReturnsFinalValue() {
        // Given
        Option<String> option = Option.Some("123")
                .andThen(s -> Option.Some(s + "456"));
        String message = "Value should be present";

        // When
        String result = option.expect(message);

        // Then
        assertEquals("123456", result);
    }

    @Test
    void givenNoneOptionAfterAndThenOperation_whenExpect_thenThrowsException() {
        // Given
        Option<String> option = Option.Some("123")
                .andThen(s -> Option.None());
        String message = "AndThen should produce value";

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> option.expect(message));
        assertEquals(message, exception.getMessage());
    }

    // Helper class for test
    static class CustomObject {
        private final String name;

        public CustomObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}