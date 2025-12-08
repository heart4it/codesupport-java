package io.github.heart4it.codesupport;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.*;

/**
 * A Result type that represents either a successful value (Ok) or an error value (Err).
 * This is similar to Either in functional programming and provides a type-safe way
 * to handle operations that can fail.
 *
 * @param <T> the type of the success value
 * @param <E> the type of the error value
 */
public sealed interface Result<T, E> permits Result.Ok, Result.Err {

    /**
     * Record representing a successful result.
     *
     * @param <T>   the type of the success value
     * @param <E>   the type of the error value
     * @param value the successful value
     */
    record Ok<T, E>(T value) implements Result<T, E> {
    }

    /**
     * Record representing an error result.
     *
     * @param <T>   the type of the success value
     * @param <E>   the type of the error value
     * @param error the error value
     */
    record Err<T, E>(E error) implements Result<T, E> {
    }

    /**
     * Creates a successful Result with the given value.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.ok("success");
     * // result.isOk() -> true
     * // result.unwrap() -> "success"
     * }</pre>
     *
     * @param <T>   the type of the success value
     * @param <E>   the type of the error value
     * @param value the successful value
     * @return a Result containing the successful value
     */
    static <T, E> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    /**
     * Creates a successful Result with the given value.
     * Alias for {@link #ok(Object)}.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.success("data");
     * // result.isSuccess() -> true
     * // result.unwrap() -> "data"
     * }</pre>
     *
     * @param <T>   the type of the success value
     * @param <E>   the type of the error value
     * @param value the successful value
     * @return a Result containing the successful value
     */
    static <T, E> Result<T, E> success(T value) {
        return ok(value);
    }

    /**
     * Creates an error Result with the given error value.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.err(404);
     * // result.isErr() -> true
     * // result.unwrapErr() -> 404
     * }</pre>
     *
     * @param <T>   the type of the success value
     * @param <E>   the type of the error value
     * @param error the error value
     * @return a Result containing the error value
     */
    static <T, E> Result<T, E> err(E error) {
        return new Err<>(error);
    }

    /**
     * Creates an error Result with the given error value.
     * Alias for {@link #err(Object)}.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, String> result = Result.error("not found");
     * // result.isError() -> true
     * // result.unwrapError() -> "not found"
     * }</pre>
     *
     * @param <T>   the type of the success value
     * @param <E>   the type of the error value
     * @param error the error value
     * @return a Result containing the error value
     */
    static <T, E> Result<T, E> error(E error) {
        return err(error);
    }

    /**
     * Returns true if this Result is Ok (successful).
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> success = Result.ok("hello");
     * Result<String, Integer> failure = Result.err(404);
     *
     * success.isOk() -> true
     * failure.isOk() -> false
     * }</pre>
     *
     * @return true if this Result is Ok, false otherwise
     */
    default boolean isOk() {
        return this instanceof Ok<T, E>;
    }

    /**
     * Returns true if this Result is successful.
     * Alias for {@link #isOk()}.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<Integer, String> result = Result.success(42);
     * result.isSuccess() -> true
     * }</pre>
     *
     * @return true if this Result is successful, false otherwise
     */
    default boolean isSuccess() {
        return this.isOk();
    }

    /**
     * Returns true if this Result is Err (an error).
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> success = Result.ok("hello");
     * Result<String, Integer> failure = Result.err(500);
     *
     * success.isErr() -> false
     * failure.isErr() -> true
     * }</pre>
     *
     * @return true if this Result is Err, false otherwise
     */
    default boolean isErr() {
        return this instanceof Err<T, E>;
    }

    /**
     * Returns true if this Result is an error.
     * Alias for {@link #isErr()}.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, String> result = Result.error("failed");
     * result.isError() -> true
     * }</pre>
     *
     * @return true if this Result is an error, false otherwise
     */
    default boolean isError() {
        return this.isErr();
    }

