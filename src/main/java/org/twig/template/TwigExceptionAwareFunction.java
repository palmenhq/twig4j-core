package org.twig.template;

import org.twig.exception.TwigException;

@FunctionalInterface
public interface TwigExceptionAwareFunction<T, R> {
    R apply(T t) throws TwigException;
}