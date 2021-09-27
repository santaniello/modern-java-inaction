package br.com.felipe.modernjava.optional.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
public class Person {
    /**
     * A person may not own a car, so you declare this field Optional.
     * OBS: Optional class doesn’t implement the Serializable interface
     * For this reason, using Optionals in your domain model could break
     * applications with tools or frameworks that require a serializable
     * model to work.
     * */
    //private Optional<Car> car;

    private Car car;


    /**
     * If you can not use the Optional as an class atribute because Serializable interface,
     * you can create a getter than returns an optional car !
     * */
    public Optional<Car> getCar(){
        return Optional.ofNullable(this.car);
    }


}
