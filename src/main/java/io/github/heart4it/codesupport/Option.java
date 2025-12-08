package io.github.heart4it.codesupport;

import java.util.*;
import java.util.function.*;

/**
 * A type-safe container that may or may not contain a value.
 * Similar to {@link java.util.Optional} but with richer functional
 * operations and explicit {@code Some}/{@code None} semantics inspired
 * by functional programming languages.
 *
 * <p>This is a {@code sealed} interface (Java 17+) with two permitted
 * implementations: {@link Option.Some} and {@link Option.None}.
 *
 * @param <T> the type of the value contained in the Option
 * @since 21
 */
public sealed interface Option<T> permits Option.Some, Option.None {

    /**
     * Record representing an {@code Option} that contains a value.
     *
     * @param <T>   the type of the contained value
     * @param value the contained (non-null) value
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("hello");
     * System.out.println(s); // prints: Some(hello)
     * }</pre>
     */
    record Some<T>(T value) implements Option<T> {


        /**
         * Constructs a {@link Some} wrapping the given non-null value.
         * <p>
         * The compact constructor automatically enforces the invariant that
         * {@code Some} cannot contain {@code null}. If a {@code null} value is passed,
         * a {@link NullPointerException} is thrown immediately.
         *
         * <p><b>Example:</b></p>
         * <pre>{@code
         * Option<Integer> some = Option.Some(42);
         * System.out.println(some); // prints: Some[value=42]
         *
         * // This throws NullPointerException:
         * Option<String> bad = Option.Some(null);
         * }</pre>
         *
         * @param value the value to wrap; must not be {@code null}
         * @throws NullPointerException if {@code value} is {@code null}
         */
        public Some {
            java.util.Objects.requireNonNull(value, "Some cannot contain null");
        }

        /**
         * Convenience accessor that returns the contained value.
         *
         * @return the non-null contained value
         *
         * <p>Example:
         * <pre>{@code
         * Option.Some<String> s = Option.Some("value");
         * String v = s.get();
         * System.out.println(v); // prints: value
         * }</pre>
         */
        public T get() {
            return value;
        }
    }

    /**
     * Record representing an empty {@code Option} that contains no value.
     *
     * @param <T> the type parameter for consistency
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> n = Option.None();
     * System.out.println(n); // prints: None
     * }</pre>
     */
    record None<T>() implements Option<T> {
    }

    /**
     * Creates a {@code Some} {@code Option} containing the given value.
     *
     * @param <T>   the type of the value
     * @param value the value to contain. Must not be null.
     * @return a {@code Some} {@code Option} containing the value
     * @throws NullPointerException if {@code value} is null
     *
     * <p>Example:
     * <pre>{@code
     * Option<Integer> some = Option.Some(42);
     * // some.isSome() -> true
     * // some.unwrap() -> 42
     * }</pre>
     */
    static <T> Option<T> Some(T value) {
        return new Some<>(value);
    }

    /**
     * Creates an empty {@code None} {@code Option}.
     *
     * @param <T> the type parameter
     * @return an empty {@code None} {@code Option}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> none = Option.None();
     * // none.isNone() -> true
     * }</pre>
     */
    static <T> Option<T> None() {
        return new None<>();
    }

    /**
     * Converts a {@link java.util.Optional} to an {@code Option}.
     *
     * @param <T> the type of the value
     * @param opt the {@link java.util.Optional} to convert
     * @return {@code Some} containing the value if {@code opt} is present, otherwise {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Optional<String> jOpt = Optional.of("a");
     * Option<String> opt = Option.fromOptional(jOpt);
     * // opt -> Some("a")
     *
     * Optional<String> empty = Optional.empty();
     * Option<String> o2 = Option.fromOptional(empty);
     * // o2 -> None
     * }</pre>
     */
    static <T> Option<T> fromOptional(Optional<T> opt) {
        return opt.map(Option::Some).orElseGet(Option::None);
    }

    /**
     * Returns {@code true} if this {@code Option} contains a value (is {@code Some}).
     *
     * @return {@code true} if this {@code Option} is {@code Some}, {@code false} if {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> some = Option.Some("x");
     * Option<String> none = Option.None();
     * assert some.isSome();
     * assert !none.isSome();
     * }</pre>
     */
    default boolean isSome() {
        return this instanceof Some<T>;
    }

