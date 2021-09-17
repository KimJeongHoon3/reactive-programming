package com.study.reactiveprgramming.modernjava;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparing;


public class Lambda {

    public static void main(String[] args) {
        List<Apple> inventory=new ArrayList<Apple>(){{add(new Apple(10));add(new Apple(5));add(new Apple(50));}};

        inventory.sort(comparing(Apple::getWeight).reversed());

        System.out.println(inventory);
    }

//    private static <T,R extends Comparable<? super R>> Comparator<T> comparing(Function<? super T,? extends R> function) {
//        return (o1, o2) -> function.apply(o1).compareTo(function.apply(o2));
//    }

    private static class Apple{
        int weight;
        public Apple(int weight) {
            this.weight = weight;
        }

        int getWeight(){
            return weight;
        }

        @Override
        public String toString() {
            return "Apple{" +
                    "weight=" + weight +
                    '}';
        }
    }

    private static class Fruit<T> {

    }
}
