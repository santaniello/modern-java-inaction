package br.com.felipe.modernjava.lambda.refactoring.designpatterns.templatemethod;

import java.util.function.Consumer;

public class OnlineBankingWithLambda {
    public void processCustomer(int id, Consumer<Customer> makeCustomerHappy){
        Customer c = new Customer(id);
        makeCustomerHappy.accept(c);
    }
}
