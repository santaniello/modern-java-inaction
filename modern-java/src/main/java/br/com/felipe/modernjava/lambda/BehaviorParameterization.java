package br.com.felipe.modernjava.lambda;

import br.com.felipe.modernjava.lambda.interfaces.ApplePredicate;
import br.com.felipe.modernjava.lambda.interfaces.BufferedReaderProcessor;
import br.com.felipe.modernjava.lambda.models.Apple;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class BehaviorParameterization {

    public List<Apple> filter(List<Apple> inventory, ApplePredicate predicate){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if(predicate.test(apple))
               result.add(apple);
        }
        return  result;
    }
    /**
    The java.util.function.Predicate<T> interface defines an abstract method named
    test that accepts an object of generic type T and returns a boolean . It’s exactly the
    same one that you created earlier with the ApplePredicate interface.
    **/
    public List<Apple> filterUsingInterfacePredicate(List<Apple> inventory, Predicate<Apple> predicate){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if(predicate.test(apple))
                result.add(apple);
        }
        return  result;
    }

    public String processFile(BufferedReaderProcessor processor) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))){
            return processor.process(br);
        }
    }

    /**
     * The java.util.function.Consumer<T> interface defines an abstract method named
     * accept that takes an object of generic type T and returns no result ( void ). You might
     * use this interface when you need to access an object of type T and perform some oper-
     * ations on it.
     * */
    public void foreach(List<Integer> list, Consumer<Integer> c){
        for(Integer i :list){
            c.accept(i);
        }
    }

    /**
     * The java.util.function.Function<T, R> interface defines an abstract method
     * named apply that takes an object of generic type T as input and returns an object of
     * generic type R . You might use this interface when you need to define a lambda that
     * maps information from an input object to an output (for example, extracting the
     * weight of an apple or mapping a string to its length).
     * */
    public List<Integer> map(List<Apple> list, Function<Apple, Integer> f){
        List<Integer> result = new ArrayList<>();
        for(Apple a: list){
            result.add(f.apply(a));
        }
        return result;
    }
}
