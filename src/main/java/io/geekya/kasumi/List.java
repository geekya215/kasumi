package io.geekya.kasumi;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public sealed interface List<T> {

    record Nil<T>() implements List<T> {
    }

    record Cons<T>(T head, List<T> tail) implements List<T> {
    }

    @SafeVarargs
    static <T> List<T> of(T... xs) {
        return switch (xs.length) {
            case 0 -> new Nil<>();
            case 1 -> singleton(xs[0]);
            default -> new Cons<>(xs[0], of(Arrays.copyOfRange(xs, 1, xs.length)));
        };
    }

    static <T> List<T> concat(List<T> left, List<T> right) {
        return switch (left) {
            case Nil nil -> right;
            case Cons<T> cons -> new Cons<>(cons.head, concat(cons.tail, right));
        };
    }

    static <T> T head(List<T> list) {
        return switch (list) {
            case Nil nil -> throw new IllegalArgumentException("head called on empty list");
            case Cons<T> cons -> cons.head;
        };
    }

    static <T> List<T> tail(List<T> list) {
        return switch (list) {
            case Nil nil -> throw new IllegalArgumentException("tail called on empty list");
            case Cons<T> cons -> cons.tail;
        };
    }

    static <T> List<T> singleton(T x) {
        return new Cons<>(x, new Nil<>());
    }

    static <T> List<T> empty() {
        return new Nil<>();
    }

    static <T, U> List<U> map(Function<? super T, ? extends U> f, List<T> list) {
        return switch (list) {
            case Nil nil -> new Nil<>();
            case Cons<T> cons -> new Cons<>(f.apply(cons.head), map(f, cons.tail));
        };
    }

    static <T, U> List<U> flatMap(Function<? super T, ? extends List<? extends U>> f, List<T> list) {
        return switch (list) {
            case Nil nil -> new Nil<>();
            case Cons<T> cons -> {
                @SuppressWarnings("unchecked")
                List<U> result = (List<U>) f.apply(cons.head);
                yield concat(result, flatMap(f, cons.tail));
            }
        };
    }

    static <T> List<T> filter(Predicate<? super T> p, List<T> list) {
        return switch (list) {
            case Nil nil -> new Nil<>();
            case Cons<T> cons -> {
                if (p.test(cons.head)) {
                    yield new Cons<>(cons.head, filter(p, cons.tail));
                } else {
                    yield filter(p, cons.tail);
                }
            }
        };
    }

    static <T> T foldl(Function<? super T, Function<? super T, ? extends T>> f, T z, List<T> list) {
        return switch (list) {
            case Nil nil -> z;
            case Cons<T> cons -> foldl(f, f.apply(z).apply(cons.head), cons.tail);
        };
    }

    static <T> T foldl1(Function<? super T, Function<? super T, ? extends T>> f, List<T> list) {
        return foldl(f, head(list), tail(list));
    }

    static <T> T foldr(Function<? super T, Function<? super T, ? extends T>> f, T z, List<T> list) {
        return switch (list) {
            case Nil nil -> z;
            case Cons<T> cons -> f.apply(cons.head).apply(foldr(f, z, cons.tail));
        };
    }

    static <T> T foldr1(Function<? super T, Function<? super T, ? extends T>> f, List<T> list) {
        return foldr(f, head(list), tail(list));
    }
}
