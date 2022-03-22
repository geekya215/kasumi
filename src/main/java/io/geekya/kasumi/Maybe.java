package io.geekya.kasumi;


import io.geekya.kasumi.typeclass.Applicative;
import io.geekya.kasumi.typeclass.Monad;

import java.util.Objects;
import java.util.function.Function;

public class Maybe<A> implements Monad<A, Maybe<?>> {
    private Maybe() {
    }

    @SuppressWarnings("unchecked")
    public static <A> Maybe<A> nothing() {
        return (Maybe<A>) Nothing.INSTANCE;
    }

    public static <A> Maybe<A> just(A a) {
        if (a == null) {
            throw new NullPointerException("Just can not be created with null");
        }
        return new Just<>(a);
    }

    public static <A> Maybe<A> maybe(A a) {
        return a == null ? nothing() : just(a);
    }

    public final Boolean isNothing() {
        return this instanceof Maybe.Nothing<A>;
    }

    public A fromJust() {
        if (isNothing()) {
            throw new NullPointerException("Nothing can not extract element");
        } else {
            return fromJust();
        }
    }

    @Override
    public final <B> Maybe<B> pure(B b) {
        return just(b);
    }

    @Override
    public <B> Maybe<B> map(Function<? super A, ? extends B> f) {
        return isNothing() ? nothing() : just(f.apply(fromJust()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <B> Maybe<B> fmap(Applicative<Function<? super A, ? extends B>, Maybe<?>> af) {
        return isNothing() ? nothing() : (Maybe<B>) af.map(f -> f.apply(fromJust()));
    }

    @Override
    public <B> Maybe<B> bind(Function<? super A, ? extends Monad<B, Maybe<?>>> mf) {
        return isNothing() ? nothing() : (Maybe<B>) mf.apply(fromJust());
    }

    private static final class Nothing<A> extends Maybe<A> {
        private static final Nothing<?> INSTANCE = new Nothing<>();

        private Nothing() {
        }

        @Override
        public String toString() {
            return "Nothing";
        }
    }

    private static final class Just<A> extends Maybe<A> {
        private final A a;

        private Just(A a) {
            this.a = a;
        }

        @Override
        public A fromJust() {
            return a;
        }

        @Override
        public String toString() {
            return "Just " + a;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Maybe.Just<?> && Objects.equals(this.a, ((Just<?>) obj).a);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(a);
        }
    }
}
