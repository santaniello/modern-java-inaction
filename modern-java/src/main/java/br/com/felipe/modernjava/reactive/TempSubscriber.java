package br.com.felipe.modernjava.reactive;

import java.util.concurrent.Flow;

public class TempSubscriber implements Flow.Subscriber<TempInfo> {

    private Flow.Subscription subscription;

    /**
     * Stores the subscription and sends a  first request
     * */
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request( 1 );
    }

    /**
     * Prints the received temperature and requests a further one
     * */
    @Override
    public void onNext(TempInfo tempInfo) {
        System.out.println( tempInfo );
        subscription.request( 1 );
    }

    /**
     * Prints the error message in case of an error
     * */
    @Override
    public void onError(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Done!");
    }
}
