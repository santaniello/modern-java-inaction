package br.com.felipe.modernjava.lambda;

import br.com.felipe.modernjava.lambda.interfaces.ApplePredicate;
import br.com.felipe.modernjava.lambda.models.Apple;

public class AppleHeavyWeightPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return apple.getWeight() > 150;
    }
}
