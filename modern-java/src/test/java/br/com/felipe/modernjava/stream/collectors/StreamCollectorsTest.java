package br.com.felipe.modernjava.stream.collectors;

import br.com.felipe.modernjava.stream.models.CaloricLevel;
import br.com.felipe.modernjava.stream.models.Dish;
import br.com.felipe.modernjava.stream.models.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

public class StreamCollectorsTest {

    private List<Dish> menu;

    @BeforeEach
    void setUp() {
        this.menu = asList(
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
    void must_count_the_number_of_dishes_in_the_menu_using_the_collector_returned_by_the_counting_factory_method(){
        long howManyDishes = menu.stream().collect(counting());
        Assertions.assertEquals(9, howManyDishes);
    }

    @Test
    void must_find_the_max_calorie(){
        Optional<Dish> mostCalorieDish =  menu.stream()
                        .collect(maxBy(comparing(Dish::getCalories)));

        Assertions.assertEquals(800, mostCalorieDish.get().getCalories());
    }

    @Test
    void must_find_the_min_calorie(){
        Optional<Dish> mostCalorieDish =  menu.stream()
                .collect(minBy(comparing(Dish::getCalories)));

        Assertions.assertEquals(120, mostCalorieDish.get().getCalories());
    }

    /**
     * The method summingInt, add all elements of a stream.
     * We have the methods summingDouble and summarizingLong
     * */
    @Test
    void must_use_summingInt_method(){
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
        Assertions.assertEquals(4200, totalCalories);
    }

    /**
     * With the method summarizingInt, we have all the statics about a stream (Min, Max, Count, Sum, Avg) in a single operation
     * We have the methods summarizingLong and summarizingDouble
     * */

    @Test
    void must_summarizing_the_dishs(){
        IntSummaryStatistics summary = menu.stream().collect(summarizingInt(Dish::getCalories));
        Assertions.assertEquals(120,summary.getMin());
        Assertions.assertEquals(800,summary.getMax());
        Assertions.assertEquals(9, summary.getCount());
        Assertions.assertEquals(4200, summary.getSum());
    }

    /**
     * The joining creates a single String with all dishs names.
     * The joining uses the toString method in the Dish class.
     * Also note that if the Dish class had a toString method returning the dish’s name,
     * you’d obtain the same result without needing to map over the original
     * stream with a function extracting the name from each dish.
     * */
    @Test
    void must_convert_a_stream_in_a_single_string(){
        String shortMenu = menu.stream().map(Dish::getName).collect(joining());
        Assertions.assertEquals("porkbeefchickenfrench friesriceseason fruitpizzaprawnssalmon",shortMenu);
    }

    /**
     * The joining factory method is overloaded, with
     * one of its overloaded variants taking a string used to delimit two consecutive elements,
     * so you can obtain a comma-separated list of the dishes’ names with , (comma)
     * */
    @Test
    void must_convert_a_stream_in_a_single_string_separated_by_comma(){
        String shortMenu = menu.stream().map(Dish::getName).collect(joining(", "));
        Assertions.assertEquals("pork, beef, chicken, french fries, rice, season fruit, pizza, prawns, salmon",shortMenu);
    }

    /**
     * Once again, this demonstrates how functional programming in general (and the new
     * API based on functional-style principles added to the Collections framework in Java 8
     * in particular) often provides multiple ways to perform the same operation. This exam-
     * ple also shows that collectors are somewhat more complex to use than the methods
     * directly available on the Streams interface, but in exchange they offer higher levels of
     * abstraction and generalization and are more reusable and customizable.
     * Our suggestion is to explore the largest number of solutions possible to the prob-
     * lem at hand, but always choose the most specialized one that’s general enough to
     * solve it. This is often the best decision for both readability and performance reasons.
     * For instance, to calculate the total calories in our menu, we’d prefer the last solution
     * (using IntStream ) because it’s the most concise and likely also the most readable one.
     * */
    @Test
    void must_sum_using_the_reduce_collector(){
        int totalCalories = menu.stream().collect(reducing(
                0, Dish::getCalories, (i, j) -> i + j));

        Assertions.assertEquals(4200, totalCalories);
    }

    @Test
    void must_group_dishs_by_type(){
        Map<Type, List<Dish>> dishesByType =
                menu.stream().collect(groupingBy(Dish::getType));

        Assertions.assertEquals(3, dishesByType.size());
    }

    @Test
    void must_group_dishs_by_caloriclevel(){
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(
             groupingBy(dish -> {
                  if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                  else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                  else return CaloricLevel.FAT;
        }));

        Assertions.assertEquals(4, dishesByCaloricLevel.get(CaloricLevel.DIET).size());
        Assertions.assertEquals(4, dishesByCaloricLevel.get(CaloricLevel.NORMAL).size());
        Assertions.assertEquals(1, dishesByCaloricLevel.get(CaloricLevel.FAT).size());
    }

    @Test
    void must_filter_elements_in_a_map(){
        Map<Type, List<Dish>> caloricDishesByType =
                menu.stream()
                    .collect(
                        groupingBy(Dish::getType,
                        filtering(dish -> dish.getCalories() > 500, toList())) // filters the elements after grouping and create a list with the result to each key
                    );

        Assertions.assertEquals(2, caloricDishesByType.get(Type.MEAT).size());
        Assertions.assertEquals(2, caloricDishesByType.get(Type.OTHER).size());
        Assertions.assertEquals(0, caloricDishesByType.get(Type.FISH).size());
        Assertions.assertTrue(caloricDishesByType.containsKey(Type.FISH));

    }

    @Test
    void must_mapping_elements_in_a_map(){
        Map<Type, List<String>> dishNamesByType =
                menu.stream()
                        .collect(groupingBy(Dish::getType,
                                mapping(Dish::getName, toList())));
    }

    @Test
    void must_flatten_elements_in_a_map(){
        Map<String, List<String>> dishTags = new HashMap<>();
        dishTags.put("pork", asList("greasy", "salty"));
        dishTags.put("beef", asList("salty", "roasted"));
        dishTags.put("chicken", asList("fried", "crisp"));
        dishTags.put("french fries", asList("greasy", "fried"));
        dishTags.put("rice", asList("light", "natural"));
        dishTags.put("season fruit", asList("fresh", "natural"));
        dishTags.put("pizza", asList("tasty", "salty"));
        dishTags.put("prawns", asList("tasty", "roasted"));
        dishTags.put("salmon", asList("delicious", "fresh"));

        Map<Type, Set<String>> dishNamesByType =
                menu.stream()
                        .collect(groupingBy(Dish::getType,
                                flatMapping(dish -> dishTags.get( dish.getName() ).stream(),
                                        toSet())));
    }

    @Test
    void must_group_elements_by_type_and_calories(){
        Map<Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream().collect(
                groupingBy(
                    Dish::getType,
                    groupingBy(Dish::getCaloricLevel)
                )
        );
    }


    @Test
    void must_group_elements_and_count_the_quantity_of_elements_in_a_subgroup(){
        Map<Type, Long> typesCount = menu.stream().collect(
                groupingBy(Dish::getType, counting()));

        Assertions.assertEquals(3, typesCount.get(Type.MEAT));
        Assertions.assertEquals(2, typesCount.get(Type.FISH));
        Assertions.assertEquals(4, typesCount.get(Type.OTHER));
    }

    /**
     * of the collector generated by the maxBy factory method, but in reality if
     * there’s no Dish in the menu for a given type, that type won’t have an
     * Optional.empty() as value; it won’t be present at all as a key in the Map . The
     * groupingBy collector lazily adds a new key in the grouping Map only the first
     * time it finds an element in the stream, producing that key when applying on
     * it the grouping criteria being used. This means that in this case, the Optional
     * wrapper isn’t useful, because it’s not modeling a value that could be possibly
     * absent but is there incidentally, only because this is the type returned by the
     * reducing collector.
     * */
    @Test
    void must_finding_the_highest_calorie_dish_in_each_subgroup(){
        Map<Type, Optional<Dish>> mostCaloricByType =
                menu.stream()
                        .collect(groupingBy(Dish::getType,
                                maxBy(comparingInt(Dish::getCalories))));

        Assertions.assertEquals("Optional[Dish{name='salmon'}]", mostCaloricByType.get(Type.FISH).toString());
        Assertions.assertEquals("Optional[Dish{name='pizza'}]", mostCaloricByType.get(Type.OTHER).toString());
        Assertions.assertEquals("Optional[Dish{name='pork'}]", mostCaloricByType.get(Type.MEAT).toString());
    }


    /**
     * The collectingAndThen method takes two arguments—the collector to be adapted and a transfor-
     * mation function—and returns another collector. This additional collector acts as a
     * wrapper for the old one and maps the value it returns using the transformation func-
     * tion as the last step of the collect operation. In this case, the wrapped collector is the
     * one created with maxBy , and the transformation function, Optional::get , extracts
     * the value contained in the Optional returned. As we’ve said, here this is safe
     * because the reducing collector will never return an Optional.empty(). The result is
     * the following Map :
     *
     * */
    @Test
    void must_finding_the_highest_calorie_dish_in_each_subgroup_without_optional(){
        Map<Type, Dish> mostCaloricByType = menu.stream()
                .collect(groupingBy(Dish::getType,
                        collectingAndThen(
                                maxBy(comparingInt(Dish::getCalories)),
                                Optional::get
                        )));

        Assertions.assertEquals("Dish{name='salmon'}", mostCaloricByType.get(Type.FISH).toString());
        Assertions.assertEquals("Dish{name='pizza'}", mostCaloricByType.get(Type.OTHER).toString());
        Assertions.assertEquals("Dish{name='pork'}", mostCaloricByType.get(Type.MEAT).toString());
    }

    @Test
    void must_sum_calories_by_dish_type(){
        Map<Type, Integer> totalCaloriesByType =
                menu.stream().collect(groupingBy(Dish::getType,
                        summingInt(Dish::getCalories)));

        Assertions.assertEquals(750,totalCaloriesByType.get(Type.FISH));
        Assertions.assertEquals(1900,totalCaloriesByType.get(Type.MEAT));
        Assertions.assertEquals(1550,totalCaloriesByType.get(Type.OTHER));
    }

    /**
     * Partitioning is a special case of grouping: having a predicate called a partitioning func-
     * tion as a classification function. The fact that the partitioning function returns a bool-
     * ean means the resulting grouping Map will have a Boolean as a key type, and therefore,
     * there can be at most two different groups—one for true and one for false .
     * You may be interested in partitioning the menu into vegetarian and nonvegetar-
     * ian dishes.
     * */
    @Test
    void must_partitioning_a_menu_in_vegetariam_and_nonvegetariam_dishes(){
        Map<Boolean, List<Dish>> partitionedMenu = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian));

        Assertions.assertEquals(4,partitionedMenu.get(true).size());
        Assertions.assertEquals(5,partitionedMenu.get(false).size());
    }

