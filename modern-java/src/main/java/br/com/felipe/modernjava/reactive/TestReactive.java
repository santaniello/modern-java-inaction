package br.com.felipe.modernjava.reactive;

import java.util.concurrent.Flow;

public class TestReactive {
    public static void main(String[] args) {
        /**
         * Creates a new Publisher of temperatures
         * in New York and subscribes the
         * TempSubscriber to it
         * */
        getTemperatures("New York").subscribe(new TempSubscriber());
    }

    /**
     * Returns a Publisher that sends a TempSubscription
     * to the Subscriber that subscribes to it
     * */
    private static Flow.Publisher<TempInfo> getTemperatures(String town){
        return subscriber -> subscriber.onSubscribe(new TempSubscription(subscriber, town));
    }
}
