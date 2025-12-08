package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class OptionIsSomeAndTest {

    @Test
    void givenSomeOption_whenIsSomeAndPredicateTrue_thenReturnsTrue() {
        // Given
        Option<String> option = Option.Some("hello");
        Predicate<String> predicate = s -> s.length() > 3;

        // When
        boolean result = option.isSomeAnd(predicate);

        // Then
        assertTrue(result);
    }

    @Test
    void givenSomeOption_whenIsSomeAndPredicateFalse_thenReturnsFalse() {
        // Given
        Option<String> option = Option.Some("hi");
        Predicate<String> predicate = s -> s.length() > 3;

        // When
        boolean result = option.isSomeAnd(predicate);

        // Then
        assertFalse(result);
    }

    @Test
    void givenNoneOption_whenIsSomeAndPredicate_thenReturnsFalse() {
        // Given
        Option<String> option = Option.None();
        Predicate<String> predicate = s -> s.length() > 3;

        // When
        boolean result = option.isSomeAnd(predicate);

        // Then
        assertFalse(result);
    }


    @Test
    void givenSomeOption_whenIsSomeAndWithComplexPredicate_thenReturnsCorrectBoolean() {
        // Given
        Option<Integer> option = Option.Some(42);
        Predicate<Integer> predicate = n -> n > 0 && n % 2 == 0 && n < 100;

        // When
        boolean result = option.isSomeAnd(predicate);

        // Then
        assertTrue(result);
    }

    @Test
    void givenSomeOption_whenIsSomeAndWithAlwaysTruePredicate_thenReturnsTrue() {
        // Given
        Option<String> option = Option.Some("any value");
        Predicate<String> predicate = s -> true;

        // When
        boolean result = option.isSomeAnd(predicate);

        // Then
        assertTrue(result);
    }

    @Test
    void givenSomeOption_whenIsSomeAndWithAlwaysFalsePredicate_thenReturnsFalse() {
        // Given
        Option<String> option = Option.Some("any value");
        Predicate<String> predicate = s -> false;

        // When
        boolean result = option.isSomeAnd(predicate);

        // Then
        assertFalse(result);
    }

    @Test
    void givenNoneOption_whenIsSomeAndWithAlwaysTruePredicate_thenReturnsFalse() {
        // Given
        Option<String> option = Option.None();
        Predicate<String> predicate = s -> true;

        // When
        boolean result = option.isSomeAnd(predicate);

        // Then
        assertFalse(result);
    }

    @Test
    void givenSomeOption_whenIsSomeAndPredicateThrowsException_thenExceptionPropagates() {
        // Given
        Option<String> option = Option.Some("hello");
        Predicate<String> predicate = s -> {
            throw new RuntimeException("Predicate failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> option.isSomeAnd(predicate));
    }

    @Test
    void givenSomeOptionAfterOperations_whenIsSomeAnd_thenReturnsCorrectBoolean() {
        // Given
        Option<String> option = Option.Some(" hello ")
                .map(String::trim)
                .filter(s -> !s.isEmpty());
        Predicate<String> predicate = s -> s.equals("hello");

        // When
        boolean result = option.isSomeAnd(predicate);

        // Then
        assertTrue(result);
    }
}