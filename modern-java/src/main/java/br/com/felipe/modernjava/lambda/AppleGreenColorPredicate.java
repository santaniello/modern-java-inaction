package br.com.felipe.modernjava.lambda;

import br.com.felipe.modernjava.lambda.interfaces.ApplePredicate;
import br.com.felipe.modernjava.lambda.models.Apple;

import static br.com.felipe.modernjava.lambda.models.Color.GREEN;

public class AppleGreenColorPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return GREEN.equals(apple.getColor());
    }
}