    /**
     * Returns {@code true} if this {@code Option} is empty (is {@code None}).
     *
     * @return {@code true} if this {@code Option} is {@code None}, {@code false} if {@code Some}
     *
     * <p>Example:
     * <pre>{@code
     * Option<Integer> none = Option.None();
     * assert none.isNone();
     * Option<Integer> some = Option.Some(1);
     * assert !some.isNone();
     * }</pre>
     */
    default boolean isNone() {
        return this instanceof None<T>;
    }

    /**
     * Returns {@code other} if this {@code Option} is {@code Some}, otherwise returns {@code None}.
     *
     * @param other the other {@code Option} to return if this {@code Option} is {@code Some}
     * @return {@code other} if this {@code Option} is {@code Some}, otherwise {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> a = Option.Some("a");
     * Option<String> b = Option.Some("b");
     * Option<String> r = a.and(b);
     * // r -> Some("b")
     *
     * Option<String> none = Option.None();
     * Option<String> r2 = none.and(b);
     * // r2 -> None
     * }</pre>
     */
    default Option<T> and(Option<T> other) {
        return this.isSome() ? other : None();
    }

    /**
     * Calls the function {@code f} with the contained value if this {@code Option} is {@code Some},
     * otherwise returns {@code None}. This is similar to {@code flatMap} for {@code Option}.
     *
     * @param f the function to apply to the contained value
     * @return the {@code Option} returned by {@code f} if this {@code Option} is {@code Some}, otherwise {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("123");
     * Option<Integer> parsed = s.andThen(str -> {
     *     try {
     *         return Option.Some(Integer.parseInt(str));
     *     } catch (NumberFormatException e) {
     *         return Option.None();
     *     }
     * });
     * // parsed -> Some(123)
     * }</pre>
     */
    default Option<T> andThen(Function<? super T, Option<T>> f) {
        return this instanceof Some<T> s ? f.apply(s.value()) : None();
    }

    /**
     * Returns this {@code Option} if it is {@code Some}, otherwise returns {@code other}.
     *
     * @param other the other {@code Option} to return if this {@code Option} is {@code None}
     * @return this {@code Option} if it is {@code Some}, otherwise {@code other}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> primary = Option.None();
     * Option<String> fallback = Option.Some("fallback");
     * Option<String> r = primary.or(fallback);
     * // r -> Some("fallback")
     * }</pre>
     */
    default Option<T> or(Option<T> other) {
        return this.isSome() ? this : other;
    }

    /**
     * Returns this {@code Option} if it is {@code Some}, otherwise calls the supplier and returns its result.
     *
     * @param f the supplier that provides a fallback {@code Option}
     * @return this {@code Option} if it is {@code Some}, otherwise the result of {@code f.get()}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> none = Option.None();
     * Option<String> r = none.orElse(() -> Option.Some("computed"));
     * // r -> Some("computed")
     * }</pre>
     */
    default Option<T> orElse(Supplier<Option<T>> f) {
        return this.isSome() ? this : f.get();
    }

    /**
     * Returns this {@code Option} if it is {@code Some} and the other is {@code None}, or the other {@code Option}
     * if it is {@code Some} and this is {@code None}. Returns {@code None} if both are {@code Some} or both are {@code None}.
     *
     * @param other the other {@code Option}
     * @return this {@code Option} or the other {@code Option} if exactly one is {@code Some}, otherwise {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> a = Option.Some("a");
     * Option<String> b = Option.None();
     * Option<String> r = a.xor(b);
     * // r -> Some("a")
     *
     * Option<String> both = Option.Some("x");
     * Option<String> both2 = Option.Some("y");
     * Option<String> r2 = both.xor(both2);
     * // r2 -> None
     * }</pre>
     */
    default Option<T> xor(Option<T> other) {
        if (this.isSome() == other.isSome()) return None();
        return this.isSome() ? this : other;
    }

    /**
     * Maps an {@code Option<T>} to {@code Option<U>} by applying a function to the contained value.
     *
     * @param <U> the type of the value returned by the mapping function
     * @param f   the mapping function to apply to the contained value
     * @return a {@code Some} containing the result of applying {@code f} to the contained value,
     *         or {@code None} if this {@code Option} is {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("hello");
     * Option<Integer> len = s.map(String::length);
     * // len -> Some(5)
     * }</pre>
     */
    default <U> Option<U> map(Function<? super T, ? extends U> f) {
        return this instanceof Some<T> s ? Some(f.apply(s.value())) : None();
    }