    /**
     * Returns true if this Result is Ok and the value matches the predicate.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result1 = Result.ok("hello");
     * Result<String, Integer> result2 = Result.ok("hi");
     * Result<String, Integer> result3 = Result.err(404);
     *
     * result1.isOkAnd(s -> s.length() > 3) -> true
     * result2.isOkAnd(s -> s.length() > 3) -> false
     * result3.isOkAnd(s -> s.length() > 3) -> false
     * }</pre>
     *
     * @param predicate the predicate to test the value against
     * @return true if this Result is Ok and the predicate returns true for the value,
     * false otherwise
     */
    default boolean isOkAnd(Predicate<? super T> predicate) {
        return this instanceof Ok<T, E> ok && predicate.test(ok.value());
    }

    /**
     * Returns true if this Result is Err and the error matches the predicate.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result1 = Result.err(404);
     * Result<String, Integer> result2 = Result.err(200);
     * Result<String, Integer> result3 = Result.ok("hello");
     *
     * result1.isErrAnd(e -> e >= 400) -> true
     * result2.isErrAnd(e -> e >= 400) -> false
     * result3.isErrAnd(e -> e >= 400) -> false
     * }</pre>
     *
     * @param predicate the predicate to test the error against
     * @return true if this Result is Err and the predicate returns true for the error,
     * false otherwise
     */
    default boolean isErrAnd(Predicate<? super E> predicate) {
        return this instanceof Err<T, E> e && predicate.test(e.error());
    }

    /**
     * Returns {@code other} if this Result is Ok, otherwise returns the Err value of this Result.
     * This is useful for chaining operations where you want to continue with another Result
     * only if the current one is successful.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> success = Result.ok("first");
     * Result<String, Integer> failure = Result.err(404);
     * Result<String, Integer> next = Result.ok("second");
     *
     * success.and(next) -> Ok("second")
     * failure.and(next) -> Err(404)
     * }</pre>
     *
     * @param <U>   the success type of the other Result
     * @param other the other Result to return if this Result is Ok
     * @return {@code other} if this Result is Ok, otherwise this Result's Err
     */
    default <U> Result<U, E> and(Result<U, E> other) {
        return this.isOk() ? other : err(this.unwrapErr());
    }

    /**
     * Calls the function {@code f} with the value of this Result if it is Ok,
     * otherwise returns the Err value of this Result.
     * This is useful for chaining operations that may fail.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<Integer, String> parseResult = Result.ok("123");
     * Result<Integer, String> result = parseResult.andThen(s -> {
     *     try {
     *         return Result.ok(Integer.parseInt(s));
     *     } catch (NumberFormatException e) {
     *         return Result.err("Invalid number");
     *     }
     * });
     * // result -> Ok(123)
     * }</pre>
     *
     * @param <U> the success type of the function's result
     * @param f   the function to apply to the value if this Result is Ok
     * @return the Result from applying {@code f} if this Result is Ok, otherwise this Result's Err
     */
    default <U> Result<U, E> andThen(Function<? super T, Result<U, E>> f) {
        return this instanceof Ok<T, E> ok ? f.apply(ok.value()) : err(this.unwrapErr());
    }

    /**
     * Returns this Result if it is Ok, otherwise returns {@code other}.
     * This is useful for providing fallback Results.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> primary = Result.err(404);
     * Result<String, Integer> fallback = Result.ok("fallback data");
     *
     * primary.or(fallback) -> Ok("fallback data")
     * }</pre>
     *
     * @param other the other Result to return if this Result is Err
     * @return this Result if it is Ok, otherwise {@code other}
     */
    default Result<T, E> or(Result<T, E> other) {
        return this.isOk() ? this : other;
    }

    /**
     * Returns this Result if it is Ok, otherwise calls the supplier function and returns its result.
     * This is useful for lazy evaluation of fallback Results.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.err(404);
     * Result<String, Integer> recovered = result.orElse(() -> Result.ok("recovered data"));
     * // recovered -> Ok("recovered data")
     * }</pre>
     *
     * @param f the supplier function that provides a fallback Result
     * @return this Result if it is Ok, otherwise the result of {@code f.get()}
     */
    default Result<T, E> orElse(Supplier<Result<T, E>> f) {
        return this.isOk() ? this : f.get();
    }

