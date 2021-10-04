package com.example.quote.repository;

import com.example.quote.model.Discount;
import com.example.quote.model.Product;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static com.example.quote.repository.ProductRepository.*;
import static com.example.quote.repository.ProductRepository.ID_IPHONE;

@Repository
public class DiscountRepository {
    private static Map<Integer, Discount.Code> discountsByProduct;

    static {
        discountsByProduct = Map.ofEntries(
                Map.entry(ID_IPHONE, Discount.Code.DIAMOND),
                Map.entry(ID_SAMSUNG_TV, Discount.Code.PLATINUM),
                Map.entry(ID_SAMSUNG_GALAXY, Discount.Code.NONE),
                Map.entry(ID_DELL_NOTEBOOK, Discount.Code.PLATINUM),
                Map.entry(ID_PLAY_5, Discount.Code.DIAMOND));
    }

    public Discount.Code findDiscountByProduct(int idProduct){
        return discountsByProduct.get(idProduct);
    }
}
