package br.com.felipe.modernjava.asyncchronousapis;

import java.util.function.IntConsumer;

public class DrawReactive {
    public static void f(int x, IntConsumer dealWithResult) {
        dealWithResult.accept(DrawSync.f(x));
    }

    public static void g(int x, IntConsumer dealWithResult) {
        dealWithResult.accept(DrawSync.g(x));
    }

}
