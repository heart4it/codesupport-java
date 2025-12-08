package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.unwrapOrElse() method
 */
public class ResultUnwrapOrElseTest {

    @Test
    void unwrapOrElse_Ok_ShouldReturnValue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");
        Supplier<String> defaultSupplier = () -> "default";

        // When
        String result = okResult.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals("success", result);
    }

    @Test
    void unwrapOrElse_Err_ShouldReturnSupplierResult() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        Supplier<String> defaultSupplier = () -> "default";

        // When
        String result = errResult.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals("default", result);
    }

    @Test
    void unwrapOrElse_WithOkResult_ShouldNotCallSupplier() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };

        // When
        okResult.unwrapOrElse(defaultSupplier);

        // Then
        assertFalse(supplierCalled[0]);
    }

    @Test
    void unwrapOrElse_WithErrResult_ShouldCallSupplier() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };

        // When
        errResult.unwrapOrElse(defaultSupplier);

        // Then
        assertTrue(supplierCalled[0]);
    }

    @Test
    void unwrapOrElse_WithComplexRecoveryLogic_ShouldWork() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        Supplier<String> defaultSupplier = () -> {
            if (System.currentTimeMillis() > 0) {
                return "time-based-recovery";
            }
            return "other";
        };

        // When
        String result = errResult.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals("time-based-recovery", result);
    }

    @Test
    void unwrapOrElse_WithNullSupplierResult_ShouldWork() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        Supplier<String> defaultSupplier = () -> null;

        // When
        String result = errResult.unwrapOrElse(defaultSupplier);

        // Then
        assertNull(result);
    }

    @Test
    void unwrapOrElse_WithDifferentTypes_ShouldWork() {
        // Given
        Result<Integer, String> okResult = Result.ok(42);
        Supplier<Integer> defaultSupplier = () -> 0;

        // When
        Integer result = okResult.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals(42, result);
    }

    @Test
    void unwrapOrElse_WithSupplierThatThrowsException_ShouldPropagateException() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        Supplier<String> defaultSupplier = () -> {
            throw new RuntimeException("Supplier failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> errResult.unwrapOrElse(defaultSupplier));
    }

    @Test
    void unwrapOrElse_WithListCreation_ShouldWork() {
        // Given
        Result<java.util.List<String>, Integer> errResult = Result.err(404);
        Supplier<java.util.List<String>> defaultSupplier = () -> java.util.List.of("default1", "default2");

        // When
        java.util.List<String> result = errResult.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals(2, result.size());
        assertEquals("default1", result.get(0));
        assertEquals("default2", result.get(1));
    }
}