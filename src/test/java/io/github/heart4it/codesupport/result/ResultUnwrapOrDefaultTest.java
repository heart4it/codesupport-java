package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.unwrapOrDefault() method
 */
public class ResultUnwrapOrDefaultTest {

    @Test
    void unwrapOrDefault_Ok_ShouldReturnValue() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");
        Supplier<String> defaultSupplier = () -> "default";

        // When
        String result = okResult.unwrapOrDefault(defaultSupplier);

        // Then
        assertEquals("success", result);
    }

    @Test
    void unwrapOrDefault_Err_ShouldReturnSupplierResult() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        Supplier<String> defaultSupplier = () -> "default";

        // When
        String result = errResult.unwrapOrDefault(defaultSupplier);

        // Then
        assertEquals("default", result);
    }

    @Test
    void unwrapOrDefault_ShouldBeEquivalentToUnwrapOrElse() {
        // Given
        Result<String, Integer> okResult = Result.ok("test");
        Supplier<String> defaultSupplier = () -> "default";

        // When
        String viaDefault = okResult.unwrapOrDefault(defaultSupplier);
        String viaOrElse = okResult.unwrapOrElse(defaultSupplier);

        // Then
        assertEquals(viaDefault, viaOrElse);
    }

    @Test
    void unwrapOrDefault_WithOkResult_ShouldNotCallSupplier() {
        // Given
        Result<String, Integer> okResult = Result.ok("success");
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };

        // When
        okResult.unwrapOrDefault(defaultSupplier);

        // Then
        assertFalse(supplierCalled[0]);
    }

    @Test
    void unwrapOrDefault_WithErrResult_ShouldCallSupplier() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };

        // When
        errResult.unwrapOrDefault(defaultSupplier);

        // Then
        assertTrue(supplierCalled[0]);
    }

    @Test
    void unwrapOrDefault_WithComplexDefaultCreation_ShouldWork() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        Supplier<String> defaultSupplier = () -> {
            long timestamp = System.currentTimeMillis();
            return "default-at-" + timestamp;
        };

        // When
        String result = errResult.unwrapOrDefault(defaultSupplier);

        // Then
        assertTrue(result.startsWith("default-at-"));
    }

    @Test
    void unwrapOrDefault_WithNullSupplierResult_ShouldWork() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        Supplier<String> defaultSupplier = () -> null;

        // When
        String result = errResult.unwrapOrDefault(defaultSupplier);

        // Then
        assertNull(result);
    }

    @Test
    void unwrapOrDefault_WithSupplierThatThrowsException_ShouldPropagateException() {
        // Given
        Result<String, Integer> errResult = Result.err(404);
        Supplier<String> defaultSupplier = () -> {
            throw new RuntimeException("Supplier failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> errResult.unwrapOrDefault(defaultSupplier));
    }

    @Test
    void unwrapOrDefault_WithIntegerTypes_ShouldWork() {
        // Given
        Result<Integer, String> errResult = Result.err("error");
        Supplier<Integer> defaultSupplier = () -> 42;

        // When
        Integer result = errResult.unwrapOrDefault(defaultSupplier);

        // Then
        assertEquals(42, result);
    }

}