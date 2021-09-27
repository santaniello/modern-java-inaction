package br.com.felipe.modernjava.stream.parallel;

import br.com.felipe.modernjava.stream.parallel.spliterator.WordCounter;
import br.com.felipe.modernjava.stream.parallel.spliterator.WordCounterSpliterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WordCounterSpliteratorTest {
    @Test
    void must_count_words_in_parallel(){
        final String SENTENCE =
        " Nel mezzo del cammin di nostra vita " +
        "mi ritrovai in una selva oscura" +
        " ché la dritta via era smarrita ";

        Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);
        Stream<Character> stream = StreamSupport.stream(spliterator, true);
        Assertions.assertEquals(19,  countWords(stream));
    }

    private int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
                WordCounter::accumulate,
                WordCounter::combine);
        return wordCounter.getCounter();
    }
}
