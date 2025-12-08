package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OptionMapOrTest {

    @Test
    void whenSomeMapOr_thenReturnsMappedValue() {
        // When
        Option<String> option = Option.Some("hello");
        Integer result = option.mapOr(0, String::length);

        // Then
        assertEquals(5, result);
    }

    @Test
    void whenNoneMapOr_thenReturnsDefaultValue() {
        // When
        Option<String> option = Option.None();
        Integer result = option.mapOr(0, String::length);

        // Then
        assertEquals(0, result);
    }

    @Test
    void whenSomeMapOrWithNullDefault_thenReturnsMappedValue() {
        // When
        Option<String> option = Option.Some("hello");
        String result = option.mapOr(null, String::toUpperCase);

        // Then
        assertEquals("HELLO", result);
    }

    @Test
    void whenNoneMapOrWithNullDefault_thenReturnsNull() {
        // When
        Option<String> option = Option.None();
        String result = option.mapOr(null, String::toUpperCase);

        // Then
        assertNull(result);
    }

    @Test
    void whenSomeMapOrChangesType_thenReturnsMappedType() {
        // When
        Option<Integer> option = Option.Some(42);
        String result = option.mapOr("default", n -> "Number: " + n);

        // Then
        assertEquals("Number: 42", result);
    }

    @Test
    void whenNoneMapOrChangesType_thenReturnsDefaultType() {
        // When
        Option<Integer> option = Option.None();
        String result = option.mapOr("default", n -> "Number: " + n);

        // Then
        assertEquals("default", result);
    }

    @Test
    void whenSomeMapOrWithZeroDefault_thenReturnsMappedValue() {
        // When
        Option<String> option = Option.Some("test");
        Integer result = option.mapOr(0, String::length);

        // Then
        assertEquals(4, result);
    }

    @Test
    void whenNoneMapOrWithComplexDefault_thenReturnsDefault() {
        // When
        Option<String> option = Option.None();
        java.util.List<String> result = option.mapOr(
                java.util.List.of("default1", "default2"),
                s -> java.util.List.of(s)
        );

        // Then
        assertEquals(2, result.size());
        assertEquals("default1", result.get(0));
    }

    @Test
    void whenSomeMapOrWithFunctionReturningNull_thenReturnsNull() {
        // When
        Option<String> option = Option.Some("hello");
        String result = option.mapOr("default", s -> null);

        // Then
        assertNull(result);
    }

    @Test
    void whenMapOrAfterFilter_thenReturnsCorrectValue() {
        // When
        Option<Integer> option = Option.Some(10)
                .filter(n -> n > 20); // becomes None

        Integer result = option.mapOr(-1, n -> n * 2);

        // Then
        assertEquals(-1, result);
    }
}