package br.com.felipe.modernjava.reactive;

import java.util.concurrent.Flow;

/**
 * A Processor is both a Subscriber and a Publisher . In fact, it’s intended to subscribe to a Publisher
 * and republish the data that it receives after transforming that data.
 * A processor transforming a TempInfo into another TempInfo
 * */
public class TempProcessor implements Flow.Processor<TempInfo, TempInfo> {
    private Flow.Subscriber<? super TempInfo> subscriber;

    @Override
    public void subscribe(Flow.Subscriber<? super TempInfo> subscriber) {
        this.subscriber = subscriber;
    }

    /**
     * Republishes the TempInfo after converting the temperature to Celsius
     * */
    @Override
    public void onNext(TempInfo temp) {
        subscriber.onNext(new TempInfo(temp.getTown(), (temp.getTemp() - 32) * 5 / 9));
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscriber.onSubscribe(subscription);
    }

    @Override
    public void onError(Throwable throwable) {
        subscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        subscriber.onComplete();
    }
}
