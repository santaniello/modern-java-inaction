package br.com.felipe.modernjava.dsl;

import br.com.felipe.modernjava.dsl.models.Order;
import br.com.felipe.modernjava.dsl.models.Stock;
import br.com.felipe.modernjava.dsl.models.Tax;
import br.com.felipe.modernjava.dsl.models.Trade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static br.com.felipe.modernjava.dsl.LambdaOrderBuilder.order;
import static br.com.felipe.modernjava.dsl.MethodChainingOrderBuilder.*;
import static br.com.felipe.modernjava.dsl.MixedBuilder.buy;
import static br.com.felipe.modernjava.dsl.MixedBuilder.forCustomer;
import static br.com.felipe.modernjava.dsl.MixedBuilder.sell;
import static br.com.felipe.modernjava.dsl.NestedFunctionOrderBuilder.*;

public class DSLTest {

    @Test
    void must_create_an_order_without_dsl(){
        Order order = new Order();
        order.setCustomer("BigBank");
        Trade trade1 = new Trade();
        trade1.setType(Trade.Type.BUY);
        Stock stock1 = new Stock();
        stock1.setSymbol("IBM");
        stock1.setMarket("NYSE");
        trade1.setStock(stock1);
        trade1.setPrice(125.00);
        trade1.setQuantity(80);
        order.addTrade(trade1);
        Trade trade2 = new Trade();
        trade2.setType(Trade.Type.BUY);
        Stock stock2 = new Stock();
        stock2.setSymbol("GOOGLE");
        stock2.setMarket("NASDAQ");
        trade2.setStock(stock2);
        trade2.setPrice(375.00);
        trade2.setQuantity(50);
        order.addTrade(trade2);

        Assertions.assertEquals(2,order.getTrades().size());
    }

    @Test
    void must_create_an_order_using_chaining_method_dsl_strategy(){
        Order order = MethodChainingOrderBuilder.forCustomer("BigBank")
                          .buy(80)
                          .stock("IBM")
                              .on("NYSE")
                          .at(125.00)
                          .sell(50)
                          .stock("Google")
                              .on("Nasdack")
                          .at(375.00)
                      .end();

        Assertions.assertEquals(2,order.getTrades().size());

    }

    @Test
    void must_create_an_order_using_nested_functions_dsl_strategy(){
        Order order = NestedFunctionOrderBuilder.order("BigBank",
                NestedFunctionOrderBuilder.buy(80,
                        stock("IBM", on("NYSE")),
                        at(125.00)),
                NestedFunctionOrderBuilder.sell(50,
                        stock("Google", on("NASDAQ")),
                        at(375.00))
        );

        Assertions.assertEquals(2,order.getTrades().size());
    }

    @Test
    void must_create_an_order_using_function_sequencing_with_lambda_expressions(){
        Order order = order(o -> {
            o.forCustomer("BigBank");
            o.buy(t -> {
                t.quantity(80);
                t.price(125.00);
                t.stock(s -> {
                    s.symbol("IBM");
                    s.market("NYSE");
                });
            });
            o.sell(t -> {
                t.quantity(50);
                t.price(375.00);
                t.stock(s -> {
                    s.symbol("GOOGLE");
                    s.market("NASDAQ");
                });
            });
        });

        Assertions.assertEquals(2,order.getTrades().size());
    }

    @Test
    void must_create_an_order_using_a_mixed_builder(){
        Order order = forCustomer( "BigBank", // Nested function to specify attributes of the top-level order
                                             buy( t -> t.quantity( 80 ) // Lambda expression to  create a single trade
                                                        .stock( "IBM" ) // Method Chaining in the body of the lambda expression that populates the trade object
                                                        .on("NYSE" )
                                                        .at( 125.00 )),
                                             sell( t -> t.quantity( 50 )
                                                         .stock( "GOOGLE" )
                                                         .on( "NASDAQ" )
                                                         .at( 125.00 )) );
        Assertions.assertEquals(2,order.getTrades().size());
    }

    @Test
    void must_use_method_reference_in_a_dll(){
        Order order =
                forCustomer("BigBank",
                        buy(t -> t.quantity(80)
                                .stock("IBM")
                                .on("NYSE")
                                .at(125.00)),
                        sell(t -> t.quantity(50)
                                .stock("GOOGLE")
                                .on("NASDAQ")
                                .at(125.00)));


        double value = new TaxCalculator()
                .with(Tax::regional)
                .with(Tax::surcharge)
                .calculate(order);

        Assertions.assertEquals(18768.75, value);

    }




}
