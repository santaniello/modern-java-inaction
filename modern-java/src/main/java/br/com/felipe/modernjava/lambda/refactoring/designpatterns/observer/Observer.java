package br.com.felipe.modernjava.lambda.refactoring.designpatterns.observer;

@FunctionalInterface
public interface Observer {
    void notify(String tweet);
}
