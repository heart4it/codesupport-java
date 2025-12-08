package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.isOkAnd() method
 */
public class ResultIsOkAndTest {

    @Test
    void isOkAnd_WithOkValueSatisfyingPredicate_ShouldReturnTrue() {
        // Given
        Result<String, Integer> okResult = Result.ok("hello");
        Predicate<String> predicate = s -> s.length() == 5;

        // When
        boolean result = okResult.isOkAnd(predicate);

        // Then
        assertTrue(result);
    }

    @Test
    void isOkAnd_WithOkValueNotSatisfyingPredicate_ShouldReturnFalse() {
        // Given
        Result<String, Integer> okResult = Result.ok("hello");
        Predicate<String> predicate = s -> s.length() > 10;

        // When
        boolean result = okResult.isOkAnd(predicate);

        // Then
        assertFalse(result);
    }

    @Test
    void isOkAnd_WithErrResult_ShouldReturnFalse() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        Predicate<String> predicate = s -> true; // Always true predicate

        // When
        boolean result = errResult.isOkAnd(predicate);

        // Then
        assertFalse(result);
    }

    @Test
    void isOkAnd_WithNullOkValueAndNullSafePredicate_ShouldWork() {
        // Given
        Result<String, Integer> okResult = Result.ok(null);
        Predicate<String> predicate = s -> s == null;

        // When
        boolean result = okResult.isOkAnd(predicate);

        // Then
        assertTrue(result);
    }

    @Test
    void isOkAnd_WithIntegerValueAndNumericPredicate_ShouldWork() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);
        Predicate<Integer> predicate = n -> n > 0 && n < 100;

        // When
        boolean result = okResult.isOkAnd(predicate);

        // Then
        assertTrue(result);
    }

    @Test
    void isOkAnd_WithBooleanValueAndIdentityPredicate_ShouldWork() {
        // Given
        Result<Boolean, String> okResult = Result.ok(true);
        Predicate<Boolean> predicate = b -> b;

        // When
        boolean result = okResult.isOkAnd(predicate);

        // Then
        assertTrue(result);
    }

    @Test
    void isOkAnd_WithEmptyStringAndLengthPredicate_ShouldReturnFalse() {
        // Given
        Result<String, Integer> okResult = Result.ok("");
        Predicate<String> predicate = s -> s.length() > 0;

        // When
        boolean result = okResult.isOkAnd(predicate);

        // Then
        assertFalse(result);
    }

    @Test
    void isOkAnd_WithPredicateThatThrowsException_ShouldPropagateException() {
        // Given
        Result<String, Integer> okResult = Result.ok("test");
        Predicate<String> predicate = s -> {
            if (s.equals("test")) {
                throw new RuntimeException("Test exception");
            }
            return true;
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> okResult.isOkAnd(predicate));
    }

    @Test
    void isOkAnd_WithListValueAndCollectionPredicate_ShouldWork() {
        // Given
        java.util.List<String> list = java.util.List.of("a", "b", "c");
        Result<java.util.List<String>, Integer> okResult = Result.ok(list);
        Predicate<java.util.List<String>> predicate = l -> l.size() == 3 && l.contains("b");

        // When
        boolean result = okResult.isOkAnd(predicate);

        // Then
        assertTrue(result);
    }
}