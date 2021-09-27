package br.com.felipe.modernjava.asyncchronousapis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static br.com.felipe.modernjava.asyncchronousapis.DrawSync.*;

public class DrawSyncTest {
    @Test
    void test(){
        Assertions.assertEquals(4, f(2));
        Assertions.assertEquals(3, g(2));
    }
}
