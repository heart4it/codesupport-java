package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionOkOrTest {

    @Test
    void givenSomeOption_whenOkOr_thenReturnsOkWithValue() {
        // Given
        Option<String> option = Option.Some("success");
        String error = "error";

        // When
        Result<String, String> result = option.okOr(error);

        // Then
        assertTrue(result.isOk());
        assertEquals("success", result.unwrap());
    }

    @Test
    void givenNoneOption_whenOkOr_thenReturnsErrWithError() {
        // Given
        Option<String> option = Option.None();
        String error = "error";

        // When
        Result<String, String> result = option.okOr(error);

        // Then
        assertTrue(result.isErr());
        assertEquals("error", result.unwrapErr());
    }

    @Test
    void givenSomeOptionWithInteger_whenOkOr_thenReturnsOkWithInteger() {
        // Given
        Option<Integer> option = Option.Some(42);
        String error = "error";

        // When
        Result<Integer, String> result = option.okOr(error);

        // Then
        assertTrue(result.isOk());
        assertEquals(42, result.unwrap());
    }

    @Test
    void givenNoneOptionWithIntegerError_whenOkOr_thenReturnsErrWithInteger() {
        // Given
        Option<String> option = Option.None();
        Integer error = 404;

        // When
        Result<String, Integer> result = option.okOr(error);

        // Then
        assertTrue(result.isErr());
        assertEquals(404, result.unwrapErr());
    }

    @Test
    void givenSomeOptionAfterMap_whenOkOr_thenReturnsOkWithMappedValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);
        String error = "error";

        // When
        Result<String, String> result = option.okOr(error);

        // Then
        assertTrue(result.isOk());
        assertEquals("HELLO", result.unwrap());
    }

    @Test
    void givenNoneOptionAfterMap_whenOkOr_thenReturnsErrWithError() {
        // Given
        Option<String> option = Option.<String>None().map(String::toUpperCase);
        String error = "error";

        // When
        Result<String, String> result = option.okOr(error);

        // Then
        assertTrue(result.isErr());
        assertEquals("error", result.unwrapErr());
    }

    @Test
    void givenSomeOptionAfterFilter_whenOkOr_thenReturnsOkWithFilteredValue() {
        // Given
        Option<Integer> option = Option.Some(10).filter(n -> n > 5);
        String error = "error";

        // When
        Result<Integer, String> result = option.okOr(error);

        // Then
        assertTrue(result.isOk());
        assertEquals(10, result.unwrap());
    }

    @Test
    void givenNoneOptionAfterFilter_whenOkOr_thenReturnsErrWithError() {
        // Given
        Option<Integer> option = Option.Some(3).filter(n -> n > 5);
        String error = "value too small";

        // When
        Result<Integer, String> result = option.okOr(error);

        // Then
        assertTrue(result.isErr());
        assertEquals("value too small", result.unwrapErr());
    }

    @Test
    void givenOkOrWithNullError_whenNone_thenReturnsErrWithNull() {
        // Given
        Option<String> option = Option.None();
        String error = null;

        // When
        Result<String, String> result = option.okOr(error);

        // Then
        assertTrue(result.isErr());
        assertNull(result.unwrapErr());
    }
}