    /**
     * Applies the given mapping function to the {@code Ok} value of this
     * {@code Result<T, E>}, producing a {@code Result<U, E>}. If this result is
     * {@code Err}, the error is propagated unchanged.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Result<Integer, String> result = Result.success(5);
     *
     * Result<String, String> mapped =
     *     result.map(n -> "Number: " + n);
     *
     * // mapped is Ok("Number: 5")
     * }</pre>
     *
     * @param <U> the type produced by the mapping function
     * @param f   the function applied to the {@code Ok} value
     * @return a {@code Result<U, E>} containing the mapped value if this is {@code Ok};
     * otherwise an {@code Err} containing the original error
     */
    default <U> Result<U, E> map(Function<? super T, ? extends U> f) {
        return this instanceof Ok<T, E> ok ? ok(f.apply(ok.value())) : err(this.unwrapErr());
    }

    /**
     * Applies the given mapping function to the {@code Err} value of this
     * {@code Result<T, E>}, producing a {@code Result<T, F>}. If this result is
     * {@code Ok}, the value is returned unchanged.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Result<String, Integer> result = Result.err(404);
     *
     * Result<String, String> mapped =
     *     result.mapErr(code -> "HTTP Error: " + code);
     *
     * // mapped is Err("HTTP Error: 404")
     * }</pre>
     *
     * @param <F> the type of the mapped error
     * @param f   the function applied to the {@code Err} value
     * @return a {@code Result<T, F>} containing the mapped error if this is {@code Err};
     * otherwise an {@code Ok} containing the original value
     */
    default <F> Result<T, F> mapErr(Function<? super E, ? extends F> f) {
        return this instanceof Err<T, E> e ? err(f.apply(e.error())) : ok(this.unwrap());
    }

    /**
     * Returns the provided default value if this Result is Err,
     * otherwise applies the mapping function to the Ok value.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<Integer, String> result = Result.ok(5);
     * String value = result.mapOr("default", n -> "Number: " + n);
     * // value -> "Number: 5"
     *
     * Result<Integer, String> errResult = Result.err("error");
     * String defaultValue = errResult.mapOr("default", n -> "Number: " + n);
     * // defaultValue -> "default"
     * }</pre>
     *
     * @param <U>          the type of the resulting value
     * @param defaultValue the default value to return if this Result is Err
     * @param f            the mapping function to apply to the Ok value
     * @return the mapped value if this Result is Ok, otherwise the default value
     */
    default <U> U mapOr(U defaultValue, Function<? super T, ? extends U> f) {
        return this instanceof Ok<T, E> ok ? f.apply(ok.value()) : defaultValue;
    }

    /**
     * Returns the result of the default supplier if this Result is Err,
     * otherwise applies the mapping function to the Ok value.
     * The default supplier is only called if needed (lazy evaluation).
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<Integer, String> result = Result.ok(5);
     * String value = result.mapOrDefault(() -> "default", n -> "Number: " + n);
     * // value -> "Number: 5"
     *
     * Result<Integer, String> errResult = Result.err("error");
     * String defaultValue = errResult.mapOrDefault(() -> "computed default", n -> "Number: " + n);
     * // defaultValue -> "computed default"
     * }</pre>
     *
     * @param <U>             the type of the resulting value
     * @param defaultSupplier the supplier that provides the default value if this Result is Err
     * @param f               the mapping function to apply to the Ok value
     * @return the mapped value if this Result is Ok, otherwise the result of the default supplier
     */
    default <U> U mapOrDefault(Supplier<? extends U> defaultSupplier, Function<? super T, ? extends U> f) {
        return this instanceof Ok<T, E> ok ? f.apply(ok.value()) : defaultSupplier.get();
    }

    /**
     * Returns the result of the default supplier if this Result is Err,
     * otherwise applies the mapping function to the Ok value.
     * Alias for {@link #mapOrDefault(Supplier, Function)}.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<Integer, String> result = Result.err("error");
     * String value = result.mapOrElse(() -> "fallback", n -> "Number: " + n);
     * // value -> "fallback"
     * }</pre>
     *
     * @param <U>             the type of the resulting value
     * @param defaultSupplier the supplier that provides the default value if this Result is Err
     * @param f               the mapping function to apply to the Ok value
     * @return the mapped value if this Result is Ok, otherwise the result of the default supplier
     */
    default <U> U mapOrElse(Supplier<? extends U> defaultSupplier, Function<? super T, ? extends U> f) {
        return mapOrDefault(defaultSupplier, f);
    }

