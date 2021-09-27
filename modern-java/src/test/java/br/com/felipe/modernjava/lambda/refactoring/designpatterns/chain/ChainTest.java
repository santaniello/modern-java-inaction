package br.com.felipe.modernjava.lambda.refactoring.designpatterns.chain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ChainTest {
    @Test
    void must_use_chain_without_in_a_normal_way(){
        ProcessingObject<String> p1 = new HeaderTextProcessing();
        ProcessingObject<String> p2 = new SpellCheckerProcessing();
        p1.setSuccessor(p2);
        String result = p1.handle("Aren't labdas really sexy?!!");
        Assertions.assertEquals("From Raoul, Mario and Alan: Aren't lambdas really sexy?!!", result);
    }

    @Test
    void must_use_chain_with_lambda(){
        UnaryOperator<String> headerProcessing = (String text) -> "From Raoul, Mario and Alan: " + text;
        UnaryOperator<String> spellCheckerProcessing =   (String text) -> text.replaceAll("labda", "lambda");
        Function<String, String> pipeline =  headerProcessing.andThen(spellCheckerProcessing);
        String result = pipeline.apply("Aren't labdas really sexy?!!");
        Assertions.assertEquals("From Raoul, Mario and Alan: Aren't lambdas really sexy?!!", result);
    }
}
