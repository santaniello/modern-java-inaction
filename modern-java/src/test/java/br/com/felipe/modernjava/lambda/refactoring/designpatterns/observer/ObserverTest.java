package br.com.felipe.modernjava.lambda.refactoring.designpatterns.observer;

import org.junit.jupiter.api.Test;

public class ObserverTest {
    @Test
    void must_use_observer_pattern_in_normal_way(){
        Feed f = new Feed();
        f.registerObserver(new NYTimes());
        f.registerObserver(new Guardian());
        f.notifyObservers("The queen said her favourite book is Modern Java in Action!");
    }

    @Test
    void must_use_observer_pattern_with_lambda(){
        Feed f = new Feed();
        f.registerObserver((String tweet) -> {
            if(tweet != null && tweet.contains("money")){
                System.out.println("Breaking news in NY! " + tweet);
            }
        });
        f.registerObserver((String tweet) -> {
            if(tweet != null && tweet.contains("queen")){
                System.out.println("Yet more news from London... " + tweet);
            }
        });
    }
}
