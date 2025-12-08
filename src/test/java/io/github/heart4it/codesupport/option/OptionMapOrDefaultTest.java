package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class OptionMapOrDefaultTest {

    @Test
    void givenSomeOption_whenMapOrDefault_thenReturnsMappedValue() {
        // Given
        Option<String> option = Option.Some("hello");
        Supplier<String> defaultSupplier = () -> "default";
        Function<String, String> mapper = String::toUpperCase;

        // When
        String result = option.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals("HELLO", result);
    }

    @Test
    void givenNoneOption_whenMapOrDefault_thenReturnsDefaultValue() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> "default";
        Function<String, String> mapper = String::toUpperCase;

        // When
        String result = option.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals("default", result);
    }

    @Test
    void givenSomeOption_whenMapOrDefaultWithComplexMapper_thenReturnsMappedValue() {
        // Given
        Option<Integer> option = Option.Some(5);
        Supplier<String> defaultSupplier = () -> "default";
        Function<Integer, String> mapper = n -> "Count: " + n;

        // When
        String result = option.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals("Count: 5", result);
    }

    @Test
    void givenNoneOption_whenMapOrDefaultWithLazySupplier_thenSupplierCalled() {
        // Given
        Option<String> option = Option.None();
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "computed-default";
        };
        Function<String, String> mapper = String::toUpperCase;

        // When
        String result = option.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals("computed-default", result);
        assertTrue(supplierCalled[0]);
    }

    @Test
    void givenSomeOption_whenMapOrDefaultWithLazySupplier_thenSupplierNotCalled() {
        // Given
        Option<String> option = Option.Some("hello");
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "computed-default";
        };
        Function<String, String> mapper = String::toUpperCase;

        // When
        String result = option.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals("HELLO", result);
        assertFalse(supplierCalled[0]);
    }

    @Test
    void givenSomeOption_whenMapOrDefaultReturnsNull_thenReturnsNull() {
        // Given
        Option<String> option = Option.Some("hello");
        Supplier<String> defaultSupplier = () -> "default";
        Function<String, String> mapper = s -> null;

        // When
        String result = option.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertNull(result);
    }

    @Test
    void givenNoneOption_whenMapOrDefaultSupplierReturnsNull_thenReturnsNull() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> null;
        Function<String, String> mapper = String::toUpperCase;

        // When
        String result = option.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertNull(result);
    }

    @Test
    void givenSomeOption_whenMapOrDefaultWithTypeChange_thenReturnsCorrectType() {
        // Given
        Option<String> option = Option.Some("123");
        Supplier<Integer> defaultSupplier = () -> 0;
        Function<String, Integer> mapper = Integer::parseInt;

        // When
        Integer result = option.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals(123, result);
    }

    @Test
    void givenNoneOption_whenMapOrDefaultWithTypeChange_thenReturnsDefaultType() {
        // Given
        Option<String> option = Option.None();
        Supplier<Integer> defaultSupplier = () -> -1;
        Function<String, Integer> mapper = Integer::parseInt;

        // When
        Integer result = option.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals(-1, result);
    }

    @Test
    void givenSomeOption_whenMapOrDefaultAfterFilter_thenReturnsMappedValue() {
        // Given
        Option<Integer> option = Option.Some(10).filter(n -> n > 5);
        Supplier<String> defaultSupplier = () -> "too-small";
        Function<Integer, String> mapper = n -> "large: " + n;

        // When
        String result = option.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals("large: 10", result);
    }
}