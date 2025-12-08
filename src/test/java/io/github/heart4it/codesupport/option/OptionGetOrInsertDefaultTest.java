package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class OptionGetOrInsertDefaultTest {

    @Test
    void givenSomeOption_whenGetOrInsertDefault_thenReturnsExistingValue() {
        // Given
        Option<String> option = Option.Some("existing");
        Supplier<String> defaultSupplier = () -> "default";

        // When
        String result = option.getOrInsertDefault(defaultSupplier);

        // Then
        assertEquals("existing", result);
    }

    @Test
    void givenNoneOption_whenGetOrInsertDefault_thenReturnsSupplierValue() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> "computed-default";

        // When
        String result = option.getOrInsertDefault(defaultSupplier);

        // Then
        assertEquals("computed-default", result);
    }

    @Test
    void givenSomeOption_whenGetOrInsertDefaultSupplierNotCalled_thenSupplierNotInvoked() {
        // Given
        Option<String> option = Option.Some("existing");
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };

        // When
        String result = option.getOrInsertDefault(defaultSupplier);

        // Then
        assertEquals("existing", result);
        assertFalse(supplierCalled[0]);
    }

    @Test
    void givenNoneOption_whenGetOrInsertDefaultSupplierCalled_thenSupplierInvoked() {
        // Given
        Option<String> option = Option.None();
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "computed-default";
        };

        // When
        String result = option.getOrInsertDefault(defaultSupplier);

        // Then
        assertEquals("computed-default", result);
        assertTrue(supplierCalled[0]);
    }


    @Test
    void givenNoneOption_whenGetOrInsertDefaultSupplierReturnsNull_thenReturnsNull() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> null;

        // When
        String result = option.getOrInsertDefault(defaultSupplier);

        // Then
        assertNull(result);
    }

    @Test
    void givenSomeOptionWithInteger_whenGetOrInsertDefault_thenReturnsExistingInteger() {
        // Given
        Option<Integer> option = Option.Some(42);
        Supplier<Integer> defaultSupplier = () -> 100;

        // When
        Integer result = option.getOrInsertDefault(defaultSupplier);

        // Then
        assertEquals(42, result);
    }

    @Test
    void givenNoneOptionWithInteger_whenGetOrInsertDefault_thenReturnsSupplierInteger() {
        // Given
        Option<Integer> option = Option.None();
        Supplier<Integer> defaultSupplier = () -> 100;

        // When
        Integer result = option.getOrInsertDefault(defaultSupplier);

        // Then
        assertEquals(100, result);
    }

    @Test
    void givenNoneOption_whenGetOrInsertDefaultWithComplexSupplier_thenReturnsComputedValue() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> "value-" + System.currentTimeMillis();

        // When
        String result = option.getOrInsertDefault(defaultSupplier);

        // Then
        assertTrue(result.startsWith("value-"));
    }

    @Test
    void givenNoneOption_whenGetOrInsertDefaultSupplierThrowsException_thenExceptionPropagates() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> {
            throw new RuntimeException("Supplier failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> option.getOrInsertDefault(defaultSupplier));
    }
}