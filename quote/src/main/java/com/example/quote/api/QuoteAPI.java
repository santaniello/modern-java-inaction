package com.example.quote.api;

import com.example.quote.model.Quote;
import com.example.quote.service.QuoteServiceAsync;
import com.example.quote.service.QuoteServiceSync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
public class QuoteAPI {

    @Autowired
    private QuoteServiceSync quoteServiceSync;

    @Autowired
    private QuoteServiceAsync quoteServiceAsync;

    @GetMapping(value = "/quote/sync/{id}")
    public Quote quoteSync(@PathParam("id") int id) {
        return quoteServiceSync.quote(id);
    }

    @GetMapping(value = "/quote/async/{id}")
    public Quote quoteAsync(@PathParam("id") int id) {
        return quoteServiceAsync.quote(id);
    }
}
