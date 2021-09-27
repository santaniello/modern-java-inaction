package br.com.felipe.modernjava.dsl;

import br.com.felipe.modernjava.dsl.models.Order;

import java.util.function.DoubleUnaryOperator;

public class TaxCalculator {

    private DoubleUnaryOperator taxFunction = d -> d;

    public TaxCalculator with(DoubleUnaryOperator f) {
        taxFunction = taxFunction.andThen(f);
        return this;
    }

    public double calculate(Order order) {
        return taxFunction.applyAsDouble(order.getValue());
    }



}
