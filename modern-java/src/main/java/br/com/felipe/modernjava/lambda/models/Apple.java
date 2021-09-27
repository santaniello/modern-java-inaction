package br.com.felipe.modernjava.lambda.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Apple {
    private int weight;
    private Color color;

    public Apple(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "weight='" + weight + '\'' +
                '}';
    }
}
