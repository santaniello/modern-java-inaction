package br.com.felipe.modernjava.lambda.interfaces;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * a functional interface is an interface that specifies exactly one abstract
 * method.
 *
 * An interface is still a functional interface if it has many default methods
 * as long as it specifies only one abstract method.
 *
 * Lambdas use this kind of interface to work
 * */
@FunctionalInterface
public interface BufferedReaderProcessor {
    String process(BufferedReader b) throws IOException;
}
