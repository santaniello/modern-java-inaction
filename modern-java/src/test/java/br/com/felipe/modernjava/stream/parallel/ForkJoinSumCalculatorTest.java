package br.com.felipe.modernjava.stream.parallel;

import br.com.felipe.modernjava.stream.parallel.forkjoin.ForkJoinSumCalculator;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;


public class ForkJoinSumCalculatorTest  {
    @Test
    void must_execute_in_parallel(){
        long[] numbers = LongStream.rangeClosed(1, 20).toArray();
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
        Long result = new ForkJoinPool().invoke(task);
        System.out.println(result);
    }
}
