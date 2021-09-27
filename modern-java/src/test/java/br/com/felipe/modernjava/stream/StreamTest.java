package br.com.felipe.modernjava.stream;

import br.com.felipe.modernjava.stream.models.Dish;
import br.com.felipe.modernjava.stream.models.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.nio.charset.Charset.*;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.*;
import static java.util.stream.Collectors.toList;

public class StreamTest {

    private List<Dish> menu;

    @BeforeEach
    void setUp() {
        this.menu = Arrays.asList(
                new Dish("pork", false, 800, Type.MEAT),
                new Dish("beef", false, 700, Type.MEAT),
                new Dish("chicken", false, 400, Type.MEAT),
                new Dish("french fries", true, 530, Type.OTHER),
                new Dish("rice", true, 350, Type.OTHER),
                new Dish("season fruit", true, 120, Type.OTHER),
                new Dish("pizza", true, 550,   Type.OTHER),
                new Dish("prawns", false, 300, Type.FISH),
                new Dish("salmon", false, 450, Type.FISH)
        );
    }

    @Test
    void teste(){
        List<String> names =
                menu.stream()
                        .filter(dish -> {
                            System.out.println("filtering:" + dish.getName());
                            return dish.getCalories() > 300;
                        })
                        .map(dish -> {
                            System.out.println("mapping:" + dish.getName());
                            return dish.getName();
                        })

                .limit(3)
                .collect(toList());
        System.out.println(names);
    }

