package com.example.quote.service;

import com.example.quote.model.*;
import com.example.quote.repository.ProductRepository;
import com.example.quote.repository.ShopRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.*;

import static com.example.quote.model.ExchangeService.Money.EUR;
import static com.example.quote.model.ExchangeService.Money.USD;
import static com.example.quote.model.ExchangeService.getRate;
import static com.example.quote.repository.ProductRepository.ID_IPHONE;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

@SpringBootTest
public class QuoteServiceAsyncTest {

    @Autowired
    private QuoteServiceSync serviceSync;

    @Autowired
    private QuoteServiceAsync serviceAsync;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountService discountService;

    @Test
    void should_quote_with_sucess(){
        Quote quoteSync  = serviceSync.quote(ID_IPHONE);
        Quote quoteAsync = serviceAsync.quote(ID_IPHONE);

        Assertions.assertEquals(10,quoteSync.getStatistic().getStatisticInSeconds());
        Assertions.assertEquals(2, quoteAsync.getStatistic().getStatisticInSeconds());
        Assertions.assertTrue(quoteAsync.getStatistic().getStatisticInSeconds() < quoteSync.getStatistic().getStatisticInSeconds());
    }

    @Test
    void should_get_price_in_USD(){
        Quote quoteSync  = serviceSync.quote(ID_IPHONE);
        Quote quoteAsync = serviceAsync.quote(ID_IPHONE);

        Assertions.assertEquals(10,quoteSync.getStatistic().getStatisticInSeconds());
        Assertions.assertEquals(2, quoteAsync.getStatistic().getStatisticInSeconds());
        Assertions.assertTrue(quoteAsync.getStatistic().getStatisticInSeconds() < quoteSync.getStatistic().getStatisticInSeconds());
    }

