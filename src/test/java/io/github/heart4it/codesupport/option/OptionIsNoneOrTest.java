package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionIsNoneOrTest {

    @Test
    void givenSomeOption_whenIsNoneOrPredicateTrue_thenReturnsTrue() {
        // Given
        Option<String> option = Option.Some("hello");
        Predicate<String> predicate = s -> s.length() > 3;

        // When
        boolean result = option.isNoneOr(predicate);

        // Then
        assertTrue(result);
    }

    @Test
    void givenSomeOption_whenIsNoneOrPredicateFalse_thenReturnsFalse() {
        // Given
        Option<String> option = Option.Some("hi");
        Predicate<String> predicate = s -> s.length() > 3;

        // When
        boolean result = option.isNoneOr(predicate);

        // Then
        assertFalse(result);
    }

    @Test
    void givenNoneOption_whenIsNoneOrPredicate_thenReturnsTrue() {
        // Given
        Option<String> option = Option.None();
        Predicate<String> predicate = s -> s.length() > 3;

        // When
        boolean result = option.isNoneOr(predicate);

        // Then
        assertTrue(result);
    }


    @Test
    void givenSomeOptionWithInteger_whenIsNoneOr_thenReturnsCorrectBoolean() {
        // Given
        Option<Integer> option = Option.Some(42);
        Predicate<Integer> predicate = n -> n > 0;

        // When
        boolean result = option.isNoneOr(predicate);

        // Then
        assertTrue(result);
    }

    @Test
    void givenSomeOptionWithInteger_whenIsNoneOrPredicateFalse_thenReturnsFalse() {
        // Given
        Option<Integer> option = Option.Some(-5);
        Predicate<Integer> predicate = n -> n > 0;

        // When
        boolean result = option.isNoneOr(predicate);

        // Then
        assertFalse(result);
    }

    @Test
    void givenSomeOption_whenIsNoneOrWithAlwaysTruePredicate_thenReturnsTrue() {
        // Given
        Option<String> option = Option.Some("any value");
        Predicate<String> predicate = s -> true;

        // When
        boolean result = option.isNoneOr(predicate);

        // Then
        assertTrue(result);
    }

    @Test
    void givenSomeOption_whenIsNoneOrWithAlwaysFalsePredicate_thenReturnsFalse() {
        // Given
        Option<String> option = Option.Some("any value");
        Predicate<String> predicate = s -> false;

        // When
        boolean result = option.isNoneOr(predicate);

        // Then
        assertFalse(result);
    }

    @Test
    void givenNoneOption_whenIsNoneOrWithAlwaysFalsePredicate_thenReturnsTrue() {
        // Given
        Option<String> option = Option.None();
        Predicate<String> predicate = s -> false;

        // When
        boolean result = option.isNoneOr(predicate);

        // Then
        assertTrue(result); // None always returns true regardless of predicate
    }
}