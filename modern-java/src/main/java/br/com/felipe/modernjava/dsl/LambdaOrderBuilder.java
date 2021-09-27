package br.com.felipe.modernjava.dsl;

import br.com.felipe.modernjava.dsl.models.Order;
import br.com.felipe.modernjava.dsl.models.Stock;
import br.com.felipe.modernjava.dsl.models.Trade;

import java.util.function.Consumer;

public class LambdaOrderBuilder {
    /**
     * The order wrapped  by this builder
     * */
    private Order order = new Order();

    public static Order order(Consumer<LambdaOrderBuilder> consumer){
        LambdaOrderBuilder builder = new LambdaOrderBuilder();
        consumer.accept(builder);  // Executes the lambda expression  passed to the order builder
        return builder.order; // Returns the order populated by executing the Consumer of the OrderBuilder
    }

    public void forCustomer(String customer){
        order.setCustomer(customer); // Sets the customer  who placed  the order
    }

    public void buy(Consumer<TradeBuilder> consumer){
        trade(consumer, Trade.Type.BUY); // Consumes a TradeBuilder to create a trade to  buy a stock
    }

    public void sell(Consumer<TradeBuilder> consumer){
        trade(consumer, Trade.Type.SELL); // Consumes a TradeBuilder to create a trade to sell a stock
    }

    private void trade(Consumer<TradeBuilder> consumer, Trade.Type type){
        TradeBuilder builder = new TradeBuilder();
        builder.trade.setType(type);
        consumer.accept(builder); // Executes the lambda expression passed to the TradeBuilder
        order.addTrade(builder.trade); // Adds to the order the trade populated by  executing the Consumer of the TradeBuilder
    }


    public static class TradeBuilder{
        private Trade trade = new Trade();

        public void quantity(int quantity){
            trade.setQuantity(quantity);
        }

        public void price(double price){
            trade.setPrice(price);
        }

        public void stock(Consumer<StockBuilder> consumer){
            StockBuilder builder = new StockBuilder();
            consumer.accept(builder);
            trade.setStock(builder.stock);
        }
    }


    public static class StockBuilder{
        private Stock stock = new Stock();

        public void symbol(String symbol){
            stock.setSymbol(symbol);
        }

        public void market(String market) {
            stock.setMarket(market);
        }
    }
}
