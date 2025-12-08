package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class OptionMapOrElseTest {

    @Test
    void givenSomeOption_whenMapOrElse_thenReturnsMappedValue() {
        // Given
        Option<String> option = Option.Some("hello");
        Supplier<String> defaultSupplier = () -> "default";
        Function<String, String> mapper = String::toUpperCase;

        // When
        String result = option.mapOrElse(defaultSupplier, mapper);

        // Then
        assertEquals("HELLO", result);
    }

    @Test
    void givenNoneOption_whenMapOrElse_thenReturnsSupplierValue() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> "computed-default";
        Function<String, String> mapper = String::toUpperCase;

        // When
        String result = option.mapOrElse(defaultSupplier, mapper);

        // Then
        assertEquals("computed-default", result);
    }

    @Test
    void givenSomeOption_whenMapOrElseWithComplexComputation_thenReturnsMappedValue() {
        // Given
        Option<Integer> option = Option.Some(3);
        Supplier<String> defaultSupplier = () -> "no-value";
        Function<Integer, String> mapper = n -> "x".repeat(n);

        // When
        String result = option.mapOrElse(defaultSupplier, mapper);

        // Then
        assertEquals("xxx", result);
    }

    @Test
    void givenNoneOption_whenMapOrElseWithExpensiveSupplier_thenSupplierExecuted() {
        // Given
        Option<String> option = Option.None();
        int[] computationCount = {0};
        Supplier<String> defaultSupplier = () -> {
            computationCount[0]++;
            return "expensive-result";
        };
        Function<String, String> mapper = String::toUpperCase;

        // When
        String result = option.mapOrElse(defaultSupplier, mapper);

        // Then
        assertEquals("expensive-result", result);
        assertEquals(1, computationCount[0]);
    }

    @Test
    void givenSomeOption_whenMapOrElseWithExpensiveSupplier_thenSupplierNotExecuted() {
        // Given
        Option<String> option = Option.Some("hello");
        int[] computationCount = {0};
        Supplier<String> defaultSupplier = () -> {
            computationCount[0]++;
            return "expensive-result";
        };
        Function<String, String> mapper = String::toUpperCase;

        // When
        String result = option.mapOrElse(defaultSupplier, mapper);

        // Then
        assertEquals("HELLO", result);
        assertEquals(0, computationCount[0]);
    }

    @Test
    void givenSomeOption_whenMapOrElseMapperThrowsException_thenExceptionPropagates() {
        // Given
        Option<String> option = Option.Some("hello");
        Supplier<String> defaultSupplier = () -> "default";
        Function<String, String> mapper = s -> {
            throw new RuntimeException("Mapper failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () ->
                option.mapOrElse(defaultSupplier, mapper)
        );
    }

    @Test
    void givenNoneOption_whenMapOrElseSupplierThrowsException_thenExceptionPropagates() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> {
            throw new RuntimeException("Supplier failed");
        };
        Function<String, String> mapper = String::toUpperCase;

        // When & Then
        assertThrows(RuntimeException.class, () ->
                option.mapOrElse(defaultSupplier, mapper)
        );
    }

    @Test
    void givenSomeOption_whenMapOrElseWithNullMapper_thenReturnsNull() {
        // Given
        Option<String> option = Option.Some("hello");
        Supplier<String> defaultSupplier = () -> "default";
        Function<String, String> mapper = s -> null;

        // When
        String result = option.mapOrElse(defaultSupplier, mapper);

        // Then
        assertNull(result);
    }

    @Test
    void givenNoneOption_whenMapOrElseWithNullSupplier_thenReturnsNull() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> null;
        Function<String, String> mapper = String::toUpperCase;

        // When
        String result = option.mapOrElse(defaultSupplier, mapper);

        // Then
        assertNull(result);
    }

    @Test
    void givenOptionChain_whenMapOrElse_thenReturnsCorrectValue() {
        // Given
        Option<String> option = Option.Some(" hello ")
                .map(String::trim)
                .filter(s -> !s.isEmpty());
        Supplier<String> defaultSupplier = () -> "empty";
        Function<String, String> mapper = s -> "Value: " + s;

        // When
        String result = option.mapOrElse(defaultSupplier, mapper);

        // Then
        assertEquals("Value: hello", result);
    }
}