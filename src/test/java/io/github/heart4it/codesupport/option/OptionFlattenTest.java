package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionFlattenTest {

    @Test
    void givenSomeSome_whenFlatten_thenReturnsInnerSome() {
        // Given
        Option<Option<String>> nested = Option.Some(Option.Some("value"));

        // When
        Option<String> result = Option.flatten(nested);

        // Then
        assertTrue(result.isSome());
        assertEquals("value", result.unwrap());
    }

    @Test
    void givenSomeNone_whenFlatten_thenReturnsNone() {
        // Given
        Option<Option<String>> nested = Option.Some(Option.None());

        // When
        Option<String> result = Option.flatten(nested);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenNone_whenFlatten_thenReturnsNone() {
        // Given
        Option<Option<String>> nested = Option.None();

        // When
        Option<String> result = Option.flatten(nested);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenSomeSomeWithInteger_whenFlatten_thenReturnsInnerSome() {
        // Given
        Option<Option<Integer>> nested = Option.Some(Option.Some(42));

        // When
        Option<Integer> result = Option.flatten(nested);

        // Then
        assertTrue(result.isSome());
        assertEquals(42, result.unwrap());
    }


    @Test
    void givenTripleNestedSome_whenDoubleFlatten_thenReturnsInnermost() {
        // Given
        Option<Option<Option<String>>> tripleNested = Option.Some(Option.Some(Option.Some("deep")));

        // When
        Option<Option<String>> firstFlatten = Option.flatten(tripleNested);
        Option<String> secondFlatten = Option.flatten(firstFlatten);

        // Then
        assertTrue(secondFlatten.isSome());
        assertEquals("deep", secondFlatten.unwrap());
    }

    @Test
    void givenSomeSomeAfterMap_whenFlatten_thenReturnsMappedValue() {
        // Given
        Option<Option<String>> nested = Option.Some(Option.Some("hello"))
                .map(inner -> inner.map(String::toUpperCase));

        // When
        Option<String> result = Option.flatten(nested);

        // Then
        assertTrue(result.isSome());
        assertEquals("HELLO", result.unwrap());
    }

    @Test
    void givenSomeNoneAfterMap_whenFlatten_thenReturnsNone() {
        // Given
        Option<Option<String>> nested = Option.Some(Option.Some("hello"))
                .map(inner -> Option.None());

        // When
        Option<String> result = Option.flatten(nested);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenNoneAfterMap_whenFlatten_thenReturnsNone() {
        // Given
        Option<Option<String>> nested = Option.<Option<String>>None()
                .map(inner -> Option.Some("mapped"));

        // When
        Option<String> result = Option.flatten(nested);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void chainedFlattenOperations_thenReturnsCorrectLevel() {
        // Given
        Option<Option<Option<String>>> deeplyNested =
                Option.Some(Option.Some(Option.Some("deepest")));

        // When
        Option<String> result = Option.flatten(Option.flatten(deeplyNested));

        // Then
        assertTrue(result.isSome());
        assertEquals("deepest", result.unwrap());
    }
}