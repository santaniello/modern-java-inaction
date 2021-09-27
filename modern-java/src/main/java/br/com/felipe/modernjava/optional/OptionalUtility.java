package br.com.felipe.modernjava.optional;

import java.util.Optional;

import static java.util.Optional.*;

public class OptionalUtility {
    public static Optional<Integer> stringToInt(String value){
        try {
            return of(Integer.parseInt(value));
        }catch (NumberFormatException nf){
            return Optional.empty();
        }
    }
}
