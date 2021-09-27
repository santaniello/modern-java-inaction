package br.com.felipe.modernjava.lambda.refactoring.designpatterns.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Feed implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    public void registerObserver(Observer o) {
        this.observers.add(o);
    }


    public void notifyObservers(String tweet) {
        observers.forEach(o -> o.notify(tweet));
    }
}
