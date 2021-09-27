package br.com.felipe.modernjava.stream.parallel.forkjoin;

import java.util.concurrent.RecursiveTask;


/**
 * To use the fork/join framework, we need to extend the  RecursiveTask<T> class and implement the compute method
 *
 * Best practices for using the fork/join framework
 *
 * # Invoking the join method on a task blocks the caller until the result produced
 * by that task is ready. For this reason, it’s necessary to call it after the computa-
 * tion of both subtasks has been started. Otherwise, you’ll end up with a slower
 * and more complex version of your original sequential algorithm because every
 * subtask will have to wait for the other one to complete before starting.
 *
 * # The invoke method of a ForkJoinPool shouldn’t be used from within a
 * RecursiveTask . Instead, you should always call the methods compute or fork
 * directly; only sequential code should use invoke to begin parallel computation.
 *
 * # Calling the fork method on a subtask is the way to schedule it on the Fork-
 * JoinPool . It might seem natural to invoke it on both the left and right sub-
 * tasks, but this is less efficient than directly calling compute on one of them.
 * Doing this allows you to reuse the same thread for one of the two subtasks and
 * avoid the overhead caused by the unnecessary allocation of a further task on
 * the pool.
 *
 * # Debugging a parallel computation using the fork/join framework can be tricky.
 * In particular, it’s ordinarily quite common to browse a stack trace in your favor-
 * ite IDE to discover the cause of a problem, but this can’t work with a fork/join
 * computation because the call to compute occurs in a different thread than the
 * conceptual caller, which is the code that called fork .
 *
 * # As you’ve discovered with parallel streams, you should never take for granted
 * that a computation using the fork/join framework on a multicore processor is
 * faster than the sequential counterpart. We already said that a task should be
 * decomposable into several independent subtasks in order to be parallelizable
 * with a relevant performance gain. All of these subtasks should take longer to
 * execute than forking a new task; one idiom is to put I/O into one subtask and
 * computation into another, thereby overlapping computation with I/O. More-
 * over, you should consider other things when comparing the performance of
 * the sequential and parallel versions of the same algorithm. Like any other
 * Java code, the fork/join framework needs to be “warmed up,” or executed, a
 * few times before being optimized by the JIT compiler. This is why it’s always
 * important to run the program multiple times before to measure its perfor-
 * mance, as we did in our harness. Also be aware that optimizations built into
 * the compiler could unfairly give an advantage to the sequential version (for
 * example, by performing dead code analysis—removing a computation that’s
 * never used).
 **/
public class ForkJoinSumCalculator extends RecursiveTask<Long> {
    private final long[] numbers; // The array of numbers to be consumed
    private final int start; // Initial position of the subarray processed by this subtask
    private final int end; // // Final position of the subarray processed by this subtask
    public static final long THRESHOLD = 10; // The size threshold for splitting into subtasks

    // Public constructor to create the main task
    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers,0,numbers.length);
    }

    // Private constructor to create subtasks of the main task
    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        /**
         If the size is less than or equal to the threshold, computes
         the result sequentially
         **/
        if(length <= THRESHOLD)
           return computeSequentially();

        // Creates a subtask to sum the first half of the array
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2);
        // Asynchronously executes the newly created subtask using another thread of ForkJoinPool
        leftTask.fork();
        // Creates a subtask to sum the second half of array
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);
        // Execute the subtask synchronously, pottentialy allowing further recirsive splits.
        Long rightResult = rightTask.compute();
        // Reads the result of the first subtask - waiting if isn't ready.
        Long leftResult = leftTask.join();
        // Combine the results of the two tasks
        return leftResult + rightResult;
    }

    // A simple sequential algorithm for sizes below the Threshold
    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }
}
