package br.com.felipe.modernjava.lambda.refactoring.designpatterns.observer;

import java.util.function.Consumer;

public interface Subject {
    void registerObserver(Observer o);
    void notifyObservers(String tweet);
}
