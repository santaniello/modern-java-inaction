package br.com.felipe.modernjava.stream.parallel.spliterator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class WordCounter {
    @Getter
    private final int counter;
    private final boolean lastSpace;

    public WordCounter accumulate(Character c){
        if(Character.isWhitespace(c)){
            return  lastSpace ?
                    this :
                    new WordCounter(counter,true);
        }else{
            return  lastSpace ?
                    new WordCounter(counter+1, false) :
                    this;
        }
    }

    public WordCounter combine(WordCounter wordCounter){
        return  new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
    }
}