    @Test
    void should_throw_exception_in_completable_future()  {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
            try{
                List<String> list = null;
                list.get(0);
                return "ddd";
            }catch(Exception e){
                throw new CompletionException(e);
            }
        });

        Assertions.assertThrows(ExecutionException.class, () -> {
            future.get();
        });
    }


    @Test
    void should_thow_exception_when_the_service_price_called()  {
        Product product = productRepository.findById(ID_IPHONE);

        List<CompletableFuture<ShopQuote>> futuresQuotes = shopRepository.findAll()
                .stream()
                .map(shop -> getPriceWithException(shop))
                .map(futurePrice -> futurePrice.thenApply(price -> new ShopQuote(product, price)))
                .map(futureQuote -> futureQuote.thenCompose(this::applyDiscountAsync))
                .collect(toList());

        Assertions.assertThrows(CompletionException.class, () -> {
            List<ShopQuote> quotes = futuresQuotes.stream().map(CompletableFuture::join).collect(toList());
        });

    }

    @Test
    void should_be_able_to_recover_from_an_exception_using_the_handle_method()  {
        Product product = productRepository.findById(ID_IPHONE);

        List<CompletableFuture<ShopQuote>> futuresQuotes = shopRepository.findAll()
                .stream()
                .map(shop -> getPriceUsingHandleMethodWhenExceptionOccur(shop))
                .map(futurePrice -> futurePrice.thenApply(price -> new ShopQuote(product, price)))
                .map(futureQuote -> futureQuote.thenCompose(this::applyDiscountAsync))
                .collect(toList());

        List<ShopQuote> quotes = futuresQuotes.stream().map(CompletableFuture::join).collect(toList());
        Assertions.assertNotNull(quotes);
        Assertions.assertTrue(quotes.size() > 0);
        Assertions.assertEquals(50,quotes.get(0).getCost().getValue());
    }

    @Test
    void should_be_able_to_recover_from_an_exception_using_the_exceptionally_method()  {
        Product product = productRepository.findById(ID_IPHONE);

        List<CompletableFuture<ShopQuote>> futuresQuotes = shopRepository.findAll()
                .stream()
                .map(shop -> getPriceUsingExceptionallyMethodWhenExceptionOccur(shop))
                .map(futurePrice -> futurePrice.thenApply(price -> new ShopQuote(product, price)))
                .map(futureQuote -> futureQuote.thenCompose(this::applyDiscountAsync))
                .collect(toList());

        List<ShopQuote> quotes = futuresQuotes.stream().map(CompletableFuture::join).collect(toList());
        Assertions.assertNotNull(quotes);
        Assertions.assertTrue(quotes.size() > 0);
        Assertions.assertEquals(50,quotes.get(0).getCost().getValue());

    }

    @Test
    void should_skip_exceptionally_method_when_exception_doesnt_occur()  {
        Product product = productRepository.findById(ID_IPHONE);

        List<CompletableFuture<ShopQuote>> futuresQuotes = shopRepository.findAll()
                .stream()
                .map(shop -> getPriceUsingExceptionallyMethod(shop))
                .map(futurePrice -> futurePrice.thenApply(price -> new ShopQuote(product, price)))
                .map(futureQuote -> futureQuote.thenCompose(this::applyDiscountAsync))
                .collect(toList());

        List<ShopQuote> quotes = futuresQuotes.stream().map(CompletableFuture::join).collect(toList());
        Assertions.assertNotNull(quotes);
        Assertions.assertTrue(quotes.size() > 0);
        Assertions.assertEquals(20,quotes.get(0).getCost().getValue());
    }

    @Test
    void should_throw_timeoutexception()  {
        Product product = productRepository.findById(ID_IPHONE);
        String shop = shopRepository.findAll().stream().findAny().get();
        CompletableFuture<Double> future = serviceAsync.getPriceAsync(shop, product)
                .thenCombine(serviceAsync.getRateAsync(EUR, USD), this::combinePriceAndRate)
                .orTimeout(1, TimeUnit.MILLISECONDS); // configure milliseconds to whole pipeline

        Assertions.assertThrows(ExecutionException.class, () -> {
            future.get();
        });
    }


    @Test
    void showl_config_timeout_in_exchange_service_and_configure_a_default_response() throws ExecutionException, InterruptedException {
        Product product = productRepository.findById(ID_IPHONE);
        String shop = shopRepository.findAll().stream().findAny().get();
        CompletableFuture<Double> future = serviceAsync.getPriceAsync(shop, product)
                .thenCombine(getRateAsyncWithTimeout(EUR, USD), this::combinePriceAndRate)
                .orTimeout(6, TimeUnit.SECONDS); // configure milliseconds to whole pipeline

        Assertions.assertTrue(future.get() > 0);

    }

    public CompletableFuture<Double> getRateAsyncWithTimeout(ExchangeService.Money source, ExchangeService.Money destination){
        return supplyAsync(() -> getRate(source,destination)).completeOnTimeout(ExchangeService.DEFAULT_RATE, 1 , TimeUnit.MILLISECONDS);
    }

    public double combinePriceAndRate(ShopPrice price, double rate){
        return price.getValue() * rate;
    }
    public CompletableFuture<ShopQuote> applyDiscountAsync(ShopQuote quote){
        return  supplyAsync(() -> discountService.applyDiscount(quote));
    }

    /**
     * In method handle(), you have access to the result and exception of the current completable future as arguments:
     * you can transform the current result another result or recover the exception.
     * For example, given a failed future with exception “Oops” which normally returns a string, we can use handle()
     * to handle the result and exception, by either recovering from exception or returning the normal result msg directly:
     * */
    public CompletableFuture<ShopPrice> getPriceUsingHandleMethodWhenExceptionOccur(String shop){
        return  getPriceWithException(shop).handle((shopPrice, ex)-> {
            if(ex != null) return new ShopPrice("Defalt",50);
            return shopPrice;
        });
    }

    /**
     * In method exceptionally(), you only have access to the exception and not the result. Because as the method name indicates,
     * the method only handles exceptional cases: when an exception happened. If the completable future was completed successfully,
     * then the logic inside “exceptionally” will be skipped.
     * */
    public CompletableFuture<ShopPrice> getPriceUsingExceptionallyMethodWhenExceptionOccur(String shop){
        return  getPriceWithException(shop).exceptionally((ex)->new ShopPrice("Defalt",50));
    }

    /**
     * In method exceptionally(), you only have access to the exception and not the result. Because as the method name indicates,
     * the method only handles exceptional cases: when an exception happened. If the completable future was completed successfully,
     * then the logic inside “exceptionally” will be skipped.
     * */
    public CompletableFuture<ShopPrice> getPriceUsingExceptionallyMethod(String shop){
        return  getPriceWithoutException(shop).exceptionally((ex)->new ShopPrice("Defalt",50));
    }

    private CompletableFuture<ShopPrice> getPriceWithException(String shop){
        return   supplyAsync(() -> {
            try{
                Integer.parseInt("dddd");
                return new ShopPrice(shop, 20);
            }catch(Exception e){
                throw new CompletionException(e);
            }
        });
    }

    private CompletableFuture<ShopPrice> getPriceWithoutException(String shop){
        return   supplyAsync(() ->new ShopPrice(shop, 20));
    }

}
