package br.com.felipe.modernjava.optional.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;


@AllArgsConstructor
public class Car {
    private Insurance insurance;

    public Optional<Insurance> getInsurance() {
        return Optional.ofNullable(insurance);
    }
}