    /**
     * Returns the provided default value if this {@code Option} is {@code None},
     * otherwise applies the mapping function to the contained value.
     *
     * @param <U>          the type of the resulting value
     * @param defaultValue the default value to return if this {@code Option} is {@code None}
     * @param f            the mapping function to apply to the contained value
     * @return the mapped value if this {@code Option} is {@code Some}, otherwise the default value
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("hello");
     * int len = s.mapOr(0, String::length); // -> 5
     *
     * Option<String> none = Option.None();
     * int def = none.mapOr(0, String::length); // -> 0
     * }</pre>
     */
    default <U> U mapOr(U defaultValue, Function<? super T, ? extends U> f) {
        return this instanceof Some<T> s ? f.apply(s.value()) : defaultValue;
    }

    /**
     * Returns the result of the default supplier if this {@code Option} is {@code None},
     * otherwise applies the mapping function to the contained value.
     *
     * @param <U>             the type of the resulting value
     * @param defaultSupplier the supplier that provides the default value
     * @param f               the mapping function to apply to the contained value
     * @return the mapped value if this {@code Option} is {@code Some}, otherwise the result of the default supplier
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("hello");
     * String upper = s.mapOrDefault(() -> "none", str -> str.toUpperCase()); // -> "HELLO"
     *
     * Option<String> none = Option.None();
     * String def = none.mapOrDefault(() -> "computed", str -> str.toUpperCase()); // -> "computed"
     * }</pre>
     */
    default <U> U mapOrDefault(Supplier<? extends U> defaultSupplier, Function<? super T, ? extends U> f) {
        return this instanceof Some<T> s ? f.apply(s.value()) : defaultSupplier.get();
    }

    /**
     * Alias for {@link #mapOrDefault(java.util.function.Supplier, java.util.function.Function)}.
     *
     * @param <U>             the type of the resulting value
     * @param defaultSupplier the supplier that provides the default value
     * @param f               the mapping function to apply to the contained value
     * @return the mapped value if this {@code Option} is {@code Some}, otherwise the result of the default supplier
     *
     * <p>Example:
     * <pre>{@code
     * Option<Integer> s = Option.Some(7);
     * String r = s.mapOrElse(() -> "none", n -> "Number: " + n); // -> "Number: 7"
     * }</pre>
     */
    default <U> U mapOrElse(Supplier<? extends U> defaultSupplier, Function<? super T, ? extends U> f) {
        return this.mapOrDefault(defaultSupplier, f);
    }

    /**
     * Returns {@code None} if this {@code Option} is {@code None}, otherwise returns this {@code Option}
     * if the predicate returns {@code true} for the contained value, or {@code None} if the predicate returns {@code false}.
     *
     * @param predicate the predicate to test the contained value against
     * @return this {@code Option} if the predicate returns {@code true}, otherwise {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<Integer> n = Option.Some(10);
     * Option<Integer> filtered = n.filter(x -> x > 5); // -> Some(10)
     * Option<Integer> out = n.filter(x -> x > 20); // -> None
     * }</pre>
     */
    default Option<T> filter(Predicate<? super T> predicate) {
        return this instanceof Some<T> s && predicate.test(s.value()) ? this : None();
    }

    /**
     * Returns {@code true} if this {@code Option} is {@code Some} and the contained value matches the predicate.
     *
     * @param predicate the predicate to test the contained value against
     * @return {@code true} if this {@code Option} is {@code Some} and the predicate returns {@code true}, {@code false} otherwise
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("hello");
     * boolean ok = s.isSomeAnd(str -> str.length() > 3); // -> true
     * }</pre>
     */
    default boolean isSomeAnd(Predicate<? super T> predicate) {
        return this instanceof Some<T> s && predicate.test(s.value());
    }

    /**
     * Returns {@code true} if this {@code Option} is {@code None} or the predicate returns {@code true}
     * for the contained value.
     *
     * @param predicate the predicate to test the contained value against
     * @return {@code true} if this {@code Option} is {@code None} or the predicate returns {@code true} for the contained value
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> none = Option.None();
     * assert none.isNoneOr(s -> s.length() > 0);
     *
     * Option<String> s = Option.Some("hi");
     * assert !s.isNoneOr(str -> str.length() > 3);
     * }</pre>
     */
    default boolean isNoneOr(Predicate<? super T> predicate) {
        if (this instanceof None<T>) {
            return true;
        }
        if (this instanceof Some<T> some) {
            return predicate.test(some.value());
        }
        return false; // unreachable due to sealed hierarchy
    }

