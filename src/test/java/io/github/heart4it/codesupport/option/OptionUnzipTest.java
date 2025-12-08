package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionUnzipTest {

    @Test
    void givenSomePair_whenUnzip_thenReturnsPairOfSome() {
        // Given
        Option.Pair<String, Integer> pair = new Option.Pair<>("hello", 42);
        Option<Option.Pair<String, Integer>> option = Option.Some(pair);

        // When
        Option.Pair<Option<String>, Option<Integer>> result = Option.unzip(option);

        // Then
        assertTrue(result.first().isSome());
        assertTrue(result.second().isSome());
        assertEquals("hello", result.first().unwrap());
        assertEquals(42, result.second().unwrap());
    }

    @Test
    void givenNone_whenUnzip_thenReturnsPairOfNone() {
        // Given
        Option<Option.Pair<String, Integer>> option = Option.None();

        // When
        Option.Pair<Option<String>, Option<Integer>> result = Option.unzip(option);

        // Then
        assertTrue(result.first().isNone());
        assertTrue(result.second().isNone());
    }

    @Test
    void givenSomePairWithNullFirst_whenUnzipThrow() {
        // Given
        Option.Pair<String, Integer> pair = new Option.Pair<>(null, 42);
        Option<Option.Pair<String, Integer>> option = Option.Some(pair);

        // When
        assertThrows(NullPointerException.class,
                () -> {
                    Option.unzip(option);
                });
    }

    @Test
    void givenSomePairWithNullSecond_whenUnzipThrow() {
        // Given
        Option.Pair<String, Integer> pair = new Option.Pair<>("hello", null);
        Option<Option.Pair<String, Integer>> option = Option.Some(pair);

        assertThrows(NullPointerException.class,
                () -> {
                    Option.unzip(option);
                });
    }

    @Test
    void givenSomePairWithBothNull_whenUnzipThrow() {
        // Given
        Option.Pair<String, Integer> pair = new Option.Pair<>(null, null);
        Option<Option.Pair<String, Integer>> option = Option.Some(pair);

        // When
        assertThrows(NullPointerException.class,
                () -> {
                    Option.unzip(option);
                });
    }

    @Test
    void givenSomePairWithDifferentTypes_whenUnzip_thenReturnsPairOfSome() {
        // Given
        Option.Pair<String, Boolean> pair = new Option.Pair<>("test", true);
        Option<Option.Pair<String, Boolean>> option = Option.Some(pair);

        // When
        Option.Pair<Option<String>, Option<Boolean>> result = Option.unzip(option);

        // Then
        assertTrue(result.first().isSome());
        assertTrue(result.second().isSome());
        assertEquals("test", result.first().unwrap());
        assertTrue(result.second().unwrap());
    }

    @Test
    void givenSomePairAfterZip_whenUnzip_thenReturnsOriginalOptions() {
        // Given
        Option<String> first = Option.Some("hello");
        Option<Integer> second = Option.Some(42);
        Option<Option.Pair<String, Integer>> zipped = Option.zip(first, second);

        // When
        Option.Pair<Option<String>, Option<Integer>> unzipped = Option.unzip(zipped);

        // Then
        assertEquals(first.unwrap(), unzipped.first().unwrap());
        assertEquals(second.unwrap(), unzipped.second().unwrap());
    }

    @Test
    void givenNoneAfterZip_whenUnzip_thenReturnsPairOfNone() {
        // Given
        Option<String> first = Option.None();
        Option<Integer> second = Option.Some(42);
        Option<Option.Pair<String, Integer>> zipped = Option.zip(first, second);

        // When
        Option.Pair<Option<String>, Option<Integer>> unzipped = Option.unzip(zipped);

        // Then
        assertTrue(unzipped.first().isNone());
        assertTrue(unzipped.second().isNone());
    }

    @Test
    void givenSomePairWithComplexTypes_whenUnzip_thenReturnsPairOfSome() {
        // Given
        Option.Pair<java.util.List<String>, java.util.Set<Integer>> pair =
                new Option.Pair<>(java.util.List.of("a", "b"), java.util.Set.of(1, 2));
        Option<Option.Pair<java.util.List<String>, java.util.Set<Integer>>> option =
                Option.Some(pair);

        // When
        Option.Pair<Option<java.util.List<String>>, Option<java.util.Set<Integer>>> result =
                Option.unzip(option);

        // Then
        assertTrue(result.first().isSome());
        assertTrue(result.second().isSome());
        assertEquals(2, result.first().unwrap().size());
        assertEquals(2, result.second().unwrap().size());
    }

    @Test
    void givenUnzipThenZip_whenRoundTrip_thenReturnsOriginal() {
        // Given
        Option<String> originalFirst = Option.Some("hello");
        Option<Integer> originalSecond = Option.Some(42);
        Option<Option.Pair<String, Integer>> zipped = Option.zip(originalFirst, originalSecond);

        // When
        Option.Pair<Option<String>, Option<Integer>> unzipped = Option.unzip(zipped);
        Option<Option.Pair<String, Integer>> rezipped = Option.zip(unzipped.first(), unzipped.second());

        // Then
        assertTrue(rezipped.isSome());
        assertEquals("hello", rezipped.unwrap().first());
        assertEquals(42, rezipped.unwrap().second());
    }
}