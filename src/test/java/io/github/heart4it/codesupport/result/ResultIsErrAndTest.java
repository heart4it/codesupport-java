package io.github.heart4it.codesupport.result;

import io.github.heart4it.codesupport.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultIsErrAndTest {

    @Test
    void givenErrResultWithMatchingPredicate_whenIsErrAnd_thenReturnsTrue() {
        // Given
        Result<String, Integer> result = Result.err(404);
        Predicate<Integer> predicate = error -> error == 404;

        // When
        boolean actualResult = result.isErrAnd(predicate);

        // Then
        assertTrue(actualResult);
    }

    @Test
    void givenErrResultWithNonMatchingPredicate_whenIsErrAnd_thenReturnsFalse() {
        // Given
        Result<String, Integer> result = Result.err(404);
        Predicate<Integer> predicate = error -> error == 500;

        // When
        boolean actualResult = result.isErrAnd(predicate);

        // Then
        assertFalse(actualResult);
    }

    @Test
    void givenOkResultWithAnyPredicate_whenIsErrAnd_thenReturnsFalse() {
        // Given
        Result<String, Integer> result = Result.ok("success");
        Predicate<Integer> predicate = error -> error == 404;

        // When
        boolean actualResult = result.isErrAnd(predicate);

        // Then
        assertFalse(actualResult);
    }

    @Test
    void givenErrResultWithNullValueAndNullAcceptingPredicate_whenIsErrAnd_thenReturnsTrue() {
        // Given
        Result<String, String> result = Result.err(null);
        Predicate<String> predicate = error -> error == null;

        // When
        boolean actualResult = result.isErrAnd(predicate);

        // Then
        assertTrue(actualResult);
    }

    @Test
    void givenErrResultWithNullValueAndNullRejectingPredicate_whenIsErrAnd_thenReturnsFalse() {
        // Given
        Result<String, String> result = Result.err(null);
        Predicate<String> predicate = error -> error != null && error.length() > 0;

        // When
        boolean actualResult = result.isErrAnd(predicate);

        // Then
        assertFalse(actualResult);
    }

    @Test
    void givenErrResultWithComplexPredicate_whenIsErrAnd_thenReturnsCorrectResult() {
        // Given
        Result<String, Integer> result = Result.err(150);
        Predicate<Integer> predicate = error -> error >= 100 && error < 200 && error % 2 == 0;

        // When
        boolean actualResult = result.isErrAnd(predicate);

        // Then
        assertTrue(actualResult); // 150 IS even (150 ÷ 2 = 75)
    }

    @Test
    void givenErrResultWithComplexPredicateCheckingOddNumber_whenIsErrAnd_thenReturnsFalse() {
        // Given
        Result<String, Integer> result = Result.err(151); // Use an odd number
        Predicate<Integer> predicate = error -> error >= 100 && error < 200 && error % 2 == 0;

        // When
        boolean actualResult = result.isErrAnd(predicate);

        // Then
        assertFalse(actualResult); // 151 is not even (it's odd)
    }

    @Test
    void givenErrResultWithStringErrorAndPredicate_whenIsErrAnd_thenReturnsTrueForMatching() {
        // Given
        Result<Integer, String> result = Result.err("database connection failed");
        Predicate<String> predicate = error -> error.contains("database") && error.endsWith("failed");

        // When
        boolean actualResult = result.isErrAnd(predicate);

        // Then
        assertTrue(actualResult);
    }

    @Test
    void givenErrResultWithCustomObjectError_whenIsErrAnd_thenReturnsBasedOnPredicate() {
        // Given
        ErrorDetail errorDetail = new ErrorDetail("AUTH_ERROR", 401, "Unauthorized");
        Result<String, ErrorDetail> result = Result.err(errorDetail);
        Predicate<ErrorDetail> predicate = error ->
                "AUTH_ERROR".equals(error.getCode()) && error.getStatusCode() == 401;

        // When
        boolean actualResult = result.isErrAnd(predicate);

        // Then
        assertTrue(actualResult);
    }

    @Test
    void givenErrResultWithAlwaysTruePredicate_whenIsErrAnd_thenReturnsTrue() {
        // Given
        Result<String, Integer> result = Result.err(999);
        Predicate<Integer> predicate = error -> true;

        // When
        boolean actualResult = result.isErrAnd(predicate);

        // Then
        assertTrue(actualResult);
    }

    @Test
    void givenErrResultWithAlwaysFalsePredicate_whenIsErrAnd_thenReturnsFalse() {
        // Given
        Result<String, Integer> result = Result.err(999);
        Predicate<Integer> predicate = error -> false;

        // When
        boolean actualResult = result.isErrAnd(predicate);

        // Then
        assertFalse(actualResult);
    }

    // Helper class for test
    static class ErrorDetail {
        private final String code;
        private final int statusCode;
        private final String message;

        public ErrorDetail(String code, int statusCode, String message) {
            this.code = code;
            this.statusCode = statusCode;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getMessage() {
            return message;
        }
    }
}