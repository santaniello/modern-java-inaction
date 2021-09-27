package br.com.felipe.modernjava.datetime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static br.com.felipe.modernjava.datetime.DateTimeUtility.*;

public class DateTimeTest {
    @Test
    void should_calculate_the_next_util_working_day(){
        LocalDate friday = LocalDate.of(2021, 9, 10);
        LocalDate saturday = LocalDate.of(2021, 9, 11);
        LocalDate sunday = LocalDate.of(2021, 9, 12);
        LocalDate monday = LocalDate.of(2021, 9, 13);

        Assertions.assertEquals(monday, nextWorkingDay(friday));
        Assertions.assertEquals(monday, nextWorkingDay(saturday));
        Assertions.assertEquals(monday, nextWorkingDay(sunday));
    }


}
