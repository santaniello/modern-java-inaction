package com.example.quote.repository;

import com.example.quote.model.Discount;
import com.example.quote.model.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.quote.model.Discount.*;

@Repository
public class ProductRepository {
    private static Map<Integer, Product> products;
    public static int ID_IPHONE = 1;
    public static int ID_SAMSUNG_TV = 2;
    public static int ID_SAMSUNG_GALAXY = 3;
    public static int ID_DELL_NOTEBOOK = 4;
    public static int ID_PLAY_5 = 5;

    static {
        products = Map.ofEntries(
            Map.entry(ID_IPHONE, new Product(ID_IPHONE,"Iphone X")),
            Map.entry(ID_SAMSUNG_TV, new Product(ID_SAMSUNG_TV,"Samsung TV")),
            Map.entry(ID_SAMSUNG_GALAXY, new Product(ID_SAMSUNG_GALAXY,"Sansung Galaxy")),
            Map.entry(ID_DELL_NOTEBOOK, new Product(ID_DELL_NOTEBOOK,"Dell Notebook")),
            Map.entry(ID_PLAY_5, new Product(ID_PLAY_5,"Play 5")));
    }

    public List<Product> findAll(){
        return products.values().stream().collect(Collectors.toList());
    }

    public Product findById(int id){
        return products.get(id);
    }
}
