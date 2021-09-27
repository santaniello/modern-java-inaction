package br.com.felipe.modernjava.lambda.refactoring.designpatterns.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StrategyTest {
    @Test
    void must_use_validator_strategy_in_a_normal_way(){
        Validator numericValidator = new Validator(new IsNumeric());
        Assertions.assertFalse(numericValidator.validate("aaaa"));
        Validator lowerCaseValidator = new Validator(new IsAllowerCase());
        Assertions.assertTrue(lowerCaseValidator.validate("bbbb"));
    }

    @Test
    void must_use_validator_strategy_with_lambdas(){
        ValidatorUsingLambda numericValidator = new ValidatorUsingLambda((String s) -> s.matches("\\d+"));
        Assertions.assertFalse(numericValidator.validate("aaaa"));
        ValidatorUsingLambda lowerCaseValidator = new ValidatorUsingLambda((String s) -> s.matches("[a-z]+"));
        Assertions.assertTrue(lowerCaseValidator.validate("bbbb"));
    }

}