    /**
     * Calls the provided action with the contained value if this {@code Option} is {@code Some},
     * then returns this {@code Option} unchanged.
     *
     * @param action the action to perform with the contained value
     * @return this {@code Option} unchanged
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("data");
     * Option<String> after = s.inspect(v -> System.out.println("Found: " + v));
     * // prints "Found: data", after -> Some("data")
     * }</pre>
     */
    default Option<T> inspect(Consumer<? super T> action) {
        if (this instanceof Some<T> s) action.accept(s.value());
        return this;
    }

    /**
     * Returns the contained value if this {@code Option} is {@code Some}.
     *
     * @return the contained value
     * @throws NoSuchElementException if this {@code Option} is {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("val");
     * String v = s.unwrap(); // -> "val"
     *
     * Option<String> none = Option.None();
     * // none.unwrap(); // throws NoSuchElementException
     * }</pre>
     */
    default T unwrap() {
        if (this instanceof Some<T> s) return s.value();
        throw new NoSuchElementException("called unwrap() on None");
    }

    /**
     * Returns the contained value if this {@code Option} is {@code Some}, otherwise returns the provided default.
     *
     * @param defaultValue the default value to return if this {@code Option} is {@code None}
     * @return the contained value if {@code Some}, otherwise the default value
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> some = Option.Some("hello");
     * String v = some.unwrapOr("default"); // -> "hello"
     *
     * Option<String> none = Option.None();
     * String d = none.unwrapOr("default"); // -> "default"
     * }</pre>
     */
    default T unwrapOr(T defaultValue) {
        return this instanceof Some<T> s ? s.value() : defaultValue;
    }

    /**
     * Returns the contained value if this {@code Option} is {@code Some}, otherwise computes a default
     * value using the supplier.
     *
     * @param supplier the supplier that provides the default value
     * @return the contained value if {@code Some}, otherwise the result of {@code supplier.get()}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> none = Option.None();
     * String v = none.unwrapOrElse(() -> "computed"); // -> "computed"
     *
     * Option<String> some = Option.Some("x");
     * String s = some.unwrapOrElse(() -> "y"); // -> "x"
     * }</pre>
     */
    default T unwrapOrElse(Supplier<? extends T> supplier) {
        return this instanceof Some<T> s ? s.value() : supplier.get();
    }

    /**
     * Alias for {@link #unwrapOrElse(java.util.function.Supplier)}.
     *
     * @param defaultSupplier the supplier that provides the default value
     * @return the contained value if {@code Some}, otherwise the result of {@code defaultSupplier.get()}
     *
     * <p>Example:
     * <pre>{@code
     * Option<Integer> none = Option.None();
     * Integer v = none.unwrapOrDefault(() -> 42); // -> 42
     * }</pre>
     */
    default T unwrapOrDefault(Supplier<? extends T> defaultSupplier) {
        return unwrapOrElse(defaultSupplier);
    }

    /**
     * Returns the contained value if this {@code Option} is {@code Some}.
     *
     * @param message the message to use in the exception if this {@code Option} is {@code None}
     * @return the contained value
     * @throws NoSuchElementException if this {@code Option} is {@code None}, with the provided message
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("data");
     * String v = s.expect("should have data"); // -> "data"
     *
     * Option<String> none = Option.None();
     * // none.expect("data missing"); // throws NoSuchElementException with message "data missing"
     * }</pre>
     */
    default T expect(String message) {
        if (this instanceof Some<T> s) return s.value();
        throw new NoSuchElementException(message);
    }

    /**
     * Returns the contained value if this {@code Option} is {@code Some}, or {@code null} if this {@code Option} is {@code None}.
     * This is an unchecked version of {@link #unwrap()}.
     *
     * @return the contained value if {@code Some}, otherwise {@code null}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> some = Option.Some("v");
     * String v = some.unwrapUnchecked(); // -> "v"
     *
     * Option<String> none = Option.None();
     * String n = none.unwrapUnchecked(); // -> null
     * }</pre>
     */
    default T unwrapUnchecked() {
        return this instanceof Some<T> s ? s.value() : null;
    }

