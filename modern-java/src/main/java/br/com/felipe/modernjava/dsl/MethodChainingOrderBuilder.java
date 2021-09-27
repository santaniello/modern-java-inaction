package br.com.felipe.modernjava.dsl;

import br.com.felipe.modernjava.dsl.models.Order;
import br.com.felipe.modernjava.dsl.models.Stock;
import br.com.felipe.modernjava.dsl.models.Trade;
import lombok.AllArgsConstructor;

public class MethodChainingOrderBuilder {
    /**
     * The order wrapped by this builder
     * */
    private final Order order = new Order();

    private MethodChainingOrderBuilder(String customer){
        order.setCustomer(customer);
    }

    /**
     * A static factory method to create a
     * builder of an order placed by a given customer
     * */
    public static MethodChainingOrderBuilder forCustomer(String customer){
        return new MethodChainingOrderBuilder(customer);
    }

    /**
     * Terminates the building of the order and returns it
     * */
    public Order end() {
        return order;
    }

    /**
     * Creates a TradeBuilder to build a
     * trade to buy a stock  **/
    public TradeBuilder buy(int quantity){
        return new TradeBuilder(this, Trade.Type.BUY, quantity);
    }

    /**
     * Creates a  TradeBuilder
     * to build a  trade to sell  a stock
     * */
    public TradeBuilder sell(int quantity){
        return new TradeBuilder(this, Trade.Type.SELL, quantity);
    }

    /** Adds a trade to the order */
    public MethodChainingOrderBuilder addTrade(Trade trade){
        order.addTrade(trade);
        return this;
    }

    public class TradeBuilder {
        private final MethodChainingOrderBuilder methodChainingOrderBuilder;
        public final Trade trade = new Trade();

        private TradeBuilder(MethodChainingOrderBuilder builder, Trade.Type type, int quantity){
            this.methodChainingOrderBuilder = builder;
            trade.setType(type);
            trade.setQuantity(quantity);
        }

        public StockBuilder stock (String symbol){
            return new StockBuilder(methodChainingOrderBuilder, trade, symbol);
        }
    }

    public class StockBuilder {
        private final MethodChainingOrderBuilder methodChainingOrderBuilder;
        private final Trade trade;
        private final Stock stock = new Stock();

        private StockBuilder(MethodChainingOrderBuilder builder,
                     Trade trade, String symbol){
            this.methodChainingOrderBuilder = builder;
            this.trade = trade;
            stock.setSymbol(symbol);
        }

        public TradeBuilderWithStock on(String market){
            stock.setMarket(market);
            trade.setStock(stock);
            return new TradeBuilderWithStock(methodChainingOrderBuilder, trade);
        }
    }

    @AllArgsConstructor
    public class TradeBuilderWithStock {
        private final MethodChainingOrderBuilder methodChainingOrderBuilder;
        private final Trade trade;

        public MethodChainingOrderBuilder at(double price){
            trade.setPrice(price);
            return methodChainingOrderBuilder.addTrade(trade);
        }

    }
}
