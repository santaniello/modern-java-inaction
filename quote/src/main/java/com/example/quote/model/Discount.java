package com.example.quote.model;

import lombok.Getter;

import static com.example.quote.util.QuoteUtils.delay;
import static com.example.quote.util.QuoteUtils.format;

public class Discount {

    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        @Getter
        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static double applyDiscount(ShopQuote quote, Code code) {
        return Discount.apply(quote.getCost().getValue(), code);
    }

    private static double apply(double price, Code code) {
        delay();
        return format(price * (100 - code.percentage) / 100);
    }
}
