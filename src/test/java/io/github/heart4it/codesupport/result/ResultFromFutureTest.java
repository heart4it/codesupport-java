package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Result.fromFuture() static method
 */
public class ResultFromFutureTest {

    @Test

    @Timeout(5)
    void fromFuture_SuccessfulFuture_ShouldReturnOkWithValue() throws Exception {
        // Given
        CompletableFuture<String> future = CompletableFuture.completedFuture("success");
        Function<Throwable, String> errorMapper = Throwable::getMessage;

        // When
        CompletableFuture<Result<String, String>> resultFuture = Result.fromFuture(future, errorMapper);
        Result<String, String> result = resultFuture.get(1, TimeUnit.SECONDS);

        // Then
        assertTrue(result.isOk());
        assertEquals("success", result.unwrap());
    }

    @Test

    @Timeout(5)
    void fromFuture_FailedFuture_ShouldReturnErrWithMappedError() throws Exception {
        // Given
        CompletableFuture<String> future = CompletableFuture.failedFuture(new RuntimeException("future failed"));
        Function<Throwable, String> errorMapper = Throwable::getMessage;

        // When
        CompletableFuture<Result<String, String>> resultFuture = Result.fromFuture(future, errorMapper);
        Result<String, String> result = resultFuture.get(1, TimeUnit.SECONDS);

        // Then
        assertTrue(result.isErr());
        assertEquals("future failed", result.unwrapErr());
    }

    @Test

    @Timeout(5)
    void fromFuture_WithIntegerValues_ShouldWork() throws Exception {
        // Given
        CompletableFuture<Integer> future = CompletableFuture.completedFuture(42);
        Function<Throwable, String> errorMapper = Throwable::getMessage;

        // When
        CompletableFuture<Result<Integer, String>> resultFuture = Result.fromFuture(future, errorMapper);
        Result<Integer, String> result = resultFuture.get(1, TimeUnit.SECONDS);

        // Then
        assertTrue(result.isOk());
        assertEquals(42, result.unwrap().intValue());
    }


    @Test

    @Timeout(5)
    void fromFuture_WithNullValue_ShouldWork() throws Exception {
        // Given
        CompletableFuture<String> future = CompletableFuture.completedFuture(null);
        Function<Throwable, String> errorMapper = Throwable::getMessage;

        // When
        CompletableFuture<Result<String, String>> resultFuture = Result.fromFuture(future, errorMapper);
        Result<String, String> result = resultFuture.get(1, TimeUnit.SECONDS);

        // Then
        assertTrue(result.isOk());
        assertNull(result.unwrap());
    }

    @Test

    @Timeout(5)
    void fromFuture_WithDifferentExceptionTypes_ShouldWork() throws Exception {
        // Given
        CompletableFuture<String> future = CompletableFuture.failedFuture(new IllegalArgumentException("invalid argument"));
        Function<Throwable, String> errorMapper = ex -> ex.getClass().getSimpleName() + ": " + ex.getMessage();

        // When
        CompletableFuture<Result<String, String>> resultFuture = Result.fromFuture(future, errorMapper);
        Result<String, String> result = resultFuture.get(1, TimeUnit.SECONDS);

        // Then
        assertTrue(result.isErr());
        assertEquals("IllegalArgumentException: invalid argument", result.unwrapErr());
    }

    @Test

    @Timeout(5)
    void fromFuture_WithDelayedSuccessfulFuture_ShouldWork() throws Exception {
        // Given
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "delayed success";
        });
        Function<Throwable, String> errorMapper = Throwable::getMessage;

        // When
        CompletableFuture<Result<String, String>> resultFuture = Result.fromFuture(future, errorMapper);
        Result<String, String> result = resultFuture.get(2, TimeUnit.SECONDS);

        // Then
        assertTrue(result.isOk());
        assertEquals("delayed success", result.unwrap());
    }

    @Test

    @Timeout(5)
    void fromFuture_ShouldApplyErrorMapper() throws Exception {
        // Given
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("test error");
        });
        Function<Throwable, String> errorMapper = throwable -> "Mapped: " + throwable.getMessage();

        // When
        CompletableFuture<Result<String, String>> resultFuture = Result.fromFuture(future, errorMapper);
        Result<String, String> result = resultFuture.get(2, TimeUnit.SECONDS);

        // Then
        assertTrue(result.isErr());
        assertEquals("Mapped: test error", result.unwrapErr());
    }

    @Test

    @Timeout(5)
    void fromFuture_WithDelayedFailedFuture_ShouldWork() throws Exception {
        // Given
        RuntimeException expectedException = new RuntimeException("delayed failure");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            throw expectedException;
        });
        Function<Throwable, String> errorMapper = Throwable::getMessage;

        // When
        CompletableFuture<Result<String, String>> resultFuture = Result.fromFuture(future, errorMapper);
        Result<String, String> result = resultFuture.get(2, TimeUnit.SECONDS);

        // Then
        assertTrue(result.isErr());
        assertEquals(expectedException.getMessage(), result.unwrapErr()); // Use the same exception instance
    }


    @Test

    @Timeout(5)
    void fromFuture_WithErrorMapperThatThrows_ShouldPropagateException() throws Exception {
        // Given
        CompletableFuture<String> future = CompletableFuture.failedFuture(new RuntimeException("original error"));
        Function<Throwable, String> errorMapper = ex -> {
            throw new IllegalStateException("error mapper failed");
        };

        // When
        CompletableFuture<Result<String, String>> resultFuture = Result.fromFuture(future, errorMapper);

        // Then
        assertThrows(Exception.class, () -> resultFuture.get(1, TimeUnit.SECONDS));
    }
}