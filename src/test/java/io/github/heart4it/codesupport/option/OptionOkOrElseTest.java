package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class OptionOkOrElseTest {

    @Test
    void givenSomeOption_whenOkOrElse_thenReturnsOkWithValue() {
        // Given
        Option<String> option = Option.Some("success");
        Supplier<String> errorSupplier = () -> "error";

        // When
        Result<String, String> result = option.okOrElse(errorSupplier);

        // Then
        assertTrue(result.isOk());
        assertEquals("success", result.unwrap());
    }

    @Test
    void givenNoneOption_whenOkOrElse_thenReturnsErrWithSupplierValue() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> errorSupplier = () -> "computed-error";

        // When
        Result<String, String> result = option.okOrElse(errorSupplier);

        // Then
        assertTrue(result.isErr());
        assertEquals("computed-error", result.unwrapErr());
    }

    @Test
    void givenSomeOption_whenOkOrElseSupplierNotCalled_thenSupplierNotInvoked() {
        // Given
        Option<String> option = Option.Some("success");
        boolean[] supplierCalled = {false};
        Supplier<String> errorSupplier = () -> {
            supplierCalled[0] = true;
            return "error";
        };

        // When
        Result<String, String> result = option.okOrElse(errorSupplier);

        // Then
        assertTrue(result.isOk());
        assertEquals("success", result.unwrap());
        assertFalse(supplierCalled[0]);
    }

    @Test
    void givenNoneOption_whenOkOrElseSupplierCalled_thenSupplierInvoked() {
        // Given
        Option<String> option = Option.None();
        boolean[] supplierCalled = {false};
        Supplier<String> errorSupplier = () -> {
            supplierCalled[0] = true;
            return "computed-error";
        };

        // When
        Result<String, String> result = option.okOrElse(errorSupplier);

        // Then
        assertTrue(result.isErr());
        assertEquals("computed-error", result.unwrapErr());
        assertTrue(supplierCalled[0]);
    }

    @Test
    void givenNoneOption_whenOkOrElseSupplierReturnsNull_thenReturnsErrWithNull() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> errorSupplier = () -> null;

        // When
        Result<String, String> result = option.okOrElse(errorSupplier);

        // Then
        assertTrue(result.isErr());
        assertNull(result.unwrapErr());
    }

    @Test
    void givenSomeOptionWithInteger_whenOkOrElse_thenReturnsOkWithInteger() {
        // Given
        Option<Integer> option = Option.Some(42);
        Supplier<String> errorSupplier = () -> "error";

        // When
        Result<Integer, String> result = option.okOrElse(errorSupplier);

        // Then
        assertTrue(result.isOk());
        assertEquals(42, result.unwrap());
    }

    @Test
    void givenNoneOptionWithComplexErrorSupplier_whenOkOrElse_thenReturnsComputedError() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> errorSupplier = () -> "Error at " + System.currentTimeMillis();

        // When
        Result<String, String> result = option.okOrElse(errorSupplier);

        // Then
        assertTrue(result.isErr());
        assertTrue(result.unwrapErr().startsWith("Error at"));
    }

    @Test
    void givenNoneOption_whenOkOrElseSupplierThrowsException_thenExceptionPropagates() {
        // Given
        Option<String> option = Option.None();
        Supplier<String> errorSupplier = () -> {
            throw new RuntimeException("Supplier failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> option.okOrElse(errorSupplier));
    }

    @Test
    void givenOptionAfterOperations_whenOkOrElse_thenReturnsCorrectResult() {
        // Given
        Option<String> option = Option.Some("hello")
                .filter(s -> s.length() > 10); // becomes None
        Supplier<String> errorSupplier = () -> "filtered-out";

        // When
        Result<String, String> result = option.okOrElse(errorSupplier);

        // Then
        assertTrue(result.isErr());
        assertEquals("filtered-out", result.unwrapErr());
    }
}