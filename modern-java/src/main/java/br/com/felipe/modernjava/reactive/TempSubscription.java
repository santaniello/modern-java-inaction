package br.com.felipe.modernjava.reactive;

import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

@AllArgsConstructor
public class TempSubscription implements Flow.Subscription {

    private final Flow.Subscriber<? super TempInfo> subscriber;
    private final String town;
    private static final ExecutorService executor =  Executors.newSingleThreadExecutor();


    @Override
    public void request(long n) {
        /**Sends the next elements to the subscriber from a different thread*/
        executor.submit( () -> {
            /** Loops once per request made by the Subscriber **/
            for (long i = 0L; i < n; i++) {
                try {
                    subscriber.onNext(TempInfo.fetch(town));
                } catch (Exception e) {
                    /** In case of a failure while fetching
                     the temperature propagates the error to the Subscriber */
                    subscriber.onError(e);
                    break;
                }
            }
        });
    }

    /**
     * If the subscription is
     * canceled, send a completion (onComplete)
     * signal to the Subscriber.
     * */
    @Override
    public void cancel() {
        subscriber.onComplete();
    }
}