    @Test
    void must_partitioning_a_menu_in_vegetariam_and_nonvegetariam_dishes_and_after_grouping_the_dishes_by_type(){
        Map<Boolean, Map<Type, List<Dish>>> vegetarianDishesByType = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian,
                             groupingBy(Dish::getType)
                        )
                );

        Assertions.assertEquals(
              "{false={FISH=[Dish{name='prawns'}, Dish{name='salmon'}], MEAT=[Dish{name='pork'}," +
                       " Dish{name='beef'}, Dish{name='chicken'}]}, true={OTHER=[Dish{name='french fries'}," +
                       " Dish{name='rice'}, Dish{name='season fruit'}, " +
                       "Dish{name='pizza'}]}}", vegetarianDishesByType.toString());
    }

    @Test
    void must_partitioning_a_menu_in_vegetariam_and_nonvegetariam_dishes_and_after_get_most_caloric_dish_in_both_menus(){
        Map<Boolean, Dish> mostCaloricPartitionedByVegetarian = menu.stream()
                .collect(
                        partitioningBy(Dish::isVegetarian,
                                collectingAndThen(maxBy(comparingInt(Dish::getCalories)), Optional::get)
                        )
                );

        Assertions.assertEquals("{false=Dish{name='pork'}, true=Dish{name='pizza'}}", mostCaloricPartitionedByVegetarian.toString());
    }

    @Test
    void must_test_if_number_is_prime_or_not_with_custom_collector(){
        Map<Boolean, List<Integer>> listPartitionedByNumberPrimes = IntStream.rangeClosed(2, 10).boxed()
                .collect(new PrimeNumbersCollector());

        System.out.println(listPartitionedByNumberPrimes);
        Assertions.assertEquals("{false=[4, 6, 8, 9, 10], true=[2, 3, 5, 7]}", listPartitionedByNumberPrimes.toString());

    }
}
