package br.com.felipe.modernjava.asyncchronousapis;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class DrawAsync {
    public static Future<Integer> f(int x, int threadSleep) {
        return new CompletableFuture<Integer>().completeAsync(() -> calc(x * 2, threadSleep));
    }
    public static Future<Integer> g(int x, int threadSleep) {
        return new CompletableFuture<Integer>().completeAsync(() -> calc(x + 1, threadSleep));
    }

    private static int calc(int x, int threadSleep){
        try {
            Thread.sleep(threadSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  Integer.valueOf(x);
    }

}
