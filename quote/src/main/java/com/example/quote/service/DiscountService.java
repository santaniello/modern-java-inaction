package com.example.quote.service;

import com.example.quote.model.Discount;
import com.example.quote.model.ShopQuote;
import com.example.quote.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository repository;

    public ShopQuote applyDiscount(ShopQuote quote){
        Discount.Code discountCodeByProduct = repository.findDiscountByProduct(quote.getProduct().getId());
        return quote.applyDiscount(discountCodeByProduct);
    }
}
