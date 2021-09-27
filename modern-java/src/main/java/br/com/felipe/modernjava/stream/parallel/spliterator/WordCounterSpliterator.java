package br.com.felipe.modernjava.stream.parallel.spliterator;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Spliterator s define how a parallel stream can split the data it traverses.
 * The Spliterator is another new interface added to Java 8; its name stands for “split-
 * able iterator.” Like Iterator s, Spliterator s are used to traverse the elements of a
 * source, but they’re also designed to do this in parallel.
 *
 * To implement this, we need to implement Spliterator<T> interface
 * */
public class WordCounterSpliterator implements Spliterator<Character> {
    private final String string;
    private int currentChar = 0;

    public WordCounterSpliterator(String string) {
        this.string = string;
    }

    /**
     * The tryAdvance method feeds the Consumer with the Character in the String
     * at the current index position and increments this position. The Consumer passed
     * as its argument is an internal Java class forwarding the consumed Character to
     * the set of functions that have to be applied to it while traversing the stream,
     * which in this case is only a reducing function, namely, the accumulate method of
     * the WordCounter class. The tryAdvance method returns true if the new cursor
     * position is less than the total String length and there are further Character s to
     * be iterated.
     * */
    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(string.charAt(currentChar++));
        return currentChar < string.length();
    }

    /**
     * The trySplit method is the most important one in a Spliterator , because it’s
     * the one defining the logic used to split the data structure to be iterated. As you
     * did in the compute method of the RecursiveTask implemented in listing 7.1,
     * the first thing you have to do here is set a limit under which you don’t want to
     * perform further splits. Here, you use a low limit of 10 Character s only to make
     * sure that your program will perform some splits with the relatively short String
     * you’re parsing. But in real-world applications you’ll have to use a higher limit,
     * as you did in the fork/join example, to avoid creating too many tasks. If the
     * number of remaining Character s to be traversed is under this limit, you return
     * null to signal that no further split is necessary. Conversely, if you need to per-
     * form a split, you set the candidate split position to the half of the String chunk
     * remaining to be parsed. But you don’t use this split position directly because
     * you want to avoid splitting in the middle of a word, so you move forward until
     * you find a blank Character . Once you find an opportune split position, you cre-
     * ate a new Spliterator that will traverse the substring chunk going from the
     * current position to the split one; you set the current position of this to the split
     * one, because the part before it will be managed by the new Spliterator , and
     * then you return it.
     *
     * */
    @Override
    public Spliterator<Character> trySplit() {
        int currentSize = string.length() - currentChar;
        if(currentSize < 10)
           return  null;

        for(int splitPos = currentSize / 2 + currentChar;splitPos < string.length(); splitPos++) {
            if(Character.isWhitespace(string.charAt(splitPos))){
                Spliterator<Character> spliterator = new WordCounterSpliterator(string.substring(currentChar, splitPos));

                currentChar = splitPos;
                return  spliterator;
            }
        }

        return null;
    }

    /**
     * The estimatedSize of elements still to be traversed is the difference between
     * the total length of the String parsed by this Spliterator and the position cur-
     * rently iterated.
     * */
    @Override
    public long estimateSize() {
        return string.length() - currentChar;
    }

    /**
     * Finally, the characteristics method signals to the framework that this
     * Spliterator is ORDERED (the order is the sequence of Character s in the
     * String ), SIZED (the value returned by the estimatedSize method is exact),
     * SUBSIZED (the other Spliterator s created by the trySplit method also have
     * an exact size), NON-NULL (there can be no null Character s in the String ), and
     * IMMUTABLE (no further Character s can be added while parsing the String
     * because the String itself is an immutable class).
     *
     * */
    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}
