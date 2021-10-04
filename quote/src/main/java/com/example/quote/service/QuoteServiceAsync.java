package com.example.quote.service;

import com.example.quote.model.*;
import com.example.quote.repository.ProductRepository;
import com.example.quote.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.example.quote.model.ExchangeService.*;
import static com.example.quote.model.ExchangeService.Money.*;
import static java.util.concurrent.CompletableFuture.*;
import static java.util.stream.Collectors.toList;

@Service
public class QuoteServiceAsync {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountService discountService;

    /**
     * The thenCompose() method is similar to thenApply() in that both return a new Completion Stage.
     * However, thenCompose() uses the previous stage as the argument. It will flatten and return a Future
     * with the result directly, rather than a nested future as we observed in thenApply():
     * */
    public Quote quote(int id){
        Instant start = Instant.now();
        Product product = productRepository.findById(id);

        List<CompletableFuture<ShopQuote>> futuresQuotes = shopRepository.findAll()
                .stream()
                .map(shop -> getPriceAsync(shop,product))
                .map(futurePrice -> futurePrice.thenApply(price -> new ShopQuote(product, price)))
                .map(futureQuote -> futureQuote.thenCompose(this::applyDiscountAsync))
                .collect(toList());

        List<ShopQuote> quotes = futuresQuotes.stream().map(CompletableFuture::join).collect(toList());

        Instant end = Instant.now();
        return new Quote(quotes, new QuoteStatistic(Duration.between(start,end)));
    }

    /**
     * You can asynchronously ask the shop the price of a given product and separately
     * retrieve, from a remote exchange-rate service, the current exchange rate between € and
     * $. After both requests have completed, you can combine the results by multiplying the
     * price by the exchange rate.
     * */
    public Future<Double> priceinUSD(int id){
        Product product = productRepository.findById(id);
        String shop = shopRepository.findAll().stream().findAny().get();
        return  getPriceAsync(shop, product) // Get price in an asynchronous way
                    .thenCombine(getRateAsync(EUR, USD), // At same time that you consult the service to get the price, you consult the ExchangeService to get the rate (AT SAME TIME).
                                 this::combinePriceAndRate); // At the end, you combine the both consults in one result (price * rate)
    }

    public double combinePriceAndRate(ShopPrice price, double rate){
        return price.getValue() * rate;
    }

    public CompletableFuture<Double> getRateAsync(Money source, Money destination){
        return supplyAsync(() -> getRate(source,destination));
    }

    public CompletableFuture<ShopPrice> getPriceAsync(String shop, Product product){
        return  supplyAsync(() -> shopRepository.getPrice(shop,product));
    }

    public CompletableFuture<ShopQuote> applyDiscountAsync(ShopQuote quote){
        return  supplyAsync(() -> discountService.applyDiscount(quote));
    }
}
