package br.com.felipe.modernjava.lambda.interfaces;

import br.com.felipe.modernjava.lambda.models.Apple;

/**
 * a functional interface is an interface that specifies exactly one abstract
 * method.
 *
 * An interface is still a functional interface if it has many default methods
 * as long as it specifies only one abstract method.
 *
 * Lambdas use this kind of interface to work
 *
 * This interface could be replaced By the Predicate interface from java 8 inside de java.util.function
 *
 * */

@FunctionalInterface
public interface ApplePredicate {
    boolean test (Apple apple);
}
