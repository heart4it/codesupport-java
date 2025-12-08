package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class OptionFilterTest {

    @Test
    void givenSomeOption_whenFilterPredicateTrue_thenReturnsSome() {
        // Given
        Option<Integer> option = Option.Some(10);
        Predicate<Integer> predicate = n -> n > 5;

        // When
        Option<Integer> result = option.filter(predicate);

        // Then
        assertTrue(result.isSome());
        assertEquals(10, result.unwrap());
    }

    @Test
    void givenSomeOption_whenFilterPredicateFalse_thenReturnsNone() {
        // Given
        Option<Integer> option = Option.Some(3);
        Predicate<Integer> predicate = n -> n > 5;

        // When
        Option<Integer> result = option.filter(predicate);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenNoneOption_whenFilterPredicate_thenReturnsNone() {
        // Given
        Option<Integer> option = Option.None();
        Predicate<Integer> predicate = n -> n > 5;

        // When
        Option<Integer> result = option.filter(predicate);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenSomeOption_whenFilterWithNullCheck_thenReturnsSome() {
        // Given
        Option<String> option = Option.Some("hello");
        Predicate<String> predicate = s -> s != null && !s.isEmpty();

        // When
        Option<String> result = option.filter(predicate);

        // Then
        assertTrue(result.isSome());
        assertEquals("hello", result.unwrap());
    }


    @Test
    void givenSomeOption_whenFilterWithComplexPredicate_thenReturnsFiltered() {
        // Given
        Option<String> option = Option.Some("Hello World");
        Predicate<String> predicate = s -> s.length() > 5 && s.contains("World");

        // When
        Option<String> result = option.filter(predicate);

        // Then
        assertTrue(result.isSome());
        assertEquals("Hello World", result.unwrap());
    }

    @Test
    void givenSomeOption_whenFilterWithTypeSpecificPredicate_thenReturnsFiltered() {
        // Given
        Option<java.util.List<String>> option = Option.Some(java.util.List.of("a", "b", "c"));
        Predicate<java.util.List<String>> predicate = list -> list.size() > 2;

        // When
        Option<java.util.List<String>> result = option.filter(predicate);

        // Then
        assertTrue(result.isSome());
        assertEquals(3, result.unwrap().size());
    }

    @Test
    void givenSomeOption_whenFilterPredicateThrowsException_thenExceptionPropagates() {
        // Given
        Option<String> option = Option.Some("hello");
        Predicate<String> predicate = s -> {
            throw new RuntimeException("Predicate failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> option.filter(predicate));
    }

    @Test
    void givenChainedFilters_whenAllPredicatesTrue_thenReturnsSome() {
        // Given
        Option<Integer> option = Option.Some(15);
        Predicate<Integer> greaterThan10 = n -> n > 10;
        Predicate<Integer> lessThan20 = n -> n < 20;
        Predicate<Integer> even = n -> n % 2 != 0;

        // When
        Option<Integer> result = option
                .filter(greaterThan10)
                .filter(lessThan20)
                .filter(even);

        // Then
        assertTrue(result.isSome());
        assertEquals(15, result.unwrap());
    }

    @Test
    void givenChainedFilters_whenAnyPredicateFalse_thenReturnsNone() {
        // Given
        Option<Integer> option = Option.Some(25);
        Predicate<Integer> greaterThan10 = n -> n > 10;
        Predicate<Integer> lessThan20 = n -> n < 20;

        // When
        Option<Integer> result = option
                .filter(greaterThan10)
                .filter(lessThan20);

        // Then
        assertTrue(result.isNone());
    }
}