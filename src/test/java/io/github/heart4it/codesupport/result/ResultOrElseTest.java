package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.orElse() method
 */
public class ResultOrElseTest {

    @Test
    void orElse_WithOkResult_ShouldReturnOriginal() {
        // Given
        Result<String, Integer> original = Result.ok("success");
        Supplier<Result<String, Integer>> supplier = () -> Result.ok("fallback");

        // When
        Result<String, Integer> result = original.orElse(supplier);

        // Then
        assertTrue(result.isOk());
        assertEquals("success", result.unwrap());
    }

    @Test
    void orElse_WithErrResult_ShouldReturnSupplierResult() {
        // Given
        Result<String, Integer> original = Result.err(404);
        Supplier<Result<String, Integer>> supplier = () -> Result.ok("recovered");

        // When
        Result<String, Integer> result = original.orElse(supplier);

        // Then
        assertTrue(result.isOk());
        assertEquals("recovered", result.unwrap());
    }

    @Test
    void orElse_WithErrAndSupplierReturningErr_ShouldReturnSupplierErr() {
        // Given
        Result<String, Integer> original = Result.err(404);
        Supplier<Result<String, Integer>> supplier = () -> Result.err(500);

        // When
        Result<String, Integer> result = original.orElse(supplier);

        // Then
        assertTrue(result.isErr());
        assertEquals(500, result.unwrapErr());
    }

    @Test
    void orElse_WithOkResult_ShouldNotCallSupplier() {
        // Given
        Result<String, Integer> original = Result.ok("success");
        boolean[] supplierCalled = {false};
        Supplier<Result<String, Integer>> supplier = () -> {
            supplierCalled[0] = true;
            return Result.ok("fallback");
        };

        // When
        original.orElse(supplier);

        // Then
        assertFalse(supplierCalled[0]);
    }

    @Test
    void orElse_WithErrResult_ShouldCallSupplier() {
        // Given
        Result<String, Integer> original = Result.err(404);
        boolean[] supplierCalled = {false};
        Supplier<Result<String, Integer>> supplier = () -> {
            supplierCalled[0] = true;
            return Result.ok("fallback");
        };

        // When
        original.orElse(supplier);

        // Then
        assertTrue(supplierCalled[0]);
    }

    @Test
    void orElse_WithComplexRecoveryLogic_ShouldWork() {
        // Given
        Result<String, Integer> original = Result.err(404);
        Supplier<Result<String, Integer>> supplier = () -> {
            if (System.currentTimeMillis() > 0) {
                return Result.ok("time-based recovery");
            }
            return Result.err(999);
        };

        // When
        Result<String, Integer> result = original.orElse(supplier);

        // Then
        assertTrue(result.isOk());
        assertEquals("time-based recovery", result.unwrap());
    }

    @Test
    void orElse_WithMultipleChainedOperations_ShouldWork() {
        // Given
        Result<String, Integer> original = Result.err(100);
        Supplier<Result<String, Integer>> firstSupplier = () -> Result.err(200);
        Supplier<Result<String, Integer>> secondSupplier = () -> Result.ok("finally recovered");

        // When
        Result<String, Integer> result = original
                .orElse(firstSupplier)
                .orElse(secondSupplier);

        // Then
        assertTrue(result.isOk());
        assertEquals("finally recovered", result.unwrap());
    }

    @Test
    void orElse_WithSupplierThatThrowsException_ShouldPropagateException() {
        // Given
        Result<String, Integer> original = Result.err(404);
        Supplier<Result<String, Integer>> supplier = () -> {
            throw new RuntimeException("Supplier failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> original.orElse(supplier));
    }

    @Test
    void orElse_WithNullValueFromSupplier_ShouldWork() {
        // Given
        Result<String, Integer> original = Result.err(404);
        Supplier<Result<String, Integer>> supplier = () -> Result.ok(null);

        // When
        Result<String, Integer> result = original.orElse(supplier);

        // Then
        assertTrue(result.isOk());
        assertNull(result.unwrap());
    }
}