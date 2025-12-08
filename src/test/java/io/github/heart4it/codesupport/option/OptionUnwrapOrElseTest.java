package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class OptionUnwrapOrElseTest {

    @Test
    void givenSomeOption_whenUnwrapOrElse_thenReturnsValue() {
        // Given
        Option<String> option = Option.Some("hello");
        Supplier<String> defaultSupplier = () -> "default";

        // When
        String result = option.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals("hello", result);
    }

    @Test
    void givenNoneOption_whenUnwrapOrElse_thenReturnsSupplierValue() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> "computed-default";

        // When
        String result = option.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals("computed-default", result);
    }

    @Test
    void givenSomeOption_whenUnwrapOrElseSupplierNotCalled_thenSupplierNotInvoked() {
        // Given
        Option<String> option = Option.Some("hello");
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };

        // When
        String result = option.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals("hello", result);
        assertFalse(supplierCalled[0]);
    }

    @Test
    void givenNoneOption_whenUnwrapOrElseSupplierCalled_thenSupplierInvoked() {
        // Given
        Option<String> option = Option.None();
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "computed-default";
        };

        // When
        String result = option.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals("computed-default", result);
        assertTrue(supplierCalled[0]);
    }


    @Test
    void givenNoneOption_whenUnwrapOrElseSupplierReturnsNull_thenReturnsNull() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> null;

        // When
        String result = option.unwrapOrElse(defaultSupplier);

        // Then
        assertNull(result);
    }

    @Test
    void givenSomeOption_whenUnwrapOrElseWithComplexSupplier_thenReturnsValue() {
        // Given
        Option<Integer> option = Option.Some(42);
        Supplier<String> defaultSupplier = () -> "expensive-computation";

        // When
        Integer result = option.unwrapOrElse(() -> Integer.parseInt(defaultSupplier.get()));

        // Then
        assertEquals(42, result);
    }

    @Test
    void givenNoneOption_whenUnwrapOrElseWithComplexSupplier_thenReturnsComputedValue() {
        // Given
        Option<Integer> option = Option.None();
        Supplier<Integer> defaultSupplier = () -> {
            // Simulate expensive computation
            return 100 * 2;
        };

        // When
        Integer result = option.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals(200, result);
    }

    @Test
    void givenNoneOption_whenUnwrapOrElseSupplierThrowsException_thenExceptionPropagates() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> {
            throw new RuntimeException("Supplier failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> option.unwrapOrElse(defaultSupplier));
    }

    @Test
    void givenOptionAfterOperations_whenUnwrapOrElse_thenReturnsCorrectValue() {
        // Given
        Option<String> option = Option.Some("hello")
                .map(String::toUpperCase)
                .filter(s -> s.length() > 10); // becomes None
        Supplier<String> defaultSupplier = () -> "fallback";

        // When
        String result = option.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals("fallback", result);
    }
}