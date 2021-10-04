package com.example.quote.model;

import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class QuoteCost {
    private double value;
    private double valueWithDiscount;

    public QuoteCost(double value) {
        this.value = value;
        this.valueWithDiscount = value;
    }

    public QuoteCost(double value, double valueWithDiscount) {
        this.value = value;
        this.valueWithDiscount = valueWithDiscount;
    }
}
