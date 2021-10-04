package com.example.quote.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class Quote {
    private List<ShopQuote> quotes;
    @Getter
    private QuoteStatistic statistic;

    public Quote() {
        this.quotes = new ArrayList<>();
    }

    public List<ShopQuote> getQuotes() {
        return Collections.unmodifiableList(quotes);
    }
}
