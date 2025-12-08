package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class OptionGetOrInsertWithTest {

    @Test
    void givenSomeOption_whenGetOrInsertWith_thenReturnsExistingValue() {
        // Given
        Option<String> option = Option.Some("existing");
        Supplier<String> defaultSupplier = () -> "default";

        // When
        String result = option.getOrInsertWith(defaultSupplier);

        // Then
        assertEquals("existing", result);
    }

    @Test
    void givenNoneOption_whenGetOrInsertWith_thenReturnsSupplierValue() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> "computed-default";

        // When
        String result = option.getOrInsertWith(defaultSupplier);

        // Then
        assertEquals("computed-default", result);
    }

    @Test
    void givenSomeOption_whenGetOrInsertWithSameAsGetOrInsertDefault_thenSameBehavior() {
        // Given
        Option<String> someOption = Option.Some("value");
        Option<String> noneOption = Option.None();
        Supplier<String> supplier = () -> "default";

        // When
        String result1 = someOption.getOrInsertWith(supplier);
        String result2 = someOption.getOrInsertDefault(supplier);
        String result3 = noneOption.getOrInsertWith(supplier);
        String result4 = noneOption.getOrInsertDefault(supplier);

        // Then
        assertEquals(result1, result2);
        assertEquals(result3, result4);
    }

    @Test
    void givenSomeOption_whenGetOrInsertWithSupplierNotCalled_thenSupplierNotInvoked() {
        // Given
        Option<String> option = Option.Some("existing");
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };

        // When
        String result = option.getOrInsertWith(defaultSupplier);

        // Then
        assertEquals("existing", result);
        assertFalse(supplierCalled[0]);
    }

    @Test
    void givenNoneOption_whenGetOrInsertWithSupplierCalled_thenSupplierInvoked() {
        // Given
        Option<String> option = Option.None();
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "computed-default";
        };

        // When
        String result = option.getOrInsertWith(defaultSupplier);

        // Then
        assertEquals("computed-default", result);
        assertTrue(supplierCalled[0]);
    }

    @Test
    void givenSomeOptionWithInteger_whenGetOrInsertWith_thenReturnsExistingInteger() {
        // Given
        Option<Integer> option = Option.Some(42);
        Supplier<Integer> defaultSupplier = () -> 100;

        // When
        Integer result = option.getOrInsertWith(defaultSupplier);

        // Then
        assertEquals(42, result);
    }

    @Test
    void givenNoneOptionWithInteger_whenGetOrInsertWith_thenReturnsSupplierInteger() {
        // Given
        Option<Integer> option = Option.None();
        Supplier<Integer> defaultSupplier = () -> 100;

        // When
        Integer result = option.getOrInsertWith(defaultSupplier);

        // Then
        assertEquals(100, result);
    }

    @Test
    void givenNoneOption_whenGetOrInsertWithComplexSupplier_thenReturnsComputedValue() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                sb.append("x");
            }
            return sb.toString();
        };

        // When
        String result = option.getOrInsertWith(defaultSupplier);

        // Then
        assertEquals("xxx", result);
    }

    @Test
    void givenNoneOption_whenGetOrInsertWithSupplierThrowsException_thenExceptionPropagates() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> defaultSupplier = () -> {
            throw new RuntimeException("Supplier failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> option.getOrInsertWith(defaultSupplier));
    }

    @Test
    void givenOptionAfterOperations_whenGetOrInsertWith_thenReturnsCorrectValue() {
        // Given
        Option<String> option = Option.Some("hello")
                .filter(s -> s.length() > 10); // becomes None
        Supplier<String> defaultSupplier = () -> "fallback";

        // When
        String result = option.getOrInsertWith(defaultSupplier);

        // Then
        assertEquals("fallback", result);
    }
}