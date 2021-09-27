package br.com.felipe.modernjava.asyncchronousapis;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DrawAsyncTest {

    @Test
    void test() throws ExecutionException, InterruptedException {
        Future<Integer> f = DrawAsync.f(2,1000);
        Future<Integer> g = DrawAsync.g(2,0);

        System.out.println(g.get());
        System.out.println(f.get());
        Assertions.assertEquals(7, f.get()+ g.get());
    }
}
