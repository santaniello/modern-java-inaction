package br.com.felipe.modernjava.lambda.refactoring.designpatterns.chain;

public class HeaderTextProcessing extends ProcessingObject<String>{
    @Override
    protected String handleWork(String text) {
        return "From Raoul, Mario and Alan: " + text;
    }
}
