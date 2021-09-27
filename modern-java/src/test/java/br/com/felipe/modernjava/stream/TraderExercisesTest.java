package br.com.felipe.modernjava.stream;

import br.com.felipe.modernjava.stream.models.Trader;
import br.com.felipe.modernjava.stream.models.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

public class TraderExercisesTest {

    private List<Transaction> transactions = null;

    @BeforeEach
    void setUp() {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
        this.transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
    }

    @Test
    void must_find_all_transactions_in_the_year_2012_and_sort_them_by_value(){
        List<Transaction> transactions2012 = this.transactions.stream()
                .filter(t -> t.getYear() == 2012)
                .sorted(comparing(Transaction::getValue))
                .collect(Collectors.toList());

        Assertions.assertEquals(4, transactions2012.size());
        Assertions.assertEquals(700, transactions2012.get(0).getValue());
        Assertions.assertEquals(710, transactions2012.get(1).getValue());
        Assertions.assertEquals(950, transactions2012.get(2).getValue());
        Assertions.assertEquals(1000, transactions2012.get(3).getValue());
    }

    @Test
    void must_find_all_the_unique_cities_where_the_traders_work(){
        List<String> cities = this.transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct()
                .collect(Collectors.toList());

        Assertions.assertEquals(2, cities.size());
        Assertions.assertTrue(cities.contains("Cambridge"));
        Assertions.assertTrue(cities.contains("Milan"));
    }

    @Test
    void must_find_all_traders_from_cambridge_and_sort_them_by_name(){
        List<Trader> traders = this.transactions.stream()
                .map(Transaction::getTrader)
                .filter(t->t.getCity().equalsIgnoreCase("Cambridge"))
                .distinct()
                .sorted(comparing(Trader::getName))
                .collect(Collectors.toList());

        Assertions.assertEquals(3, traders.size());
        Assertions.assertEquals("Alan", traders.get(0).getName());
        Assertions.assertEquals("Brian", traders.get(1).getName());
        Assertions.assertEquals("Raoul", traders.get(2).getName());
    }

    @Test
    void must_return_a_string_of_all_traders_names_sorted_alphabetically(){
        String traderStr = this.transactions.stream()
                .map(t->t.getTrader().getName())
                .distinct()
                .sorted()
                .reduce("", (n1, n2) -> n1 + n2);
        System.out.println(traderStr);

        Assertions.assertEquals("AlanBrianMarioRaoul", traderStr);
    }

    @Test
    void are_any_traders_based_in_milan(){
        boolean milan = this.transactions.stream()
                .map(Transaction::getTrader)
                .anyMatch(t -> t.getCity().equalsIgnoreCase("Milan"));

        Assertions.assertTrue(milan);
    }

    @Test
    void print_the_values_of_all_transactions_from_the_traders_living_in_cambridge(){
       this.transactions.stream()
                .filter(t->t.getTrader().getCity().equalsIgnoreCase("Cambridge"))
                .map(Transaction::getValue)
                .forEach(System.out::println);
    }

    @Test
    void whats_the_highest_value_of_all_the_transactions(){
        Transaction transaction = this.transactions.stream()
                .max(comparing(Transaction::getValue))
                .get();

        Assertions.assertEquals(1000,transaction.getValue());
    }

    @Test
    void find_the_transaction_with_the_smallest_value(){
        Transaction transaction = this.transactions.stream()
                .min(comparing(Transaction::getValue))
                .get();

        Assertions.assertEquals(300,transaction.getValue());
    }

    @Test
    void must_group_a_list_of_transactions_by_their_currency(){
        Map<Trader, List<Transaction>> collect = this.transactions.stream()
                .collect(groupingBy(Transaction::getTrader));

        Assertions.assertEquals(4, collect.size());

    }
}
