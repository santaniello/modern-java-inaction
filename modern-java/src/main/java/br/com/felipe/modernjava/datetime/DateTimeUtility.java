package br.com.felipe.modernjava.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoUnit.DAYS;

public class DateTimeUtility {
    public static LocalDate nextWorkingDay(LocalDate date){
        return date.with(temporal -> {
            DayOfWeek dayOfWeek = DayOfWeek.of(temporal.get(DAY_OF_WEEK));
            if(dayOfWeek == DayOfWeek.FRIDAY)
                return temporal.plus(3, DAYS);

            if(dayOfWeek == DayOfWeek.SATURDAY)
                return temporal.plus(2, DAYS);

            return temporal.plus(1, DAYS);
        });
    }
}