    /**
     * Returns the contained Ok value.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.ok("success");
     * String value = result.unwrap();
     * // value -> "success"
     *
     * Result<String, Integer> errResult = Result.err(404);
     * // errResult.unwrap() throws NoSuchElementException
     * }</pre>
     *
     * @return the contained Ok value
     * @throws NoSuchElementException if this Result is Err
     */
    default T unwrap() {
        if (this instanceof Ok<T, E> ok) return ok.value();
        throw new NoSuchElementException("called unwrap() on Err");
    }

    /**
     * Returns the contained Ok value or a provided default.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.ok("success");
     * String value = result.unwrapOr("default");
     * // value -> "success"
     *
     * Result<String, Integer> errResult = Result.err(404);
     * String defaultValue = errResult.unwrapOr("default");
     * // defaultValue -> "default"
     * }</pre>
     *
     * @param defaultValue the default value to return if this Result is Err
     * @return the contained Ok value if this Result is Ok, otherwise the default value
     */
    default T unwrapOr(T defaultValue) {
        return this instanceof Ok<T, E> ok ? ok.value() : defaultValue;
    }

    /**
     * Returns the contained Ok value or computes it from the supplier function.
     * The supplier is only called if this Result is Err (lazy evaluation).
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.err(404);
     * String value = result.unwrapOrElse(() -> "computed default");
     * // value -> "computed default"
     * }</pre>
     *
     * @param f the supplier function that provides a default value if this Result is Err
     * @return the contained Ok value if this Result is Ok, otherwise the result of {@code f.get()}
     */
    default T unwrapOrElse(Supplier<? extends T> f) {
        return this instanceof Ok<T, E> ok ? ok.value() : f.get();
    }

    /**
     * Returns the contained Ok value or computes it from the supplier function.
     * Alias for {@link #unwrapOrElse(Supplier)}.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.err(500);
     * String value = result.unwrapOrDefault(() -> "fallback value");
     * // value -> "fallback value"
     * }</pre>
     *
     * @param f the supplier function that provides a default value if this Result is Err
     * @return the contained Ok value if this Result is Ok, otherwise the result of {@code f.get()}
     */
    default T unwrapOrDefault(Supplier<? extends T> f) {
        return unwrapOrElse(f);
    }

    /**
     * Returns the contained Ok value.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.ok("data");
     * String value = result.expect("Should have data");
     * // value -> "data"
     *
     * Result<String, Integer> errResult = Result.err(404);
     * // errResult.expect("Data should be present") throws NoSuchElementException with message
     * }</pre>
     *
     * @param msg the message to use in the exception if this Result is Err
     * @return the contained Ok value
     * @throws NoSuchElementException if this Result is Err, with the provided message
     */
    default T expect(String msg) {
        if (this instanceof Ok<T, E> ok) return ok.value();
        throw new NoSuchElementException(msg);
    }

    /**
     * Returns the contained Ok value, or null if this Result is Err.
     * This is an unchecked version of {@link #unwrap()} that doesn't throw an exception.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.ok("success");
     * String value = result.unwrapUnchecked();
     * // value -> "success"
     *
     * Result<String, Integer> errResult = Result.err(404);
     * String nullValue = errResult.unwrapUnchecked();
     * // nullValue -> null
     * }</pre>
     *
     * @return the contained Ok value, or null if this Result is Err
     */
    default T unwrapUnchecked() {
        return this instanceof Ok<T, E> ok ? ok.value() : null;
    }

    /**
     * Returns the contained Err value.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.err(404);
     * Integer error = result.unwrapErr();
     * // error -> 404
     *
     * Result<String, Integer> okResult = Result.ok("success");
     * // okResult.unwrapErr() throws NoSuchElementException
     * }</pre>
     *
     * @return the contained Err value
     * @throws NoSuchElementException if this Result is Ok
     */
    default E unwrapErr() {
        if (this instanceof Err<T, E> e) return e.error();
        throw new NoSuchElementException("called unwrapErr() on Ok");
    }

