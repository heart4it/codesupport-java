package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

class OptionZipWithTest {

    @Test
    void givenTwoSomeOptions_whenZipWith_thenReturnsSomeWithCombinedValue() {
        // Given
        Option<String> first = Option.Some("hello");
        Option<Integer> second = Option.Some(3);
        BiFunction<String, Integer, String> combiner = (s, n) -> s.repeat(n);

        // When
        Option<String> result = Option.zipWith(first, second, combiner);

        // Then
        assertTrue(result.isSome());
        assertEquals("hellohellohello", result.unwrap());
    }

    @Test
    void givenFirstNone_whenZipWith_thenReturnsNone() {
        // Given
        Option<String> first = Option.None();
        Option<Integer> second = Option.Some(3);
        BiFunction<String, Integer, String> combiner = (s, n) -> s.repeat(n);

        // When
        Option<String> result = Option.zipWith(first, second, combiner);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenSecondNone_whenZipWith_thenReturnsNone() {
        // Given
        Option<String> first = Option.Some("hello");
        Option<Integer> second = Option.None();
        BiFunction<String, Integer, String> combiner = (s, n) -> s.repeat(n);

        // When
        Option<String> result = Option.zipWith(first, second, combiner);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenBothNone_whenZipWith_thenReturnsNone() {
        // Given
        Option<String> first = Option.None();
        Option<Integer> second = Option.None();
        BiFunction<String, Integer, String> combiner = (s, n) -> s.repeat(n);

        // When
        Option<String> result = Option.zipWith(first, second, combiner);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenTwoSomeWithTypeChange_whenZipWith_thenReturnsNewType() {
        // Given
        Option<String> first = Option.Some("John");
        Option<String> second = Option.Some("Doe");
        BiFunction<String, String, String> combiner = (f, l) -> f + " " + l;

        // When
        Option<String> result = Option.zipWith(first, second, combiner);

        // Then
        assertTrue(result.isSome());
        assertEquals("John Doe", result.unwrap());
    }

    @Test
    void givenTwoSomeWithComplexCombiner_whenZipWith_thenReturnsCombinedResult() {
        // Given
        Option<Integer> first = Option.Some(5);
        Option<Integer> second = Option.Some(3);
        BiFunction<Integer, Integer, java.util.List<Integer>> combiner = (a, b) ->
                java.util.List.of(a + b, a - b, a * b);

        // When
        Option<java.util.List<Integer>> result = Option.zipWith(first, second, combiner);

        // Then
        assertTrue(result.isSome());
        java.util.List<Integer> list = result.unwrap();
        assertEquals(8, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(15, list.get(2));
    }

    @Test
    void givenSomeAfterOperations_whenZipWith_thenReturnsCombinedResult() {
        // Given
        Option<String> first = Option.Some(" hello ").map(String::trim);
        Option<Integer> second = Option.Some(2).filter(n -> n > 0);
        BiFunction<String, Integer, String> combiner = (s, n) -> s.repeat(n);

        // When
        Option<String> result = Option.zipWith(first, second, combiner);

        // Then
        assertTrue(result.isSome());
        assertEquals("hellohello", result.unwrap());
    }

    @Test
    void givenZipWithCombinerThrowsException_whenZipWith_thenExceptionPropagates() {
        // Given
        Option<String> first = Option.Some("hello");
        Option<Integer> second = Option.Some(2);
        BiFunction<String, Integer, String> combiner = (s, n) -> {
            throw new RuntimeException("Combiner failed");
        };

        // When & Then
        assertThrows(RuntimeException.class,
                () -> Option.zipWith(first, second, combiner));
    }

    @Test
    void givenZipWithReturnsNull_whenZipWithReturnNullThenThrow() {
        // Given
        Option<String> first = Option.Some("hello");
        Option<Integer> second = Option.Some(2);
        BiFunction<String, Integer, String> combiner = (s, n) -> null;

        // When
        assertThrows(NullPointerException.class,
                () -> {
                    Option.zipWith(first, second, combiner);
                });


    }
}