    /**
     * The distinct operation remove from the list all the elements duplicated using the equals method !
     * */
    @Test
    void must_filter_unique_elements_in_a_list_with_distinct(){
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i > 2 )
                .distinct()
                .forEach(System.out::println);
    }


    /**
     * If we need to get some elements in a list, we could use the filter operation to do this.
     * The downside of using the filter operation here is that you need to iterate through the
     * whole stream and the predicate is applied to each element. Instead, you could stop
     * once you found a dish that is greater than (or equal to) 320 calories. With a small list
     * this may not seem like a huge benefit, but it can become useful if you work with poten-
     * tially large stream of elements. But how do you specify this? The takeWhile operation
     * is here to rescue you! It lets you slice any stream (even an infinite stream) using a predicate.
     * */
    @Test
    void must_slice_a_list_with_takewhile(){
        List<Dish> specialMenu = Arrays.asList(
                new Dish("seasonal fruit", true, 120, Type.OTHER),
                new Dish("prawns", false, 300, Type.FISH),
                new Dish("rice", true, 350, Type.OTHER),
                new Dish("chicken", false, 400, Type.MEAT),
                new Dish("french fries", true, 530, Type.OTHER));

        List<Dish> slicedMenu1
                = specialMenu.stream()
                .takeWhile(dish -> dish.getCalories() < 320)
                .collect(toList());

        Assertions.assertEquals(2, slicedMenu1.size());


    }

    /**
     * The dropWhile operation is the complement of takeWhile . It throws away the ele-
     * ments at the start where the predicate is false. Once the predicate evaluates to true it
     * stops and returns all the remaining elements, and it even works if there are an infinite
     * number of remaining elements!
     *
     * In the example below dropwhile will select the elements with more than 320 calories !
     *
     * */
    @Test
    void must_slice_a_list_with_dropwhile(){
        List<Dish> specialMenu = Arrays.asList(
                new Dish("seasonal fruit", true, 120, Type.OTHER),
                new Dish("prawns", false, 300, Type.FISH),
                new Dish("rice", true, 350, Type.OTHER),
                new Dish("chicken", false, 400, Type.MEAT),
                new Dish("french fries", true, 530, Type.OTHER));

        List<Dish> slicedMenu1
                = specialMenu.stream()
                .dropWhile(dish -> dish.getCalories() < 320)
                .collect(toList());

        Assertions.assertEquals(3, slicedMenu1.size());
    }

    /**
     * Streams support the limit(n) method, which returns another stream that’s no lon-
     * ger than a given size. The requested size is passed as argument to limit .
     * */
    @Test
    void must_truncate_a_list_with_limit(){
        List<Dish> specialMenu = Arrays.asList(
                new Dish("seasonal fruit", true, 120, Type.OTHER),
                new Dish("prawns", false, 300, Type.FISH),
                new Dish("rice", true, 350, Type.OTHER),
                new Dish("chicken", false, 400, Type.MEAT),
                new Dish("french fries", true, 530, Type.OTHER));

        List<Dish> slicedMenu1
                = specialMenu.stream()
                .dropWhile(dish -> dish.getCalories() < 320)
                .limit(1)
                .collect(toList());

        Assertions.assertEquals(1, slicedMenu1.size());
    }

    /**
     * Streams support the skip(n) method to return a stream that discards the first n ele-
     * ments. If the stream has fewer than n elements, an empty stream is returned. Note that
     * limit(n) and skip(n) are complementary! For example, the following code skips the
     * first two dishes that have more than 300 calories and returns the rest.
     * */
    @Test
    void must_skip_elements_in_a_list(){
        List<Dish> specialMenu = Arrays.asList(
                new Dish("seasonal fruit", true, 120, Type.OTHER),
                new Dish("prawns", false, 300, Type.FISH),
                new Dish("rice", true, 350, Type.OTHER),
                new Dish("chicken", false, 400, Type.MEAT),
                new Dish("french fries", true, 530, Type.OTHER));

        List<Dish> dishes = specialMenu.stream()
                .filter(d -> d.getCalories() > 300)
                .skip(2)
                .collect(toList());

        Assertions.assertEquals(1, dishes.size());
        Assertions.assertEquals(530, dishes.get(0).getCalories());

    }

    @Test
    void must_map_a_list() {
        menu.stream()
                .map(Dish::getCalories) // I get the calories of a dish to create a new list
                .collect(toList())
                .forEach(d -> System.out.println(d));
    }

    @Test
    void must_flatMap_two_lists() {
        List<String> letters1 = Arrays.asList("a", "b");
        List<String> letters2 = Arrays.asList("b", "c", "d");
        List<String> letters3 = Arrays.asList("e", "f");
        List<List<String>> listOfLetters = Arrays.asList(letters1, letters2, letters3);

        List<String> flatList = listOfLetters.stream()
                .flatMap(List::stream) //  Converts a stream of collections into a single “flat” stream
                .collect(Collectors.toList());

        System.out.println(flatList);
    }

    @Test
    void must_check_if_a_predicate_matches_at_least_one_element() {
        Assertions.assertTrue(menu.stream().anyMatch(Dish::isVegetarian));
    }

    @Test
    void must_check_if_a_predicate_matches_at_with_all_elements(){
        Assertions.assertTrue(menu.stream()
                .allMatch(dish -> dish.getCalories() < 1000));
    }

    @Test
    void must_check_if_none_elements_in_the_stream_matches_with_a_predicate(){
        Assertions.assertTrue(menu.stream()
                .noneMatch(d -> d.getCalories() >= 1000));
    }

    @Test
    void must_find_one_vegetarian_food_in_the_menu(){
        Optional<Dish> any = menu.stream()
                .filter(Dish::isVegetarian)
                .findAny();

        Assertions.assertTrue(any.isPresent());
    }

    /**
     * When to use findFirst and findAny
     * You may wonder why we have both findFirst and findAny. The answer is parallel-
     * ism. Finding the first element is more constraining in parallel. If you don’t care about
     * which element is returned, use findAny because it’s less constraining when using
     * parallel streams.
     * */

    @Test
    void must_find_the_first_element_in_the_menu(){
        menu.stream()
            .map(Dish::getCalories)
            .filter(c->c > 500)
            .findFirst()
            .ifPresent(System.out::println);
    }

    /**
     * A reduction is a terminal operation that aggregates a stream into a type or a primitive.
     * We transform a stream in a single output variable
     * In the example below, we summarize the calories in a menu.
     * i'ts like:
     *
     * int sum = 0;
     * for (int x : numbers) {
     * sum += x;
     * }
     *
     * */
    @Test
    void must_count_the_total_calories_in_the_menu(){
        Integer totalCalories = menu.stream()
                .map(Dish::getCalories)
                .reduce(0,Integer::sum); // 0 is the initial value to start the counting

        Assertions.assertEquals(4200, totalCalories);
    }

    /**
     * when we do:
     * int calories = menu.stream()
     *      .map(Dish::getCalories)
     *      .reduce(0, Integer::sum);
     *
     * The problem with this code is that there’s an insidious boxing cost. Behind the scenes
     * each Integer needs to be unboxed to a primitive before performing the summation.
     *
     * In this case when we work with primitive types (int, double, long) we have specialized stream interfaces to work
     * with this values (IntStream , DoubleStream , and LongStream) and thereby avoid hidden boxing costs
     *
     * */

    @Test
    void must_use_IntStream_instead_stream_integer(){
        int calories = menu.stream()
                // Here, the method mapToInt extracts all the calories from each dish (represented as an
                // Integer) and returns an IntStream as the result (rather than a Stream<Integer> )
                .mapToInt(Dish::getCalories)
                .sum();

        Assertions.assertEquals(4200, calories);
    }

    @Test
    void must_boxed_int_value_to_integer(){
        IntStream intStream = menu.stream()
                .mapToInt(Dish::getCalories);

        // We return IntStream to  Stream<Integer>
        Stream<Integer> boxed = intStream.boxed();
    }

    @Test
    void must_create_stream_with_method_StreamOf(){
        // Creating a Stream of Strings
        Stream<String> stream = Stream.of("Modern ", "Java ", "In ", "Action");

        stream.map(String::toUpperCase).forEach(System.out::println);
    }

    @Test
    void must_create_stream_that_accept_null_values(){
        Stream<String> homeValueStream
                = Stream.ofNullable(System.getProperty("home"));
    }

    @Test
    void must_create_stream_from_array(){
        int[] numbers = {2, 3, 5, 7, 11, 13};
        IntStream stream = Arrays.stream(numbers);
    }


    @Test
    void must_create_stream_from_a_file(){
        long uniqueWords = 0;
        try(Stream<String> lines = lines(get("data.txt"), defaultCharset())){
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
        }
        catch(IOException e){
        }
    }

    /**
     * The Streams API provides two static methods to generate a stream from a function:
     * Stream.iterate and Stream.generate . These two operations let you create what we
     * call an infinite stream, a stream that doesn’t have a fixed size like when you create a
     * stream from a fixed collection. Streams produced by iterate and generate create
     * values on demand given a function and can therefore calculate values forever! It’s
     * generally sensible to use limit(n) on such streams to avoid printing an infinite num-
     * ber of values.
     *
     * The iterate method takes an initial value, here 0 , and a lambda (of type Unary-
     * Operator<T> ) to apply successively on each new value produced.
     * */
    @Test
    void must_create_a_infinite_stream_with_iterate_method(){
        Stream.iterate(0, n -> n + 2)
                .limit(10)
                .forEach(System.out::println);
    }

    /**
     * Similarly to the method iterate , the method generate lets you produce an infinite
     * stream of values computed on demand. But generate doesn’t apply successively a
     * function on each new produced value. It takes a lambda of type Supplier<T> to pro-
     * vide new values.
     * **/
    @Test
    void must_create_a_infinite_stream_with_generate_method(){
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);
    }
}