    /**
     * Replaces the contained value with the given value, returning a new {@code Option}.
     * If this {@code Option} is {@code None}, returns {@code None}.
     *
     * @param value the new value to replace with
     * @return a {@code Some} containing the new value if this {@code Option} was {@code Some}, otherwise {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("old");
     * Option<String> r = s.replace("new"); // -> Some("new")
     * Option<String> none = Option.None();
     * Option<String> r2 = none.replace("new"); // -> None
     * }</pre>
     */
    default Option<T> replace(T value) {
        return this.isSome() ? Some(value) : None();
    }

    /**
     * Inserts the given value into this {@code Option}, always returning {@code Some}.
     * (This does not mutate; returns a newly constructed {@code Some}.)
     *
     * @param value the value to insert
     * @return a {@code Some} containing the given value
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> n = Option.None();
     * Option<String> inserted = n.insert("x"); // -> Some("x")
     * }</pre>
     */
    default Option<T> insert(T value) {
        return Some(value);
    }

    /**
     * Takes the value out of the {@code Option}, returning the {@code Option} itself.
     * (This method is functional/immutable so it simply returns {@code this} if {@code Some},
     * otherwise {@code None}.)
     *
     * @return this {@code Option}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("v");
     * Option<String> t = s.take(); // -> Some("v")
     * }</pre>
     */
    default Option<T> take() {
        return this.isSome() ? this : None();
    }

    /**
     * Takes the value out of the {@code Option} if the predicate returns {@code true},
     * otherwise returns {@code None}.
     *
     * @param predicate the predicate to test the contained value against
     * @return this {@code Option} if the predicate returns {@code true}, otherwise {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<Integer> s = Option.Some(10);
     * Option<Integer> t = s.takeIf(n -> n > 5); // -> Some(10)
     * Option<Integer> none = s.takeIf(n -> n > 20); // -> None
     * }</pre>
     */
    default Option<T> takeIf(Predicate<? super T> predicate) {
        return this instanceof Some<T> s && predicate.test(s.value()) ? this : None();
    }

    /**
     * Returns the contained value if this {@code Option} is {@code Some}, otherwise inserts the given value
     * and returns it. (Functional/immutable: returns the value, does not mutate.)
     *
     * @param value the value to insert if this {@code Option} is {@code None}
     * @return the contained value if {@code Some}, otherwise the given value
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("existing");
     * String v = s.getOrInsert("default"); // -> "existing"
     *
     * Option<String> none = Option.None();
     * String v2 = none.getOrInsert("default"); // -> "default"
     * }</pre>
     */
    default T getOrInsert(T value) {
        if (this.isSome()) return this.unwrap();
        return value;
    }

    /**
     * Returns the contained value if this {@code Option} is {@code Some}, otherwise computes a value
     * using the supplier and returns it.
     *
     * @param supplier the supplier that provides the default value
     * @return the contained value if {@code Some}, otherwise the result of {@code supplier.get()}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> none = Option.None();
     * String v = none.getOrInsertDefault(() -> "computed"); // -> "computed"
     * }</pre>
     */
    default T getOrInsertDefault(Supplier<? extends T> supplier) {
        return this.isSome() ? this.unwrap() : supplier.get();
    }

    /**
     * Alias for {@link #getOrInsertDefault(java.util.function.Supplier)}.
     *
     * @param supplier the supplier that provides the default value
     * @return the contained value if {@code Some}, otherwise the result of {@code supplier.get()}
     *
     * <p>Example:
     * <pre>{@code
     * Option<Integer> none = Option.None();
     * Integer v = none.getOrInsertWith(() -> 42); // -> 42
     * }</pre>
     */
    default T getOrInsertWith(Supplier<? extends T> supplier) {
        return getOrInsertDefault(supplier);
    }

    /**
     * Flattens a nested {@code Option<Option<T>>} into an {@code Option<T>}.
     *
     * @param <T>    the type of the value in the inner {@code Option}
     * @param nested the nested {@code Option} to flatten
     * @return the inner {@code Option} if the outer {@code Option} is {@code Some}, otherwise {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<Option<String>> nested = Option.Some(Option.Some("v"));
     * Option<String> flat = Option.flatten(nested); // -> Some("v")
     *
     * Option<Option<String>> nestedNone = Option.Some(Option.None());
     * Option<String> flatNone = Option.flatten(nestedNone); // -> None
     * }</pre>
     */
    static <T> Option<T> flatten(Option<? extends Option<T>> nested) {
        return nested instanceof Some<? extends Option<T>> s ? s.value() : None();
    }

