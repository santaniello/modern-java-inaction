package br.com.felipe.modernjava.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class CollectionEnhancementsTest {

    @Test
    void must_create_a_immutable_list_using_new_factory_method(){
        List<String> friends = List.of("Paulo","Joao","Maria");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            friends.add("Jasper");
        });
        Assertions.assertEquals(3, friends.size());
    }

    @Test
    void must_create_a_immutable_set_using_new_factory(){
        Set<String> friends = Set.of("Paulo","Joao","Maria");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            friends.add("Jasper");
        });
        Assertions.assertEquals(3, friends.size());
    }

    @Test
    void dont_must_create_a_immutable_set_using_new_factory_method_with_duplicated_element(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Set<String> friends = Set.of("Paulo","Joao","Paulo");
        });
    }

    @Test
    void must_create_a_immutable_map_using_new_factory_method_of(){
        Map<String, Integer> ageOfFriends = Map.of("Raphael",30,"Olivia",25,"Paulo",18);
        System.out.println(ageOfFriends);
        Assertions.assertEquals("{Olivia=25, Paulo=18, Raphael=30}",ageOfFriends.toString());
    }

    @Test
    void must_create_a_immutable_map_using_new_factory_method_ofEntries(){
        Map<String, Integer> ageOfFriends = Map.ofEntries(
             Map.entry("Raphael",30),
             Map.entry("Olivia",25),
             Map.entry("Paulo",18)
        );
        System.out.println(ageOfFriends);
        Assertions.assertEquals("{Olivia=25, Paulo=18, Raphael=30}",ageOfFriends.toString());
    }

    @Test
    void must_remove_an_element_in_a_list(){
        // creating a mutable list
        List<String> friends = new ArrayList<>();
        friends.add("Paulo");
        friends.add("Joao");
        friends.add("Maria");
        boolean removedPaulo = friends.removeIf(f -> f.equalsIgnoreCase("Paulo"));
        Assertions.assertTrue(removedPaulo);
        Assertions.assertEquals(2,friends.size());
    }

    @Test
    void must_replaceAll_an_element_in_a_list(){
        // creating a mutable list
        List<String> friends = new ArrayList<>();
        friends.add("Paulo");
        friends.add("Joao");
        friends.add("Maria");
        friends.replaceAll(f->f.toUpperCase());
        Assertions.assertEquals("[PAULO, JOAO, MARIA]",friends.toString());
    }

    @Test
    void must_iterating_a_map_using_foreach(){
        Map<String, Integer> ageOfFriends = Map.ofEntries(
                Map.entry("Raphael",30),
                Map.entry("Olivia",25),
                Map.entry("Paulo",18)
        );

        ageOfFriends.forEach((friend,age) -> {
            System.out.println("Name = " + friend);
            System.out.println("Age = " + age);
        });
    }

    @Test
    void must_sort_a_map_and_show_the_elemntes_in_an_ordered_way_by_the_key() {
        Map<String, String> favouriteMovies = Map.ofEntries(
            Map.entry("Raphael", "Star Wars"),
            Map.entry("Cristina", "Matrix"),
            Map.entry("Olivia", "James Bond")
        );

        favouriteMovies
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEachOrdered(System.out::println);
    }

    @Test
    void must_provide_a_default_value_when_the_key_dont_exist_in_a_map() {
        Map<String, String> favouriteMovies = Map.ofEntries(
                Map.entry("Raphael", "Star Wars"),
                Map.entry("Cristina", "Matrix"),
                Map.entry("Olivia", "James Bond")
        );

       Assertions.assertEquals("Matrix", favouriteMovies.getOrDefault("Cristina", "Unknown"));
       Assertions.assertEquals("Unknown", favouriteMovies.getOrDefault("Rodrigo", "Unknown"));
    }

    @Test
    void must_compute_a_value_when_the_key_is_absent_in_a_map() {
        Map<String, List<String>> friendsToMovies = new HashMap<>();
        //If the Key Raphael not exist in the map, it will be create the key with a ArrayList as Value and will be add a movie inside the list....
        friendsToMovies.computeIfAbsent("Raphael", name -> new ArrayList<>()).add("Star Wars");
        Assertions.assertEquals(1, friendsToMovies.size());
        Assertions.assertEquals(1, friendsToMovies.get("Raphael").size());
        Assertions.assertEquals("Star Wars", friendsToMovies.get("Raphael").get(0));

    }

    @Test
    void must_compute_a_value_when_the_key_is_null_in_a_map() {
        Map<String, List<String>> friendsToMovies = new HashMap<>();
        friendsToMovies.put("Raphael", null);
        //If the Key Raphael not exist in the map, it will be create the key with a new ArrayList as Value and will be add a movie inside the list....
        friendsToMovies.computeIfAbsent("Raphael", name -> new ArrayList<>()).add("Star Wars");
        Assertions.assertEquals(1, friendsToMovies.size());
        Assertions.assertEquals(1, friendsToMovies.get("Raphael").size());
        Assertions.assertEquals("Star Wars", friendsToMovies.get("Raphael").get(0));

    }

    @Test
    void dont_must_compute_a_new_value_when_the_key_is_not_absent_in_a_map() {
        List<String> raphaelMovies = new ArrayList<>();
        raphaelMovies.add("Spider Man");
        Map<String, List<String>> friendsToMovies = new HashMap<>();
        friendsToMovies.put("Raphael", raphaelMovies);
        //In this case Raphael key exist, so, it will ne add star wars movie inside the list that already exists (it will be not create a new list)
        friendsToMovies.computeIfAbsent("Raphael", name -> new ArrayList<>()).add("Star Wars");
        Assertions.assertEquals(1, friendsToMovies.size());
        Assertions.assertEquals(2, friendsToMovies.get("Raphael").size());
        Assertions.assertEquals("Spider Man", friendsToMovies.get("Raphael").get(0));
        Assertions.assertEquals("Star Wars", friendsToMovies.get("Raphael").get(1));

    }

    @Test
    void must_compute_a_new_value_when_the_key_is_present_in_a_map() {
        List<String> raphaelMovies = new ArrayList<>();
        raphaelMovies.add("Spider Man");
        Map<String, List<String>> friendsToMovies = new HashMap<>();
        friendsToMovies.put("Raphael", raphaelMovies);
        //In this case Raphael key exist, so, it will compute a new value (create a new list) and after, add a new movie inside the new list created
        friendsToMovies.computeIfPresent("Raphael", (key,val) -> new ArrayList<>()).add("Star Wars");
        Assertions.assertEquals(1, friendsToMovies.size());
        Assertions.assertEquals(1, friendsToMovies.get("Raphael").size());
        Assertions.assertEquals("Star Wars", friendsToMovies.get("Raphael").get(0));
    }

    @Test
    void must_remove_element_in_a_map_with_a_specific_key_and_value(){
        Map<String, String> favouriteMovies = new HashMap<>();
        favouriteMovies.put("Raphael", "Star Wars");
        favouriteMovies.put("Cristina", "Matrix");
        favouriteMovies.put("Olivia", "James Bond");

        favouriteMovies.remove("Raphael","Star Wars");
        Assertions.assertEquals(2, favouriteMovies.size());
    }

    @Test
    void must_replace_all_the_elements_in_a_map(){
        Map<String, String> favouriteMovies = new HashMap<>();
        favouriteMovies.put("Raphael", "Star Wars");
        favouriteMovies.put("Cristina", "Matrix");
        favouriteMovies.put("Olivia", "James Bond");

        favouriteMovies.replaceAll((key,value)->value.toUpperCase());
        System.out.println(favouriteMovies);
        Assertions.assertEquals("{Olivia=JAMES BOND, Raphael=STAR WARS, Cristina=MATRIX}", favouriteMovies.toString());
    }

    @Test
    void must_merge_duplicated_maps(){
        Map<String, String> friendsMovies = new HashMap<>();
        friendsMovies.put("Raphael", "Star Wars");
        friendsMovies.put("Cristina", "Matrix");
        friendsMovies.put("Olivia", "James Bond");

        Map<String, String> familyMovies = new HashMap<>();
        familyMovies.put("Paula", "Avengers");
        familyMovies.put("Cristina", "Spider Man");

        Map<String, String> everyoneMovies = new HashMap<>(familyMovies);

        friendsMovies.forEach((key,value)->{
            everyoneMovies.merge(key,value, (movie1,movie2) -> movie1 + " & " + movie2);
        });

        System.out.println(everyoneMovies.get("Cristina"));
        Assertions.assertEquals("Spider Man & Matrix", everyoneMovies.get("Cristina"));
        Assertions.assertEquals(4,everyoneMovies.size());
    }

    @Test
    void must_update_a_value_in_a_map_using_merge(){
        String movieName = "Spider Man";
        Map<String, Integer> moviesToCount = new HashMap<>();
        moviesToCount.put(movieName,1);
        // 1  (second argument)  is the default value if the key not exists
        moviesToCount.merge(movieName, 1, (key,value) -> value + 1 );
        Assertions.assertEquals(2, moviesToCount.get(movieName));
    }

    @Test
    void must_update_a_value_even_with_the_value_is_absent_using_merge(){
        String movieName = "Spider Man";
        Map<String, Integer> moviesToCount = new HashMap<>();
        moviesToCount.put(movieName,1);
        // 1 (second argument) is the default value if the key not exists
        moviesToCount.merge("Unknown", 1, (key,value) -> value + 1 );
        Assertions.assertEquals(1, moviesToCount.get("Unknown"));
    }

}
