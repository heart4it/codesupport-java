package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class OptionUnwrapOrDefaultTest {

    @Test
    void givenSomeOption_whenUnwrapOrDefault_thenReturnsValue() {
        // Given
        Option<String> option = Option.Some("hello");
        Supplier<String> defaultSupplier = () -> "default";

        // When
        String result = option.unwrapOrDefault(defaultSupplier);

        // Then
        assertEquals("hello", result);
    }

    @Test
    void givenNoneOption_whenUnwrapOrDefault_thenReturnsSupplierValue() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> "computed-default";

        // When
        String result = option.unwrapOrDefault(defaultSupplier);

        // Then
        assertEquals("computed-default", result);
    }

    @Test
    void givenSomeOption_whenUnwrapOrDefaultSupplierNotCalled_thenSupplierNotInvoked() {
        // Given
        Option<String> option = Option.Some("hello");
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };

        // When
        String result = option.unwrapOrDefault(defaultSupplier);

        // Then
        assertEquals("hello", result);
        assertFalse(supplierCalled[0]);
    }

    @Test
    void givenNoneOption_whenUnwrapOrDefaultSupplierCalled_thenSupplierInvoked() {
        // Given
        Option<String> option = Option.None();
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "computed-default";
        };

        // When
        String result = option.unwrapOrDefault(defaultSupplier);

        // Then
        assertEquals("computed-default", result);
        assertTrue(supplierCalled[0]);
    }


    @Test
    void givenNoneOption_whenUnwrapOrDefaultSupplierReturnsNull_thenReturnsNull() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> null;

        // When
        String result = option.unwrapOrDefault(defaultSupplier);

        // Then
        assertNull(result);
    }

    @Test
    void givenSomeOption_whenUnwrapOrDefaultWithTypeChange_thenReturnsValue() {
        // Given
        Option<Integer> option = Option.Some(42);
        Supplier<String> defaultSupplier = () -> "default";

        // When
        Integer result = option.unwrapOrDefault(() -> Integer.parseInt(defaultSupplier.get()));

        // Then
        assertEquals(42, result);
    }

    @Test
    void givenNoneOption_whenUnwrapOrDefaultWithTypeChange_thenReturnsComputedValue() {
        // Given
        Option<Integer> option = Option.None();
        Supplier<Integer> defaultSupplier = () -> 999;

        // When
        Integer result = option.unwrapOrDefault(defaultSupplier);

        // Then
        assertEquals(999, result);
    }

    @Test
    void givenNoneOption_whenUnwrapOrDefaultSupplierThrowsException_thenExceptionPropagates() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> {
            throw new RuntimeException("Supplier failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> option.unwrapOrDefault(defaultSupplier));
    }

    @Test
    void givenUnwrapOrDefaultSameAsUnwrapOrElse_thenSameBehavior() {
        // Given
        Option<String> someOption = Option.Some("value");
        Option<String> noneOption = Option.None();
        Supplier<String> supplier = () -> "default";

        // When
        String result1 = someOption.unwrapOrDefault(supplier);
        String result2 = someOption.unwrapOrElse(supplier);
        String result3 = noneOption.unwrapOrDefault(supplier);
        String result4 = noneOption.unwrapOrElse(supplier);

        // Then
        assertEquals(result1, result2);
        assertEquals(result3, result4);
    }
}