    /**
     * Returns the contained Err value.
     * Alias for {@link #unwrapErr()}.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, String> result = Result.error("failure");
     * String error = result.unwrapError();
     * // error -> "failure"
     * }</pre>
     *
     * @return the contained Err value
     * @throws NoSuchElementException if this Result is Ok
     */
    default E unwrapError() {
        return this.unwrapErr();
    }

    /**
     * Returns the contained Err value, or null if this Result is Ok.
     * This is an unchecked version of {@link #unwrapErr()} that doesn't throw an exception.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.err(500);
     * Integer error = result.unwrapErrUnchecked();
     * // error -> 500
     *
     * Result<String, Integer> okResult = Result.ok("success");
     * Integer nullError = okResult.unwrapErrUnchecked();
     * // nullError -> null
     * }</pre>
     *
     * @return the contained Err value, or null if this Result is Ok
     */
    default E unwrapErrUnchecked() {
        return this instanceof Err<T, E> e ? e.error() : null;
    }

    /**
     * Returns the contained Err value.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.err(404);
     * Integer error = result.expectErr("Should have error");
     * // error -> 404
     *
     * Result<String, Integer> okResult = Result.ok("success");
     * // okResult.expectErr("Expected an error") throws NoSuchElementException with message
     * }</pre>
     *
     * @param msg the message to use in the exception if this Result is Ok
     * @return the contained Err value
     * @throws NoSuchElementException if this Result is Ok, with the provided message
     */
    default E expectErr(String msg) {
        if (this instanceof Err<T, E> e) return e.error();
        throw new NoSuchElementException(msg);
    }

    /**
     * Calls the provided action with the contained value if this Result is Ok,
     * then returns this Result unchanged.
     * This is useful for side effects like logging without affecting the Result.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.ok("data");
     * Result<String, Integer> logged = result.inspect(value -> System.out.println("Got: " + value));
     * // Prints "Got: data"
     * // logged -> Ok("data")
     * }</pre>
     *
     * @param action the action to perform with the value if this Result is Ok
     * @return this Result unchanged
     */
    default Result<T, E> inspect(Consumer<? super T> action) {
        if (this instanceof Ok<T, E> ok) action.accept(ok.value());
        return this;
    }

    /**
     * Executes the given action with the contained error if this result is an
     * {@code Err}, then returns this result unchanged. This is useful for
     * performing side effects such as logging without altering the result.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Result<String, Integer> result = Result.err(404);
     *
     * Result<String, Integer> logged =
     *     result.inspectErr(error -> System.out.println("Error: " + error));
     *
     * // Prints: "Error: 404"
     * // logged is Err(404)
     * }</pre>
     *
     * @param action the action to perform if this result is an {@code Err}
     * @return this result unchanged
     */
    default Result<T, E> inspectErr(Consumer<? super E> action) {
        if (this instanceof Err<T, E> e) action.accept(e.error());
        return this;
    }

    /**
     * Flattens a nested {@code Result<Result<T, E>, E>} into a single
     * {@code Result<T, E>}.
     * <ul>
     *   <li>If the outer result is {@code Ok}, the contained inner result is returned.</li>
     *   <li>If the outer result is {@code Err}, the error is propagated unchanged.</li>
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Result<Result<String, Integer>, Integer> nestedOk =
     *     Result.ok(Result.ok("success"));
     * Result<String, Integer> flattened = Result.flatten(nestedOk);
     * // flattened -> Ok("success")
     *
     * Result<Result<String, Integer>, Integer> nestedErr =
     *     Result.ok(Result.err(404));
     * Result<String, Integer> flattenedErr = Result.flatten(nestedErr);
     * // flattenedErr -> Err(404)
     *
     * Result<Result<String, Integer>, Integer> outerErr =
     *     Result.err(500);
     * Result<String, Integer> flattenedOuterErr = Result.flatten(outerErr);
     * // flattenedOuterErr -> Err(500)
     * }</pre>
     *
     * @param <T>    the success type contained in the inner result
     * @param <E>    the error type
     * @param nested the nested result to flatten
     * @return the flattened {@code Result<T, E>}
     */
    static <T, E> Result<T, E> flatten(Result<Result<T, E>, E> nested) {
        return nested instanceof Ok<Result<T, E>, E> ok ? ok.value() : err(nested.unwrapErr());
    }

