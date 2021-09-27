package br.com.felipe.modernjava.lambda.refactoring.designpatterns.strategy;

import java.util.function.Predicate;

public class ValidatorUsingLambda {
    private final Predicate<String> strategy;
    public ValidatorUsingLambda(Predicate<String> v) {
        this.strategy = v;
    }
    public Boolean validate(String s) {
        return strategy.test(s);
    }
}
