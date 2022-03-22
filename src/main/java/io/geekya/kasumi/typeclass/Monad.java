package io.geekya.kasumi.typeclass;

import java.util.function.Function;

public interface Monad<A, M extends Monad<?, M>> extends Applicative<A, M> {
    <B> Monad<B, M> pure(B b);

    <B> Monad<B, M> bind(Function<? super A, ? extends Monad<B, M>> f);
}