    /**
     * Returns an iterable containing the Ok value if present, otherwise an empty iterable.
     * This is useful for using Results in for-each loops.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.ok("hello");
     * for (String value : result.iter()) {
     *     System.out.println(value); // Prints "hello"
     * }
     *
     * Result<String, Integer> errResult = Result.err(404);
     * for (String value : errResult.iter()) {
     *     // This block doesn't execute
     * }
     * }</pre>
     *
     * @return a List containing the Ok value if this Result is Ok, otherwise an empty List
     */
    default List<T> iter() {
        if (this instanceof Ok<T, ?> ok) {
            T value = ok.value();
            return value != null ?
                    java.util.Collections.singletonList(value) :
                    java.util.Collections.singletonList(null);
        }
        return java.util.Collections.emptyList();
    }

    /**
     * Converts this {@code Result<T, E>} into an {@code Option<T>} by discarding any error.
     * <p>
     * If this result is {@code Ok}, the returned {@code Option} contains its value.
     * If this result is {@code Err}, the returned {@code Option} is {@code None}.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Result<String, Integer> okResult = Result.ok("success");
     * Option<String> some = okResult.ok();
     * // some.isSome() -> true
     * // some.unwrap() -> "success"
     *
     * Result<String, Integer> errResult = Result.err(404);
     * Option<String> none = errResult.ok();
     * // none.isNone() -> true
     * }</pre>
     *
     * @return an {@code Option} containing the {@code Ok} value if this result is {@code Ok};
     * otherwise {@code Option.None()}
     */
    default Option<T> ok() {
        return this instanceof Ok<T, E> ok ? Option.Some(ok.value()) : Option.None();
    }

    /**
     * Converts this {@code Result<T, E>} into a {@code Result<Option<T>, E>}.
     * <p>
     * If this result is {@code Ok}, the contained value is wrapped in {@code Option.Some}
     * and returned as an {@code Ok}.
     * If this result is {@code Err}, the error is propagated unchanged.
     *
     * <p>This operation is useful when transforming a {@code Result<T, E>} so that
     * the success value becomes optional while preserving the error type.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Result<String, Integer> okResult = Result.ok("value");
     *
     * Result<Option<String>, Integer> transposedOk = okResult.transpose();
     * // transposedOk -> Ok(Some("value"))
     *
     * Result<String, Integer> errResult = Result.err(404);
     *
     * Result<Option<String>, Integer> transposedErr = errResult.transpose();
     * // transposedErr -> Err(404)
     * }</pre>
     *
     * @return a {@code Result<Option<T>, E>} containing {@code Some(value)} if this result is {@code Ok};
     * otherwise an {@code Err} containing the original error
     * @throws IllegalStateException if an unknown Result variant is encountered
     */
    default Result<Option<T>, E> transpose() {
        if (this instanceof Ok<T, E> ok) return Result.ok(Option.Some(ok.value()));
        if (this instanceof Err<T, E> e) return Result.err(e.error());
        throw new IllegalStateException("Invalid state");
    }

