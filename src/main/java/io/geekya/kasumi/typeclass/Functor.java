package io.geekya.kasumi.typeclass;

import java.util.function.Function;

@FunctionalInterface
public interface Functor<A, F extends Functor<?, F>> {
    <B> Functor<B, F> map(Functor<A, F> a, Function<? super A, ? extends B> f);
}