    /**
     * Transposes an {@code Option} of {@code Result} into a {@code Result} of {@code Option}.
     *
     * <p>If this {@code Option} is {@code None}, returns {@code Result.ok(Option.None())}.
     * If it is {@code Some} containing a {@code Result}, the {@code Result} is mapped:
     * - {@code Ok(v)} -> {@code Ok(Some(v))}
     * - {@code Err(e)} -> {@code Err(e)}
     *
     * @param <E> the error type of the {@code Result}
     * @return a transposed {@code Result<Option<T>, E>}
     * @throws IllegalStateException if this {@code Option} contains something other than a {@code Result}
     *
     * <p>Example (assuming {@code Result} is defined elsewhere in your codebase):
     * <pre>{@code
     * Option<Result<String, Integer>> someOk = Option.Some(Result.ok("success"));
     * Result<Option<String>, Integer> transposed = someOk.transpose();
     * // transposed -> Ok(Some("success"))
     *
     * Option<Result<String, Integer>> none = Option.None();
     * Result<Option<String>, Integer> t2 = none.transpose();
     * // t2 -> Ok(None)
     * }</pre>
     */
    default <E> Result<Option<T>, E> transpose() {
        if (this instanceof None<T>) return Result.ok(Option.None());
        if (this.unwrap() instanceof Result<?, ?> r) {
            @SuppressWarnings("unchecked")
            Result<T, E> result = (Result<T, E>) r;
            return result.map(Option::Some);
        }
        throw new IllegalStateException("transpose requires Option<Result<T,E>>");
    }

    /**
     * Zips two {@code Option}s into a single {@code Option} containing a {@link Pair}.
     *
     * @param <A> the type of the first {@code Option}'s value
     * @param <B> the type of the second {@code Option}'s value
     * @param a   the first {@code Option}
     * @param b   the second {@code Option}
     * @return {@code Some} containing a {@link Pair} if both {@code Option}s are {@code Some}, otherwise {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> name = Option.Some("John");
     * Option<Integer> age = Option.Some(30);
     * Option<Pair<String, Integer>> zipped = Option.zip(name, age); // -> Some(Pair("John", 30))
     * }</pre>
     */
    static <A, B> Option<Pair<A, B>> zip(Option<A> a, Option<B> b) {
        if (a instanceof Some<A> sa && b instanceof Some<B> sb) return Some(new Pair<>(sa.value(), sb.value()));
        return None();
    }

    /**
     * Zips two {@code Option}s with a function, producing a new {@code Option} containing the result.
     *
     * @param <A> the type of the first {@code Option}'s value
     * @param <B> the type of the second {@code Option}'s value
     * @param <R> the type of the result
     * @param a   the first {@code Option}
     * @param b   the second {@code Option}
     * @param f   the function to combine the two values
     * @return {@code Some} containing the result of applying {@code f} if both {@code Option}s are {@code Some}, otherwise {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> first = Option.Some("John");
     * Option<String> last = Option.Some("Doe");
     * Option<String> full = Option.zipWith(first, last, (f, l) -> f + " " + l);
     * // full -> Some("John Doe")
     * }</pre>
     */
    static <A, B, R> Option<R> zipWith(Option<A> a, Option<B> b, BiFunction<? super A, ? super B, ? extends R> f) {
        if (a instanceof Some<A> sa && b instanceof Some<B> sb) return Some(f.apply(sa.value(), sb.value()));
        return None();
    }

    /**
     * Returns a {@link List} containing the value if present, otherwise an empty {@link List}.
     *
     * @return a list containing the value if this {@code Option} is {@code Some}, otherwise an empty {@link List}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> s = Option.Some("hello");
     * List<String> items = s.iter(); // -> List.of("hello")
     *
     * Option<String> none = Option.None();
     * List<String> empty = none.iter(); // -> List.of()
     * }</pre>
     */
    default List<T> iter() {
        return this instanceof Some<T> s ? List.of(s.value()) : List.of();
    }

