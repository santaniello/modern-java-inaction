package br.com.felipe.modernjava.lambda.refactoring.designpatterns.chain;

public class SpellCheckerProcessing extends ProcessingObject<String>{
    @Override
    protected String handleWork(String text) {
        return text.replaceAll("labda", "lambda");    }
}
