package br.com.felipe.modernjava.stream.collectors;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;

/**
 * Custom Collector used to partition prime numbers and nonprime
 *
 * We need to implement the interface public interface Collector<T, A, R>
 * where T , A , and R are respectively the type of the elements in the stream, the type of
 * the object used to accumulate partial results, and the type of the final result of the
 * collect operation.
 * */
public class PrimeNumbersCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {

    /**
     * The supplier method has to return a function that when invoked creates
     * the accumulator.
     *
     * Here you’re not only creating the Map that you’ll use as the accumulator, but you’re
     * also initializing it with two empty lists under the true and false keys. This is where
     * you’ll add respectively the prime and nonprime numbers during the collection pro-
     * cess.
     *
     * */
    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {
        return () -> new HashMap<>() {{
            put(true,  new ArrayList<>());
            put(false, new ArrayList<>());
        }};
    }

    /**
     * The most important method of your collector is the accumulator method,
     * because it contains the logic defining how the elements of the stream have to be col-
     * lected. In this case, it’s also the key to implementing the optimization we described
     * previously. At any given iteration you can now access the partial result of the collection
     * process, which is the accumulator containing the prime numbers found so far
     * */
    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (acc, candidate) -> {
            List<Integer> primeNumbers = acc.get(true);
             acc.get(isPrime(primeNumbers,candidate))
                .add(candidate);
        };
    }

    /**
     * The next method has to combine two partial accumulators in the case of a parallel col-
     * lection process, so in this case it has to merge the two Map s by adding all the numbers
     * in the prime and nonprime lists of the second Map to the corresponding lists in the
     * first Map
     *
     * Note that in reality this collector can’t be used in parallel, because the algorithm is
     * inherently sequential. This means the combiner method won’t ever be invoked, and
     * you could leave its implementation empty (or better, throw an UnsupportedOperation-
     * Exception ). We decided to implement it anyway only for completeness.
     * */
    @Override
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (Map<Boolean, List<Integer>> map1,
                Map<Boolean, List<Integer>> map2) -> {
            map1.get(true).addAll(map2.get(true));
            map1.get(false).addAll(map2.get(false));
            return map1;
        };

    }

    /**
     * the accumulator coincides with the collector’s result so it won’t need any further transfor-
     * mation, and the finisher method returns the identity function
     * */
    @Override
    public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
        return Function.identity();
    }

    /**
     * As for the characteristic method, we already said that it’s neither CONCURRENT nor
     * UNORDERED but is IDENTITY_FINISH :
     * This collector is IDENTITY_FINISH but neither UNORDERED
     * nor CONCURRENT because it relies on the fact that prime
     * numbers are discovered in sequence
     * */
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH));
    }

    public static boolean isPrime(List<Integer> primes, int candidate){
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return primes.stream()
                .takeWhile(i -> i <= candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }
}
