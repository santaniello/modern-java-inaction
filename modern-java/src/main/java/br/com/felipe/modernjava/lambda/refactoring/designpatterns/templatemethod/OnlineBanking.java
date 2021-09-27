package br.com.felipe.modernjava.lambda.refactoring.designpatterns.templatemethod;

public abstract class OnlineBanking {
    public void processCustomer(int id){
        Customer c = new Customer(id);
        makeCustomerHappy(c);
    }
    /**
    Now different branches can provide different implementations of the makeCustomerHappy method by sub-
    classing the OnlineBanking class.
    **/
    abstract void makeCustomerHappy(Customer c);
}
