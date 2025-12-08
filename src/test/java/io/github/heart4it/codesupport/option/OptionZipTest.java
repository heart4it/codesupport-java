package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionZipTest {

    @Test
    void givenTwoSomeOptions_whenZip_thenReturnsSomePair() {
        // Given
        Option<String> first = Option.Some("hello");
        Option<Integer> second = Option.Some(42);

        // When
        Option<Option.Pair<String, Integer>> result = Option.zip(first, second);

        // Then
        assertTrue(result.isSome());
        Option.Pair<String, Integer> pair = result.unwrap();
        assertEquals("hello", pair.first());
        assertEquals(42, pair.second());
    }

    @Test
    void givenFirstNone_whenZip_thenReturnsNone() {
        // Given
        Option<String> first = Option.None();
        Option<Integer> second = Option.Some(42);

        // When
        Option<Option.Pair<String, Integer>> result = Option.zip(first, second);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenSecondNone_whenZip_thenReturnsNone() {
        // Given
        Option<String> first = Option.Some("hello");
        Option<Integer> second = Option.None();

        // When
        Option<Option.Pair<String, Integer>> result = Option.zip(first, second);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenBothNone_whenZip_thenReturnsNone() {
        // Given
        Option<String> first = Option.None();
        Option<Integer> second = Option.None();

        // When
        Option<Option.Pair<String, Integer>> result = Option.zip(first, second);

        // Then
        assertTrue(result.isNone());
    }


    @Test
    void givenTwoSomeWithSameType_whenZip_thenReturnsSomePair() {
        // Given
        Option<String> first = Option.Some("first");
        Option<String> second = Option.Some("second");

        // When
        Option<Option.Pair<String, String>> result = Option.zip(first, second);

        // Then
        assertTrue(result.isSome());
        Option.Pair<String, String> pair = result.unwrap();
        assertEquals("first", pair.first());
        assertEquals("second", pair.second());
    }

    @Test
    void givenSomeAfterMap_whenZip_thenReturnsSomePairWithMappedValues() {
        // Given
        Option<String> first = Option.Some("hello").map(String::toUpperCase);
        Option<Integer> second = Option.Some(5).map(n -> n * 2);

        // When
        Option<Option.Pair<String, Integer>> result = Option.zip(first, second);

        // Then
        assertTrue(result.isSome());
        Option.Pair<String, Integer> pair = result.unwrap();
        assertEquals("HELLO", pair.first());
        assertEquals(10, pair.second());
    }

    @Test
    void givenNoneAfterMap_whenZip_thenReturnsNone() {
        // Given
        Option<String> first = Option.Some("hello").filter(s -> s.length() > 10);
        Option<Integer> second = Option.Some(42);

        // When
        Option<Option.Pair<String, Integer>> result = Option.zip(first, second);

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void givenChainedZipOperations_whenAllSome_thenReturnsNestedPairs() {
        // Given
        Option<String> first = Option.Some("a");
        Option<Integer> second = Option.Some(1);
        Option<Boolean> third = Option.Some(true);

        // When
        Option<Option.Pair<String, Integer>> firstZip = Option.zip(first, second);
        Option<Option.Pair<Option.Pair<String, Integer>, Boolean>> result =
                Option.zip(firstZip, third);

        // Then
        assertTrue(result.isSome());
        Option.Pair<Option.Pair<String, Integer>, Boolean> pair = result.unwrap();
        assertEquals("a", pair.first().first());
        assertEquals(1, pair.first().second());
        assertTrue(pair.second());
    }

    @Test
    void givenComplexTypes_whenZip_thenReturnsSomePair() {
        // Given
        Option<java.util.List<String>> first = Option.Some(java.util.List.of("a", "b"));
        Option<java.util.Set<Integer>> second = Option.Some(java.util.Set.of(1, 2));

        // When
        Option<Option.Pair<java.util.List<String>, java.util.Set<Integer>>> result =
                Option.zip(first, second);

        // Then
        assertTrue(result.isSome());
        Option.Pair<java.util.List<String>, java.util.Set<Integer>> pair = result.unwrap();
        assertEquals(2, pair.first().size());
        assertEquals(2, pair.second().size());
    }
}