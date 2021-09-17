package com.study.reactiveprgramming.modernjava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

public class OptionalTest {
    @Test
    void test_optional(){
        Car car=null;
        Optional<Car> optCar=Optional.ofNullable(car);
        Car car2=optCar.orElse(new Car());
        Assertions.assertNotNull(car2);
    }

    @Test
    void test_optional2(){
        Insurance insurance=new Insurance();
        insurance.name="이름";

        Car car=new Car();
        car.insurance=Optional.ofNullable(insurance);

        Person person=new Person();
        person.car=Optional.empty();

        String result=Optional.ofNullable(person)
                .flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("UnKnown");

        System.out.println(result);
    }

    static class Person{
        Optional<Car> car;

        public Optional<Car> getCar() {
            return car;
        }
    }

    static class Car{
        Optional<Insurance> insurance;

        public Optional<Insurance> getInsurance() {
            return insurance;
        }
    }

    static class Insurance{
        String name;

        public String getName() {
            return name;
        }
    }
}
