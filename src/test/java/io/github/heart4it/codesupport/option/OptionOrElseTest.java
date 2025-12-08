package io.github.heart4it.codesupport.option;

import io.github.heart4it.codesupport.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionOrElseTest {

    @Test
    void whenSomeOrElseSupplier_thenReturnsOriginalSome() {
        // When
        Option<String> original = Option.Some("original");
        Option<String> result = original.orElse(() -> Option.Some("fallback"));

        // Then
        assertTrue(result.isSome());
        assertEquals("original", result.unwrap());
    }

    @Test
    void whenNoneOrElseSupplier_thenReturnsSupplierResult() {
        // When
        Option<String> original = Option.None();
        Option<String> result = original.orElse(() -> Option.Some("fallback"));

        // Then
        assertTrue(result.isSome());
        assertEquals("fallback", result.unwrap());
    }

    @Test
    void whenNoneOrElseSupplierReturnsNone_thenReturnsNone() {
        // When
        Option<String> original = Option.None();
        Option<String> result = original.orElse(() -> Option.None());

        // Then
        assertTrue(result.isNone());
    }

    @Test
    void whenNoneOrElseSupplierComputesValue_thenReturnsComputedValue() {
        // When
        Option<String> original = Option.None();
        Option<String> result = original.orElse(() -> {
            String computed = "computed at " + System.currentTimeMillis();
            return Option.Some(computed);
        });

        // Then
        assertTrue(result.isSome());
        assertTrue(result.unwrap().startsWith("computed at"));
    }

    @Test
    void whenSomeOrElseSupplierNotCalled_thenSupplierNotInvoked() {
        // When
        Option<String> original = Option.Some("original");

        // Track if supplier was called
        boolean[] supplierCalled = {false};

        Option<String> result = original.orElse(() -> {
            supplierCalled[0] = true;
            return Option.Some("fallback");
        });

        // Then
        assertTrue(result.isSome());
        assertEquals("original", result.unwrap());
        assertFalse(supplierCalled[0]);
    }

    @Test
    void whenNoneOrElseSupplierThrowsException_thenExceptionPropagates() {
        // When
        Option<String> original = Option.None();

        // Then
        assertThrows(RuntimeException.class, () ->
                original.orElse(() -> {
                    throw new RuntimeException("Supplier failed");
                })
        );
    }

    @Test
    void whenChainingOrElseOperations_thenReturnsFirstNonNone() {
        // When
        Option<String> result = Option.<String>None()
                .orElse(() -> Option.None())
                .orElse(() -> Option.Some("first-valid"))
                .orElse(() -> Option.Some("second-valid"));

        // Then
        assertTrue(result.isSome());
        assertEquals("first-valid", result.unwrap());
    }


    @Test
    void whenMultipleOrElseWithComplexLogic_thenReturnsCorrectResult() {
        // When
        Option<String> result = Option.<String>None()
                .orElse(() -> {
                    // Some complex logic
                    boolean condition = true;
                    return condition ? Option.Some("complex") : Option.None();
                })
                .orElse(() -> Option.Some("simple-fallback"));

        // Then
        assertTrue(result.isSome());
        assertEquals("complex", result.unwrap());
    }
}