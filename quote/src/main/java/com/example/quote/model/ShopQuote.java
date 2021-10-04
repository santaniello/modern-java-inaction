package com.example.quote.model;

import lombok.Getter;

@Getter
public class ShopQuote {
    private String name;
    private Product product;
    private QuoteCost cost;

    public ShopQuote(String name, Product product, QuoteCost cost) {
        this.name = name;
        this.product = product;
        this.cost = cost;
    }

    public ShopQuote(Product product, ShopPrice shopPrice) {
        this.name = shopPrice.getShopName();
        this.product = product;
        this.cost = new QuoteCost(shopPrice.getValue());
    }

    public ShopQuote applyDiscount(Discount.Code discountCode){
        double discount = Discount.applyDiscount(this, discountCode);
        return new ShopQuote(this.name, this.product, new QuoteCost(this.cost.getValue(), discount));
    }
}
