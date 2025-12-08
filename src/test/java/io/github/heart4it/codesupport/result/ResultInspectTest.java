package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ResultInspectTest {

    @Test
    void givenOkResult_whenInspectWithAction_thenActionCalledAndSameResultReturned() {
        // Given
        Result<String, Integer> result = Result.ok("success");
        List<String> inspectedValues = new ArrayList<>();
        Consumer<String> action = inspectedValues::add;

        // When
        Result<String, Integer> returnedResult = result.inspect(action);

        // Then
        assertEquals(1, inspectedValues.size());
        assertEquals("success", inspectedValues.get(0));
        assertSame(result, returnedResult);
        assertTrue(returnedResult.isOk());
        assertEquals("success", returnedResult.unwrap());
    }

    @Test
    void givenErrResult_whenInspectWithAction_thenActionNotCalledAndSameResultReturned() {
        // Given
        Result<String, Integer> result = Result.err(404);
        boolean[] actionCalled = {false};
        Consumer<String> action = value -> actionCalled[0] = true;

        // When
        Result<String, Integer> returnedResult = result.inspect(action);

        // Then
        assertFalse(actionCalled[0]);
        assertSame(result, returnedResult);
        assertTrue(returnedResult.isErr());
        assertEquals(404, returnedResult.unwrapErr());
    }

    @Test
    void givenOkResultWithNullValue_whenInspect_thenActionCalledWithNull() {
        // Given
        Result<String, Integer> result = Result.ok(null);
        List<String> inspectedValues = new ArrayList<>();
        Consumer<String> action = inspectedValues::add;

        // When
        Result<String, Integer> returnedResult = result.inspect(action);

        // Then
        assertEquals(1, inspectedValues.size());
        assertNull(inspectedValues.get(0));
        assertSame(result, returnedResult);
    }

    @Test
    void givenOkResult_whenInspectWithSideEffect_thenSideEffectOccurs() {
        // Given
        Result<String, Integer> result = Result.ok("hello");
        StringBuilder sideEffect = new StringBuilder();
        Consumer<String> action = value -> sideEffect.append("Inspected: ").append(value);

        // When
        Result<String, Integer> returnedResult = result.inspect(action);

        // Then
        assertEquals("Inspected: hello", sideEffect.toString());
        assertSame(result, returnedResult);
    }

    @Test
    void givenOkResult_whenInspectActionThrowsException_thenExceptionPropagates() {
        // Given
        Result<String, Integer> result = Result.ok("data");
        Consumer<String> action = value -> {
            throw new RuntimeException("Inspection failed");
        };

        // When & Then
        assertThrows(RuntimeException.class, () -> result.inspect(action));
    }

    @Test
    void givenErrResult_whenInspectActionThrowsException_thenNoExceptionAndSameResultReturned() {
        // Given
        Result<String, Integer> result = Result.err(500);
        Consumer<String> action = value -> {
            throw new RuntimeException("This should not happen");
        };

        // When
        Result<String, Integer> returnedResult = result.inspect(action);

        // Then
        assertSame(result, returnedResult);
        assertTrue(returnedResult.isErr());
        assertEquals(500, returnedResult.unwrapErr());
    }

    @Test
    void givenChainedInspectCallsOnOkResult_thenAllActionsCalled() {
        // Given
        Result<String, Integer> result = Result.ok("test");
        List<String> actionsCalled = new ArrayList<>();
        Consumer<String> action1 = value -> actionsCalled.add("first: " + value);
        Consumer<String> action2 = value -> actionsCalled.add("second: " + value);

        // When
        Result<String, Integer> returnedResult = result.inspect(action1).inspect(action2);

        // Then
        assertEquals(2, actionsCalled.size());
        assertEquals("first: test", actionsCalled.get(0));
        assertEquals("second: test", actionsCalled.get(1));
        assertSame(result, returnedResult);
    }

    @Test
    void givenChainedInspectCallsOnErrResult_thenNoActionsCalled() {
        // Given
        Result<String, Integer> result = Result.err(400);
        int[] callCount = {0};
        Consumer<String> action = value -> callCount[0]++;

        // When
        Result<String, Integer> returnedResult = result.inspect(action).inspect(action);

        // Then
        assertEquals(0, callCount[0]);
        assertSame(result, returnedResult);
    }

    @Test
    void givenOkResultWithInteger_whenInspect_thenActionCalledWithInteger() {
        // Given
        Result<Integer, String> result = Result.ok(42);
        List<Integer> inspectedValues = new ArrayList<>();
        Consumer<Integer> action = inspectedValues::add;

        // When
        Result<Integer, String> returnedResult = result.inspect(action);

        // Then
        assertEquals(1, inspectedValues.size());
        assertEquals(42, inspectedValues.get(0));
        assertSame(result, returnedResult);
    }

    @Test
    void givenOkResultAfterMapOperation_whenInspect_thenActionCalledWithMappedValue() {
        // Given
        Result<String, Object> result = Result.ok("hello").map(String::toUpperCase);
        List<String> inspectedValues = new ArrayList<>();
        Consumer<String> action = inspectedValues::add;

        // When
        Result<String, Object> returnedResult = result.inspect(action);

        // Then
        assertEquals(1, inspectedValues.size());
        assertEquals("HELLO", inspectedValues.get(0));
        assertSame(result, returnedResult);
    }
}