package br.com.felipe.modernjava.dsl;

import br.com.felipe.modernjava.dsl.models.Order;
import br.com.felipe.modernjava.dsl.models.Stock;
import br.com.felipe.modernjava.dsl.models.Trade;

import java.util.stream.Stream;

public class NestedFunctionOrderBuilder {

    /**
     * Creates an order for a given customer
     * */
    public static Order order(String customer, Trade... trades){
        Order order = new Order();
        order.setCustomer(customer);
        Stream.of(trades).forEach(order::addTrade);
        return order;
    }


    /**
     * Creates a trade to buy a stock
     * */
    public static Trade buy(int quantity, Stock stock, double price){
        return buildTrade(quantity, stock, price, Trade.Type.BUY);
    }

    /**
     * Creates a trade to sell a stock
     * */
    public static Trade sell(int quantity, Stock stock, double price){
        return buildTrade(quantity, stock, price, Trade.Type.SELL);
    }

   public static Trade buildTrade(int quantity, Stock stock, double price, Trade.Type buy){
        Trade trade = new Trade();
        trade.setQuantity(quantity);
        trade.setType(buy);
        trade.setStock(stock);
        trade.setPrice(price);
        return  trade;
   }

   /**
    * A dummy method to define the unit price of the traded stock
    * */
   public static double at(double price) { return price;}

    /**
     * Creates the traded stock
     * */
   public static Stock stock(String symbol, String market){
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        stock.setMarket(market);
        return stock;
   }

    /**
     * A dummy method to define the market where the stock os traded
     * */
   public static String on(String market){
        return market;
   }
}
