package com.example.quote.model;

import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class ShopPrice {
    private String shopName;
    private double value;
}
