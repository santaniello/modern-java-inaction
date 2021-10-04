package com.example.quote.repository;

import com.example.quote.model.ShopPrice;
import com.example.quote.model.Product;
import com.example.quote.util.QuoteUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

import static com.example.quote.util.QuoteUtils.format;
import static java.util.List.*;

@Repository
public class ShopRepository {
    private final Random random = new Random();

    public ShopPrice getPrice(String shopName, Product product){
        double price = generateRandomValue(product.getName());
        return new ShopPrice(shopName, price);
    }

    public List<String> findAll(){
        return of("BestPrice","LetsSaveBig","MyFavoriteShop","BuyItAll","ShopEasy");
    }

    private double generateRandomValue(String productName){
        QuoteUtils.delay();
        return format(random.nextDouble() * productName.charAt(0) + productName.charAt(1));
    }
}
