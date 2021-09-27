package br.com.felipe.modernjava.lambda.refactoring.designpatterns.strategy;

public class IsAllowerCase implements ValidationStrategy {
    @Override
    public boolean execute(String s){
        return s.matches("[a-z]+");
    }
}
