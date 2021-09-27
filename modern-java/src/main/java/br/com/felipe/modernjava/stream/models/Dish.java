package br.com.felipe.modernjava.stream.models;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public class Dish {
    @Getter
    private final String name;
    @Getter
    private final boolean vegetarian;
    @Getter
    private final int calories;
    @Getter
    private final Type type;

    private CaloricLevel caloricLevel;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public CaloricLevel getCaloricLevel(){
        if (this.getCalories() <= 400) return CaloricLevel.DIET;
        else if (this.getCalories() <= 700) return CaloricLevel.NORMAL;
        else return CaloricLevel.FAT;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                '}';
    }
}
