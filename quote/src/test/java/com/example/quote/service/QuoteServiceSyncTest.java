package com.example.quote.service;

import com.example.quote.model.Quote;
import com.example.quote.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.quote.repository.ProductRepository.*;

@SpringBootTest
public class QuoteServiceSyncTest {

    @Autowired
    private QuoteServiceSync service;

    @Test
    void should_quote_with_sucess(){
        Quote quote = service.quote(ID_IPHONE);
        Assertions.assertNotNull(quote);
        Assertions.assertNotNull(quote.getStatistic());
        System.out.println(quote.getStatistic().getStatisticInSeconds());
        Assertions.assertEquals(10,quote.getStatistic().getStatisticInSeconds());
    }

    @Test
    void should_quote_in_parallel_with_sucess(){
        Quote quoteInParallel = service.quoteInParallel(ID_IPHONE);
        Assertions.assertNotNull(quoteInParallel);
        Assertions.assertNotNull(quoteInParallel.getStatistic());

        Quote quote = service.quote(ID_IPHONE);
        Assertions.assertNotNull(quote);
        Assertions.assertNotNull(quote.getStatistic());

        Assertions.assertEquals(2, quoteInParallel.getStatistic().getStatisticInSeconds());
        Assertions.assertTrue(quoteInParallel.getStatistic().getStatisticInSeconds() < quote.getStatistic().getStatisticInSeconds());
    }
}
