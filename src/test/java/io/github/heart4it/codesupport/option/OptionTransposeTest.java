package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionTransposeTest {

    @Test
    void givenSomeOk_whenTranspose_thenReturnsOkSome() {
        // Given
        Option<Result<String, String>> option = Option.Some(Result.ok("success"));

        // When
        Result<Option<Result<String, String>>, Object> result = option.transpose();

        // Then
        assertTrue(result.isOk());
        assertTrue(result.unwrap().isSome());
        assertEquals("success", result.unwrap().unwrap());
    }

    @Test
    void givenSomeErr_whenTranspose_thenReturnsErr() {
        // Given
        Option<Result<String, String>> option = Option.Some(Result.err("error"));

        // When
        Result<Option<Result<String, String>>, Object> result = option.transpose();

        // Then
        assertTrue(result.isErr());
        assertEquals("error", result.unwrapErr());
    }

    @Test
    void givenNone_whenTranspose_thenReturnsOkNone() {
        // Given
        Option<Result<String, String>> option = Option.None();

        // When
        Result<Option<Result<String, String>>, Object> result = option.transpose();

        // Then
        assertTrue(result.isOk());
        assertTrue(result.unwrap().isNone());
    }

    @Test
    void givenSomeOkWithInteger_whenTranspose_thenReturnsOkSomeWithInteger() {
        // Given
        Option<Result<Integer, String>> option = Option.Some(Result.ok(42));

        // When
        Result<Option<Result<Integer, String>>, Object> result = option.transpose();

        // Then
        assertTrue(result.isOk());
        assertTrue(result.unwrap().isSome());
        assertEquals(42, result.unwrap().unwrap());
    }

    @Test
    void givenSomeErrWithInteger_whenTranspose_thenReturnsErrWithInteger() {
        // Given
        Option<Result<String, Integer>> option = Option.Some(Result.err(404));

        // When
        Result<Option<Result<String, Integer>>, Object> result = option.transpose();

        // Then
        assertTrue(result.isErr());
        assertEquals(404, result.unwrapErr());
    }

    @Test
    void givenSomeOkWithNull_whenTransposeWithNullValueThrow() {
        // Given
        Option<Result<String, String>> option = Option.Some(Result.ok(null));

        assertThrows(NullPointerException.class,
                () -> {
                    option.transpose();
                });
    }

    @Test
    void givenOptionWithNonResult_whenTranspose_thenThrowsException() {
        // Given
        Option<String> option = Option.Some("not-a-result");

        // When & Then
        assertThrows(IllegalStateException.class, option::transpose);
    }

    @Test
    void givenSomeOkAfterMap_whenTranspose_thenReturnsOkSomeWithMappedValue() {
        // Given
        Option<Object> option = Option.Some(Result.ok("hello"))
                .map(result -> result.map(String::toUpperCase));

        // When
        Result<Option<Object>, Object> result = option.transpose();

        // Then
        assertTrue(result.isOk());
        assertEquals("HELLO", result.unwrap().unwrap());
    }

    @Test
    void givenSomeErrAfterMap_whenTranspose_thenReturnsErrWithMappedError() {
        // Given
        Option<Object> option = Option.Some(Result.err("error"))
                .map(result -> result.mapErr(String::toUpperCase));

        // When
        Result<Option<Object>, Object> result = option.transpose();

        // Then
        assertTrue(result.isErr());
        assertEquals("ERROR", result.unwrapErr());
    }

    @Test
    void givenNoneAfterOperations_whenTranspose_thenReturnsOkNone() {
        // Given
        Option<Result<String, String>> option = Option.<Result<String, String>>None()
                .map(result -> result.map(String::toUpperCase));

        // When
        Result<Option<Result<String, String>>, Object> result = option.transpose();

        // Then
        assertTrue(result.isOk());
        assertTrue(result.unwrap().isNone());
    }
}