package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionToDebugStringTest {

    @Test
    void givenSomeOptionWithString_whenToDebugString_thenReturnsSomeWithValue() {
        // Given
        Option<String> option = Option.Some("hello");

        // When
        String result = option.toDebugString();

        // Then
        assertEquals("Some(hello)", result);
    }


    @Test
    void givenSomeOptionWithInteger_whenToDebugString_thenReturnsSomeWithInteger() {
        // Given
        Option<Integer> option = Option.Some(42);

        // When
        String result = option.toDebugString();

        // Then
        assertEquals("Some(42)", result);
    }

    @Test
    void givenNoneOption_whenToDebugString_thenReturnsNone() {
        // Given
        Option<String> option = Option.None();

        // When
        String result = option.toDebugString();

        // Then
        assertEquals("None", result);
    }

    @Test
    void givenSomeOptionAfterMap_whenToDebugString_thenReturnsSomeWithMappedValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);

        // When
        String result = option.toDebugString();

        // Then
        assertEquals("Some(HELLO)", result);
    }

    @Test
    void givenNoneOptionAfterMap_whenToDebugString_thenReturnsNone() {
        // Given
        Option<String> option = Option.<String>None().map(String::toUpperCase);

        // When
        String result = option.toDebugString();

        // Then
        assertEquals("None", result);
    }

    @Test
    void givenSomeOptionAfterFilter_whenToDebugString_thenReturnsSomeWithFilteredValue() {
        // Given
        Option<Integer> option = Option.Some(10).filter(n -> n > 5);

        // When
        String result = option.toDebugString();

        // Then
        assertEquals("Some(10)", result);
    }

    @Test
    void givenNoneOptionAfterFilter_whenToDebugString_thenReturnsNone() {
        // Given
        Option<Integer> option = Option.Some(3).filter(n -> n > 5);

        // When
        String result = option.toDebugString();

        // Then
        assertEquals("None", result);
    }

    @Test
    void givenSomeOptionWithObject_whenToDebugString_thenReturnsSomeWithToString() {
        // Given
        Option<TestObject> option = Option.Some(new TestObject("test"));

        // When
        String result = option.toDebugString();

        // Then
        assertEquals("Some(TestObject{name='test'})", result);
    }

    @Test
    void givenChainedOperations_whenToDebugString_thenReturnsFinalState() {
        // Given
        Option<String> option = Option.Some(" hello ")
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase);

        // When
        String result = option.toDebugString();

        // Then
        assertEquals("Some(HELLO)", result);
    }

    static class TestObject {
        private final String name;

        TestObject(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "TestObject{name='" + name + "'}";
        }
    }
}