    /**
     * Converts a {@code CompletableFuture<T>} into a {@code CompletableFuture<Result<T, E>>}.
     * The resulting future completes with {@code Ok(value)} if the original future completes
     * normally, or {@code Err(mappedError)} if it completes exceptionally.
     *
     * <p><b>Example usage:</b>
     * <pre>{@code
     * CompletableFuture<String> future =
     *     CompletableFuture.completedFuture("success");
     *
     * CompletableFuture<Result<String, String>> resultFuture =
     *     Result.fromFuture(future, Throwable::getMessage);
     *
     * resultFuture.thenAccept(result -> {
     *     if (result.isOk()) {
     *         System.out.println("Success: " + result.unwrap());
     *     } else {
     *         System.out.println("Error: " + result.unwrapErr());
     *     }
     * });
     * }</pre>
     *
     * @param <T> the success type of the future
     * @param <E> the error type of the Result
     * @param future the CompletableFuture to convert
     * @param errorMapper a function mapping a {@link Throwable} to an error value of type {@code E}
     * @return a CompletableFuture that completes with a {@code Result}
     */
    static <T, E> CompletableFuture<Result<T, E>> fromFuture(
            CompletableFuture<T> future,
            Function<Throwable, E> errorMapper) {

        return future.handle((value, throwable) -> {
            if (throwable != null) {
                Throwable cause = throwable;
                if (throwable instanceof CompletionException && throwable.getCause() != null) {
                    cause = throwable.getCause();
                }
                E error = errorMapper.apply(cause);
                return Result.err(error);
            } else {
                return Result.ok(value);
            }
        });
    }

    /**
     * Converts this Result to a java.util.Optional.
     * Returns an Optional containing the Ok value if this Result is Ok, otherwise empty Optional.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.ok("value");
     * java.util.Optional<String> optional = result.toOptional();
     * // optional.isPresent() -> true
     * // optional.get() -> "value"
     *
     * Result<String, Integer> errResult = Result.err(404);
     * java.util.Optional<String> emptyOptional = errResult.toOptional();
     * // emptyOptional.isEmpty() -> true
     * }</pre>
     *
     * @return an Optional containing the Ok value if this Result is Ok, otherwise empty Optional
     */
    default java.util.Optional<T> toOptional() {
        return this instanceof Ok<T, E> ok ? java.util.Optional.of(ok.value()) : java.util.Optional.empty();
    }

    /**
     * Converts this Result to an Optional, throwing an exception if this is an Err.
     * Null-safe implementation that preserves null values.
     *
     * @param <Ex>              the type of the exception
     * @param exceptionSupplier the function that creates the exception from the error value
     * @return an Optional containing the success value if this Result is Ok
     * @throws Ex if this Result is Err
     */
    default <Ex extends RuntimeException> Optional<T> toOptionalOrThrow(
            Function<? super E, ? extends Ex> exceptionSupplier) {
        if (this instanceof Ok<T, E> ok) {
            T value = ok.value();
            return Optional.ofNullable(value);
        }
        if (this instanceof Err<T, E> err) {
            E error = err.error();
            throw exceptionSupplier.apply(error);
        }
        throw new IllegalStateException("Unknown Result type: " + this.getClass().getName());
    }

    /**
     * Converts this Result to a java.util.Optional, throwing an IllegalStateException if this Result is Err.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> result = Result.ok("success");
     * java.util.Optional<String> optional = result.toOptionalOrThrow();
     * // optional -> Optional["success"]
     *
     * Result<String, Integer> errResult = Result.err(404);
     * // errResult.toOptionalOrThrow() throws IllegalStateException with message containing "404"
     * }</pre>
     *
     * @return an Optional containing the Ok value if this Result is Ok
     * @throws IllegalStateException if this Result is Err
     */
    default java.util.Optional<T> toOptionalOrThrow() {
        if (this instanceof Ok<T, E> ok) {
            return java.util.Optional.of(ok.value());
        } else if (this instanceof Err<T, E> e) {
            throw new IllegalStateException("Cannot convert Err to Optional: " + e.error());
        }
        throw new IllegalStateException("Unknown Result variant");
    }

    /**
     * Returns a string representation of this Result for debugging purposes.
     * The format is "Ok(value)" for successful Results or "Err(error)" for error Results.
     *
     * <p>Example usage:
     * <pre>{@code
     * Result<String, Integer> success = Result.ok("hello");
     * String debug1 = success.toDebugString();
     * // debug1 -> "Ok(hello)"
     *
     * Result<String, Integer> failure = Result.err(404);
     * String debug2 = failure.toDebugString();
     * // debug2 -> "Err(404)"
     * }</pre>
     *
     * @return a debug string representation of this Result
     */
    default String toDebugString() {
        return this.isOk() ? "Ok(" + this.unwrap() + ")" : "Err(" + this.unwrapErr() + ")";
    }
}