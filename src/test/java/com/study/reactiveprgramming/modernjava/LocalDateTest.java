package com.study.reactiveprgramming.modernjava;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class LocalDateTest {
    @Test
    void test() {
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(3);



        assertFalse(now == localDate);
    }

    @Test
    void test2() {
        LocalDateTime startDateTime = LocalDateTime.now().minus(181, ChronoUnit.SECONDS);
        LocalDateTime now = LocalDateTime.now();

        long between = ChronoUnit.MINUTES.between(startDateTime, LocalDateTime.now());
        System.out.println(between);

    }
}
