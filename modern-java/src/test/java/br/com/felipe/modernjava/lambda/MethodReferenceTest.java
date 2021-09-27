package br.com.felipe.modernjava.lambda;

import br.com.felipe.modernjava.lambda.models.Apple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;

import static br.com.felipe.modernjava.lambda.models.Color.GREEN;
import static br.com.felipe.modernjava.lambda.models.Color.RED;
import static java.util.Comparator.*;

public class MethodReferenceTest {

    private List<Apple> inventory;

    @BeforeEach
    void setUp() {
        this.inventory = new ArrayList<>();
        inventory.add(new Apple(100, GREEN));
        inventory.add(new Apple(200, RED));
    }

    /**
     * Example of behavior parameterization using a dedicated class
     * */
    @Test
    void must_filter_only_green_apples(){
        ToIntFunction<String> stringToIntWhithoutMethodReference =
                (String s) -> Integer.parseInt(s);

        ToIntFunction<String> stringToIntWhithMethodReference =
                (String s) -> Integer.parseInt(s);
    }

    /**
     * This lambda expression forwards its argument to the static method parseInt
     * of Integer. This method takes a String to parse and returns an int.
     * */
    @Test
    void must_use_method_reference_with_static_method(){
        ToIntFunction<String> stringToIntWhithoutMethodReference =
                (String s) -> Integer.parseInt(s);

        ToIntFunction<String> stringToIntWhithMethodReference =
                (String s) -> Integer.parseInt(s);
    }

    /**
     * This lambda uses its first argument to call the method contains on it.
     * This is because the target type describes a function descriptor (List<String>,
     * String) -> boolean, and List::contains can be unpacked to that func-
     * tion descriptor.
     * */
    @Test
    void must_use_method_reference_with_instace_method(){
        BiPredicate<List<String>, String> contains = List::contains;
    }

    /**
     * This expression-style lambda invokes a private helper method.
     * */
    @Test
    void must_use_method_reference_with_private_method(){
        Predicate<String> startsWithNumber = this::startsWithNumber;
    }

    /**
     * We need to find a FunctionalInterface That the parameters match with the constructor
     * arguments.
     *
     * The interface Supplier doesn't receive arguments and because this, we can use with the
     * Apples's constructor whithout arguments.
     * */
    @Test
    void must_use_method_reference_with_constructor_whithout_arguments(){
        Supplier<Apple> c1 = Apple::new;

        // Its's equivalent to:
        Supplier<Apple> c2 = () -> new Apple();
    }

    /**
     * We need to find a FunctionalInterface That the parameters match with the constructor
     * arguments.
     *
     * The interface Function<Integer, Apple> receive one input parameter (Integer) and we can use this parameter
     * with the Apple(weight) constructor.
     * */
    @Test
    void must_use_method_reference_with_constructor_whith_arguments(){
        Function<Integer, Apple> c2 = Apple::new;
        Function<Integer, Apple> c3 = (weight) -> new Apple(weight);
    }

    @Test
    void must_map_a_list_of_weights_to_A_list_of_apples_using_function_and_method_reference(){
        List<Integer> weights = Arrays.asList(7, 3, 4, 10);
        List<Apple> apples = map(weights, Apple::new);
        Assertions.assertTrue(apples.size() > 0);
    }

    @Test
    void must_sort_a_list_using_lambda_with_method_reference_and_comparator_interface(){
        inventory.sort(comparing(Apple::getWeight));
        Assertions.assertEquals(100, inventory.get(0).getWeight());
    }

    @Test
    void must_use_a_chainning_comparator(){
        inventory.sort(comparing(Apple::getWeight)
                       .reversed()
                       .thenComparing(Apple::getColor)); // If two apples have the same weight the Comparator will use color to sort
        Assertions.assertEquals(200, inventory.get(0).getWeight());
    }




    public List<Apple> map(List<Integer> list, Function<Integer, Apple> f) {
        List<Apple> result = new ArrayList<>();
        for(Integer i: list) {
            result.add(f.apply(i));
        }
        return result;
    }

    private boolean startsWithNumber(String s){
        return false;
    }
}
