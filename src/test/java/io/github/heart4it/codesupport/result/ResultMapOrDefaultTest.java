package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.mapOrDefault() method
 */
public class ResultMapOrDefaultTest {

    @Test
    void mapOrDefault_OkWithMappingFunction_ShouldReturnMappedValue() {
        // Given
        Result<Integer, String> okResult = Result.ok(5);
        Supplier<String> defaultSupplier = () -> "default";
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        String result = okResult.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals("Number: 5", result);
    }

    @Test
    void mapOrDefault_Err_ShouldReturnDefaultFromSupplier() {
        // Given
        Result<Integer, String> errResult = Result.err("error");
        Supplier<String> defaultSupplier = () -> "default";
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        String result = errResult.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals("default", result);
    }

    @Test
    void mapOrDefault_WithOkResult_ShouldNotCallSupplier() {
        // Given
        Result<Integer, String> okResult = Result.ok(5);
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        okResult.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertFalse(supplierCalled[0]);
    }

    @Test
    void mapOrDefault_WithErrResult_ShouldCallSupplier() {
        // Given
        Result<Integer, String> errResult = Result.err("error");
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        errResult.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertTrue(supplierCalled[0]);
    }

    @Test
    void mapOrDefault_WithComplexDefaultLogic_ShouldWork() {
        // Given
        Result<Integer, String> errResult = Result.err("error");
        Supplier<String> defaultSupplier = () -> {
            if (System.currentTimeMillis() > 0) {
                return "time-based-default";
            }
            return "other-default";
        };
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        String result = errResult.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals("time-based-default", result);
    }

    @Test
    void mapOrDefault_WithNullMappingResult_ShouldWork() {
        // Given
        Result<String, Integer> okResult = Result.ok("value");
        Supplier<String> defaultSupplier = () -> "default";
        Function<String, String> mapper = s -> null;

        // When
        String result = okResult.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertNull(result);
    }

    @Test
    void mapOrDefault_WithNullDefaultFromSupplier_ShouldWork() {
        // Given
        Result<Integer, String> errResult = Result.err("error");
        Supplier<String> defaultSupplier = () -> null;
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        String result = errResult.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertNull(result);
    }

    @Test
    void mapOrDefault_WithSupplierThatThrowsException_ShouldPropagateException() {
        // Given
        Result<Integer, String> errResult = Result.err("error");
        Supplier<String> defaultSupplier = () -> {
            throw new RuntimeException("Supplier failed");
        };
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When & Then
        assertThrows(RuntimeException.class, () ->
                errResult.mapOrDefault(defaultSupplier, mapper));
    }

    @Test
    void mapOrDefault_WithDifferentTypes_ShouldWork() {
        // Given
        Result<String, Integer> okResult = Result.ok("123");
        Supplier<Integer> defaultSupplier = () -> 0;
        Function<String, Integer> mapper = Integer::parseInt;

        // When
        Integer result = okResult.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals(123, result);
    }

}