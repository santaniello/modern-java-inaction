package br.com.felipe.modernjava.reactive;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Random;

@ToString
@AllArgsConstructor
public class TempInfo {
    @ToString.Exclude
    public static final Random random = new Random();
    @Getter
    private final String town;
    @Getter
    private final int temp;

    public static TempInfo fetch(String town){
        if(random.nextInt(10) == 0)
            throw new RuntimeException("Error !");

        return new TempInfo(town, random.nextInt(100));
    }
}
