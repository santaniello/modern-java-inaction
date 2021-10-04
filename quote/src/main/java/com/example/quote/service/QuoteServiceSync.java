package com.example.quote.service;

import com.example.quote.model.*;
import com.example.quote.repository.DiscountRepository;
import com.example.quote.repository.ProductRepository;
import com.example.quote.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class QuoteServiceSync {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountService discountService;

    public Quote quote(int id){
        Instant start = Instant.now();
        Product product = productRepository.findById(id);
        List<ShopQuote> shopQuotes = shopRepository.findAll()
                .stream()
                .map(shopName -> shopRepository.getPrice(shopName, product))
                .map(price-> new ShopQuote(product,price))
                .map(discountService::applyDiscount)
                .collect(toList());
        Instant end = Instant.now();
        return new Quote(shopQuotes, new QuoteStatistic(Duration.between(start,end)));
    }

    public Quote quoteInParallel(int id){
        Instant start = Instant.now();
        Product product = productRepository.findById(id);
        List<ShopQuote> shopQuotes = shopRepository.findAll()
                .parallelStream()
                .map(shopName -> shopRepository.getPrice(shopName, product))
                .map(price-> new ShopQuote(product,price))
                .map(discountService::applyDiscount)
                .collect(toList());
        Instant end = Instant.now();
        return new Quote(shopQuotes, new QuoteStatistic(Duration.between(start,end)));
    }
}
