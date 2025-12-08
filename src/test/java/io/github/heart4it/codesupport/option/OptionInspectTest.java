package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class OptionInspectTest {

    @Test
    void givenSomeOption_whenInspect_thenConsumerCalledWithValue() {
        // Given
        Option<String> option = Option.Some("hello");
        List<String> inspectedValues = new ArrayList<>();
        Consumer<String> inspector = inspectedValues::add;

        // When
        Option<String> result = option.inspect(inspector);

        // Then
        assertEquals(1, inspectedValues.size());
        assertEquals("hello", inspectedValues.get(0));
        assertSame(option, result);
    }

    @Test
    void givenNoneOption_whenInspect_thenConsumerNotCalled() {
        // Given
        Option<String> option = Option.None();
        boolean[] consumerCalled = {false};
        Consumer<String> inspector = s -> consumerCalled[0] = true;

        // When
        Option<String> result = option.inspect(inspector);

        // Then
        assertFalse(consumerCalled[0]);
        assertSame(option, result);
    }

    @Test
    void givenSomeOption_whenInspectMutatesValue_thenOriginalValueUnchanged() {
        // Given
        Option<StringBuilder> option = Option.Some(new StringBuilder("hello"));
        Consumer<StringBuilder> inspector = sb -> sb.append(" inspected");

        // When
        Option<StringBuilder> result = option.inspect(inspector);

        // Then
        assertEquals("hello inspected", result.unwrap().toString());
        assertSame(option, result);
    }

    @Test
    void givenSomeOption_whenInspectThrowsException_thenExceptionPropagates() {
        // Given
        Option<String> option = Option.Some("hello");
        Consumer<String> inspector = s -> {
            throw new RuntimeException("Inspection failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> option.inspect(inspector));
    }

    @Test
    void givenSomeOption_whenChainedInspect_thenAllConsumersCalled() {
        // Given
        Option<String> option = Option.Some("hello");
        List<String> inspectedValues = new ArrayList<>();
        Consumer<String> inspector1 = s -> inspectedValues.add("first: " + s);
        Consumer<String> inspector2 = s -> inspectedValues.add("second: " + s);

        // When
        Option<String> result = option.inspect(inspector1).inspect(inspector2);

        // Then
        assertEquals(2, inspectedValues.size());
        assertEquals("first: hello", inspectedValues.get(0));
        assertEquals("second: hello", inspectedValues.get(1));
        assertSame(option, result);
    }

    @Test
    void givenNoneOption_whenChainedInspect_thenNoConsumersCalled() {
        // Given
        Option<String> option = Option.None();
        int[] callCount = {0};
        Consumer<String> inspector = s -> callCount[0]++;

        // When
        Option<String> result = option.inspect(inspector).inspect(inspector);

        // Then
        assertEquals(0, callCount[0]);
        assertSame(option, result);
    }

    @Test
    void givenSomeOption_whenInspectAfterMap_thenConsumerCalledWithMappedValue() {
        // Given
        Option<String> option = Option.Some("hello").map(String::toUpperCase);
        List<String> inspectedValues = new ArrayList<>();
        Consumer<String> inspector = inspectedValues::add;

        // When
        Option<String> result = option.inspect(inspector);

        // Then
        assertEquals(1, inspectedValues.size());
        assertEquals("HELLO", inspectedValues.get(0));
        assertSame(option, result);
    }

    @Test
    void givenSomeOption_whenInspectWithSideEffects_thenSideEffectsOccur() {
        // Given
        Option<String> option = Option.Some("test");
        StringBuilder sideEffect = new StringBuilder();
        Consumer<String> inspector = s -> sideEffect.append("Inspected: ").append(s);

        // When
        Option<String> result = option.inspect(inspector);

        // Then
        assertEquals("Inspected: test", sideEffect.toString());
        assertSame(option, result);
    }

    @Test
    void givenSomeOption_whenInspectReturnsSameOption_thenOriginalPreserved() {
        // Given
        Option<String> original = Option.Some("original");
        Consumer<String> inspector = s -> {
        }; // no-op

        // When
        Option<String> result = original.inspect(inspector);

        // Then
        assertSame(original, result);
        assertEquals("original", result.unwrap());
    }
}