    /**
     * Converts this {@code Option} into a {@code Result}, mapping {@code Some} to {@code Ok} and {@code None} to {@code Err}
     * with the given error.
     *
     * @param <E> the error type
     * @param err the error value to use if this {@code Option} is {@code None}
     * @return {@code Ok} containing the value if this {@code Option} is {@code Some}, otherwise {@code Err} containing the error
     *
     * <p>Example (assuming {@code Result} exists in your project):
     * <pre>{@code
     * Option<String> s = Option.Some("data");
     * Result<String, String> r = s.okOr("not found"); // -> Ok("data")
     *
     * Option<String> none = Option.None();
     * Result<String, String> r2 = none.okOr("not found"); // -> Err("not found")
     * }</pre>
     */
    default <E> Result<T, E> okOr(E err) {
        return this.isSome() ? Result.ok(this.unwrap()) : Result.err(err);
    }

    /**
     * Converts this {@code Option} into a {@code Result}, mapping {@code Some} to {@code Ok}
     * and {@code None} to {@code Err} with a computed error.
     *
     * @param <E>         the error type
     * @param errSupplier the supplier that provides the error value if this {@code Option} is {@code None}
     * @return {@code Ok} containing the value if this {@code Option} is {@code Some}, otherwise {@code Err} containing the computed error
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> none = Option.None();
     * Result<String, String> r = none.okOrElse(() -> "computed error"); // -> Err("computed error")
     * }</pre>
     */
    default <E> Result<T, E> okOrElse(Supplier<E> errSupplier) {
        return this.isSome() ? Result.ok(this.unwrap()) : Result.err(errSupplier.get());
    }

    /**
     * Returns a string representation of this {@code Option} for debugging purposes.
     *
     * @return {@code "Some(value)"} if this {@code Option} is {@code Some}, {@code "None"} if this {@code Option} is {@code None}
     *
     * <p>Example:
     * <pre>{@code
     * Option<Integer> s = Option.Some(3);
     * System.out.println(s.toDebugString()); // -> "Some(3)"
     * }</pre>
     */
    default String toDebugString() {
        return this.isSome() ? "Some(" + this.unwrap() + ")" : "None";
    }

    /**
     * Unzips an {@code Option} containing a {@link Pair} into a {@link Pair} of {@code Option}s.
     *
     * @param <A>     the type of the first value in the {@link Pair}
     * @param <B>     the type of the second value in the {@link Pair}
     * @param pairOpt the {@code Option} containing a {@link Pair}
     * @return a {@link Pair} of {@code Option}s, where each {@code Option} contains the corresponding value from the {@link Pair};
     *         if {@code pairOpt} is {@code None}, returns {@code Pair(None, None)}
     *
     * <p>Example:
     * <pre>{@code
     * Option<Pair<String, Integer>> zipped = Option.Some(new Pair<>("John", 30));
     * Pair<Option<String>, Option<Integer>> unzipped = Option.unzip(zipped);
     * // unzipped -> Pair(Some("John"), Some(30))
     * }</pre>
     */
    static <A, B> Pair<Option<A>, Option<B>> unzip(Option<Pair<A, B>> pairOpt) {
        if (pairOpt instanceof Some<Pair<A, B>> s) {
            return new Pair<>(Option.Some(s.value().first()), Option.Some(s.value().second()));
        }
        return new Pair<>(Option.None(), Option.None());
    }

    /**
     * Converts this {@code Option} to a {@link java.util.Optional}.
     *
     * @return an {@link java.util.Optional} containing the value if this {@code Option} is {@code Some}, otherwise {@link java.util.Optional#empty()}
     *
     * <p>Example:
     * <pre>{@code
     * Option<String> some = Option.Some("value");
     * Optional<String> j = some.toOptional(); // -> Optional["value"]
     * }</pre>
     */
    default java.util.Optional<T> toOptional() {
        return this instanceof Some<T> s
                ? java.util.Optional.of(s.value())
                : java.util.Optional.empty();
    }

    /**
     * A simple {@code Pair} record for holding two values together.
     *
     * @param <A>    the type of the first value
     * @param <B>    the type of the second value
     * @param first  the first value
     * @param second the second value
     *
     * <p>Example:
     * <pre>{@code
     * Pair<String, Integer> p = new Pair<>("a", 1);
     * System.out.println(p.first()); // -> "a"
     * }</pre>
     */
    record Pair<A, B>(A first, B second) {
    }
}
