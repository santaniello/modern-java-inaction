package br.com.felipe.modernjava.asyncchronousapis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class DrawReactiveTest {
    @Test
    void test() throws ExecutionException, InterruptedException {
        Result result = new Result();
        int x = 2;
        DrawReactive.f(x, y ->{
            result.setLeft(y);
            System.out.println(result.getLeft() + result.getRight());
        });

        DrawReactive.g(x, z ->{
            result.setRight(z);
            System.out.println(result.getLeft() + result.getRight());
        });
    }
}
