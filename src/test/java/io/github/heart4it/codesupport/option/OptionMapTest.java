package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class OptionMapTest {

    @Test
    void whenSomeMapFunction_thenReturnsMappedSome() {
        // When
        Option<String> original = Option.Some("hello");
        Option<Integer> result = original.map(String::length);

        // Then
        assertTrue(result.isSome());
        assertEquals(5, result.unwrap());
    }

    @Test
    void whenNoneMapFunction_thenReturnsNone() {
        // When
        Option<String> original = Option.None();
        Option<Integer> result = original.map(String::length);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenSomeMapWithIdentity_thenReturnsOriginal() {
        // When
        Option<String> original = Option.Some("hello");
        Option<String> result = original.map(Function.identity());

        // Then
        assertTrue(result.isSome());
        assertEquals("hello", result.unwrap());
    }

    @Test
    void whenSomeMapChangesType_thenReturnsNewType() {
        // When
        Option<Integer> original = Option.Some(42);
        Option<String> result = original.map(n -> "Number: " + n);

        // Then
        assertTrue(result.isSome());
        assertEquals("Number: 42", result.unwrap());
    }

    @Test
    void whenChainingMapOperations_thenReturnsFinalMappedValue() {
        // When
        Option<String> result = Option.Some(" hello ")
                .map(String::trim)
                .map(String::toUpperCase)
                .map(s -> s + "!");

        // Then
        assertTrue(result.isSome());
        assertEquals("HELLO!", result.unwrap());
    }

    @Test
    void whenMapOnNoneInChain_thenReturnsNone() {
        // When
        Option<String> result = Option.<String>None()
                .map(String::trim)
                .map(String::toUpperCase);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenSomeMapWithException_thenExceptionPropagates() {
        // When
        Option<String> original = Option.Some("hello");

        // Then
        assertThrows(RuntimeException.class, () ->
                original.map(s -> {
                    throw new RuntimeException("Mapping failed");
                })
        );
    }

    @Test
    void whenSomeMapWithComplexTransformation_thenReturnsTransformedValue() {
        // When
        Option<Integer> original = Option.Some(5);
        Option<String> result = original.map(n -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                sb.append("*");
            }
            return sb.toString();
        });

        // Then
        assertTrue(result.isSome());
        assertEquals("*****", result.unwrap());
    }

    @Test
    void whenMapAfterOtherOperations_thenReturnsMappedValue() {
        // When
        Option<String> result = Option.Some("hello")
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .or(Option.Some("fallback"))
                .map(s -> s + "!");

        // Then
        assertTrue(result.isSome());
        assertEquals("HELLO!", result.unwrap());
    }
}