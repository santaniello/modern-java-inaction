package br.com.felipe.modernjava.lambda;

import br.com.felipe.modernjava.lambda.interfaces.ApplePredicate;
import br.com.felipe.modernjava.lambda.models.Apple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static br.com.felipe.modernjava.lambda.models.Color.*;
import static java.util.Arrays.*;

public class LambdaTest {

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
        BehaviorParameterization parameterization = new BehaviorParameterization();
        List<Apple> greenApples = parameterization.filter(inventory, new AppleGreenColorPredicate());
        Assertions.assertTrue(greenApples.size() == 1);
    }

    /**
     * Example of behavior parameterization using anonymous class
     * */
    @Test
    void must_filter_only_apples_bigger_then_150(){
        BehaviorParameterization parameterization = new BehaviorParameterization();
        List<Apple> heavyApples = parameterization.filter(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return apple.getWeight() > 150;
            }
        });
        Assertions.assertTrue(heavyApples.size() == 1);
    }

    @Test
    void must_filter_only_green_apples_with_lambdas(){
        BehaviorParameterization parameterization = new BehaviorParameterization();
        // Using lambda to filter green Apples
        List<Apple> greenApples = parameterization.filter(inventory, (Apple a) -> GREEN.equals(a.getColor()));
        Assertions.assertTrue(greenApples.size() == 1);
    }

    @Test
    void must_filter_only_green_apples_with_lambdas_using_the_predicate_interface(){
        BehaviorParameterization parameterization = new BehaviorParameterization();
        // Using lambda to filter green Apples
        List<Apple> greenApples = parameterization.filterUsingInterfacePredicate(inventory, (Apple a) -> GREEN.equals(a.getColor()));
        Assertions.assertTrue(greenApples.size() == 1);
    }

    @Test
    void must_read_a_string_inside_a_file_using_lambdas() throws IOException {
        BehaviorParameterization parameterization = new BehaviorParameterization();
        String s = parameterization.processFile(b -> b.readLine());
        Assertions.assertEquals("Test", s);
    }

    @Test
    void must_perform_an_action_using_consumer_interface(){
        BehaviorParameterization parameterization = new BehaviorParameterization();
        parameterization.foreach(asList(1,2,3,4,5), c -> System.out.println(c));
    }

    @Test
    void must_map_list_of_apples_to_a_list_of_weight_using_function_interface()  {
        BehaviorParameterization parameterization = new BehaviorParameterization();
        List<Integer> result = parameterization.map(inventory, a -> a.getWeight());
        Assertions.assertEquals(2, result.size() );
        Assertions.assertTrue(result.contains(100));
        Assertions.assertTrue(result.contains(200));
    }

    @Test
    void must_compose_predicates()  {
        Predicate<Apple> redApple  = (Apple a) -> a.getColor().equals(RED);
        Predicate<Apple> redAndHeavyApple = redApple.and(apple -> apple.getWeight() > 150)
                                                    .or(apple -> GREEN.equals(apple.getColor()));
    }

    /**
     * The method andThen returns a function that first applies a given function to an
     * input and then applies another function to the result of that application. For exam-
     * ple, given a function f that increments a number (x -> x + 1) and another function g
     * that multiples a number by 2, you can combine them to create a function h that first
     * increments a number and then multiplies the result by 2
     * */

    @Test
    void must_compose_functions_with_andThen()  {
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = f.andThen(g);
        Assertions.assertTrue(h.apply(1) == 4);
    }

    /**
     * You can also use the method compose similarly to first apply the function given as argu-
     * ment to compose and then apply the function to the result. For example, in the previous
     * example using compose , it would mean f(g(x)) instead of g(f(x)) using andThen
     *
     * */
    @Test
    void must_compose_functions_with_compose()  {
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = f.compose(g);
        Assertions.assertTrue(h.apply(1) == 3);
    }






}
