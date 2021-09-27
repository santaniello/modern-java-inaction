package br.com.felipe.modernjava.lambda.refactoring.designpatterns.templatemethod;

import org.junit.jupiter.api.Test;

public class TemplateMethodTest {
    @Test
    void must_use_template_method_with_lambda(){
        new OnlineBankingWithLambda().processCustomer(1337, (Customer c) ->
                System.out.println("Hello " + c.getId()));

    }
}
