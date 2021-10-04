package com.example.quote.model;

import lombok.Getter;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

@Getter
public class QuoteStatistic {
    private long statisticInSeconds;
    private long statisticInMiliseconds;

    public QuoteStatistic(Duration duration){
        this.statisticInSeconds = duration.getSeconds();
        this.statisticInMiliseconds = duration.toMillis();
    }
}
