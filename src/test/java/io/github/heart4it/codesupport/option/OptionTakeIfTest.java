package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class OptionTakeIfTest {

    @Test
    void givenSomeOption_whenTakeIfPredicateTrue_thenReturnsSome() {
        // Given
        Option<Integer> option = Option.Some(10);
        Predicate<Integer> predicate = n -> n > 5;

        // When
        Option<Integer> result = option.takeIf(predicate);

        // Then
        assertTrue(result.isSome());
        assertEquals(10, result.unwrap());
    }

    @Test
    void givenSomeOption_whenTakeIfPredicateFalse_thenReturnsNone() {
        // Given
        Option<Integer> option = Option.Some(3);
        Predicate<Integer> predicate = n -> n > 5;

        // When
        Option<Integer> result = option.takeIf(predicate);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenNoneOption_whenTakeIfPredicate_thenReturnsNone() {
        // Given
        Option<Integer> option = Option.None();
        Predicate<Integer> predicate = n -> n > 5;

        // When
        Option<Integer> result = option.takeIf(predicate);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenSomeOptionWithString_whenTakeIfLengthCheck_thenReturnsFiltered() {
        // Given
        Option<String> option = Option.Some("hello");
        Predicate<String> predicate = s -> s.length() > 3;

        // When
        Option<String> result = option.takeIf(predicate);

        // Then
        assertTrue(result.isSome());
        assertEquals("hello", result.unwrap());
    }

    @Test
    void givenSomeOptionWithString_whenTakeIfLengthCheckFalse_thenReturnsNone() {
        // Given
        Option<String> option = Option.Some("hi");
        Predicate<String> predicate = s -> s.length() > 3;

        // When
        Option<String> result = option.takeIf(predicate);

        // Then
        assertTrue(result.isNone());
    }


    @Test
    void givenSomeOption_whenTakeIfWithComplexPredicate_thenReturnsFiltered() {
        // Given
        Option<String> option = Option.Some("Hello123");
        Predicate<String> predicate = s -> s.matches("^[A-Za-z0-9]+$");

        // When
        Option<String> result = option.takeIf(predicate);

        // Then
        assertTrue(result.isSome());
        assertEquals("Hello123", result.unwrap());
    }

    @Test
    void givenSomeOption_whenTakeIfPredicateThrowsException_thenExceptionPropagates() {
        // Given
        Option<String> option = Option.Some("hello");
        Predicate<String> predicate = s -> {
            throw new RuntimeException("Predicate failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> option.takeIf(predicate));
    }

    @Test
    void givenChainedTakeIfOperations_whenAllTrue_thenReturnsSome() {
        // Given
        Option<Integer> option = Option.Some(15);
        Predicate<Integer> greaterThan10 = n -> n > 10;
        Predicate<Integer> lessThan20 = n -> n < 20;

        // When
        Option<Integer> result = option
                .takeIf(greaterThan10)
                .takeIf(lessThan20);

        // Then
        assertTrue(result.isSome());
        assertEquals(15, result.unwrap());
    }

    @Test
    void givenChainedTakeIfOperations_whenAnyFalse_thenReturnsNone() {
        // Given
        Option<Integer> option = Option.Some(25);
        Predicate<Integer> greaterThan10 = n -> n > 10;
        Predicate<Integer> lessThan20 = n -> n < 20;

        // When
        Option<Integer> result = option
                .takeIf(greaterThan10)
                .takeIf(lessThan20);

        // Then
        assertTrue(result.isNone());
    }
}