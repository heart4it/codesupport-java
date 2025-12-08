package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.mapOrElse() method
 */
public class ResultMapOrElseTest {

    @Test
    void mapOrElse_OkWithMappingFunction_ShouldReturnMappedValue() {
        // Given
        Result<Integer, String> okResult = Result.ok(5);
        Supplier<String> defaultSupplier = () -> "default";
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        String result = okResult.mapOrElse(defaultSupplier, mapper);

        // Then
        assertEquals("Number: 5", result);
    }

    @Test
    void mapOrElse_Err_ShouldReturnDefaultFromSupplier() {
        // Given
        Result<Integer, String> errResult = Result.err("error");
        Supplier<String> defaultSupplier = () -> "default";
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        String result = errResult.mapOrElse(defaultSupplier, mapper);

        // Then
        assertEquals("default", result);
    }

    @Test
    void mapOrElse_ShouldBeEquivalentToMapOrDefault() {
        // Given
        Result<Integer, String> okResult = Result.ok(5);
        Supplier<String> defaultSupplier = () -> "default";
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        String viaMapOrElse = okResult.mapOrElse(defaultSupplier, mapper);
        String viaMapOrDefault = okResult.mapOrDefault(defaultSupplier, mapper);

        // Then
        assertEquals(viaMapOrElse, viaMapOrDefault);
    }

    @Test
    void mapOrElse_WithOkResult_ShouldNotCallSupplier() {
        // Given
        Result<Integer, String> okResult = Result.ok(5);
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        okResult.mapOrElse(defaultSupplier, mapper);

        // Then
        assertFalse(supplierCalled[0]);
    }

    @Test
    void mapOrElse_WithErrResult_ShouldCallSupplier() {
        // Given
        Result<Integer, String> errResult = Result.err("error");
        boolean[] supplierCalled = {false};
        Supplier<String> defaultSupplier = () -> {
            supplierCalled[0] = true;
            return "default";
        };
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        errResult.mapOrElse(defaultSupplier, mapper);

        // Then
        assertTrue(supplierCalled[0]);
    }

    @Test
    void mapOrElse_WithErrorSpecificDefault_ShouldWork() {
        // Given
        Result<Integer, String> errResult = Result.err("not found");
        Supplier<String> defaultSupplier = () -> "Error occurred";
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When
        String result = errResult.mapOrElse(defaultSupplier, mapper);

        // Then
        assertEquals("Error occurred", result);
    }

    @Test
    void mapOrElse_WithComplexTransformations_ShouldWork() {
        // Given
        Result<String, Integer> okResult = Result.ok("hello world");
        Supplier<Integer> defaultSupplier = () -> -1;
        Function<String, Integer> mapper = String::length;

        // When
        Integer result = okResult.mapOrElse(defaultSupplier, mapper);

        // Then
        assertEquals(11, result);
    }

    @Test
    void mapOrElse_WithFunctionThatThrowsException_ShouldPropagateException() {
        // Given
        Result<Integer, String> okResult = Result.ok(5);
        Supplier<String> defaultSupplier = () -> "default";
        Function<Integer, String> mapper = n -> {
            throw new RuntimeException("Mapping failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () ->
                okResult.mapOrElse(defaultSupplier, mapper));
    }

    @Test
    void mapOrElse_WithSupplierThatThrowsException_ShouldPropagateException() {
        // Given
        Result<Integer, String> errResult = Result.err("error");
        Supplier<String> defaultSupplier = () -> {
            throw new RuntimeException("Supplier failed");
        };
        Function<Integer, String> mapper = n -> "Number: " + n;

        // When & Then
        assertThrows(RuntimeException.class, () ->
                errResult.mapOrElse(defaultSupplier, mapper));
    }

}