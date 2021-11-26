package com.study.reactiveprgramming.modernjava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PartitionPrimeNumberAndNonPrimeNumberTest {
    @Test
    void testPrime(){
        Map<Boolean, List<Integer>> p=IntStream.rangeClosed(2,2_000_000)
                .boxed()
                .collect(Collectors.partitioningBy(candidate -> isPrime(candidate)));

//        System.out.println(p);
        System.out.println(p.get(true).size());
    }

    @Test
    void testPrime2(){
        List<Integer> p = IntStream.rangeClosed(2, 2_000_000)
                .boxed()
                .filter(i -> {
                    int sqrt = (int)Math.sqrt(i);
                    return IntStream.rangeClosed(2,sqrt)
                            .noneMatch(k -> i%k==0);
                }).collect(Collectors.toList());

        System.out.println(p.size());
//        for(Integer i:p){
//            System.out.println(i);
//        }

    }

    @Test
    void testPrime3(){
        List<Integer> p = IntStream.rangeClosed(2, 2_000_000)
                .boxed()
                .collect(new CustomPrimeCollector2());

        System.out.println(p.size());
//        for(Integer i:p){
//            System.out.println(i);
//        }

    }

    @Test
    void test_equal_primeCustom_prime(){
        Map<Boolean, List<Integer>> p1 = IntStream.rangeClosed(2, 2_000_000)
                .boxed()
                .collect(new CustomPrimeCollector());

        Map<Boolean, List<Integer>> p2=IntStream.rangeClosed(2,2_000_000)
                .boxed()
                .collect(Collectors.partitioningBy(candidate -> isPrime(candidate)));

        assertEquals(p1.keySet(),p2.keySet());
        assertTrue(p1.entrySet().stream()
                .allMatch(entry -> entry.getValue().equals(p2.get(entry.getKey()))));

    }

    @Test
    void testPrimeCustom(){
        Map<Boolean, List<Integer>> p = IntStream.rangeClosed(2, 2_000_000)
                .boxed()
                .collect(new CustomPrimeCollector());

//        System.out.println(p);

    }

    private boolean isPrime(Integer candidate) {
        int candidateRoot=(int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2,candidateRoot)

                .noneMatch(i->candidate%i==0);
    }

    private boolean isPrimeUpgrade(List<Integer> primeList,Integer candidate) {
        int candidateRoot=(int) Math.sqrt((double) candidate);
        /*return primeList.stream()
//                .filter(i -> i <= candidateRoot) //이렇게하면 i<=candidate 조건에 불일치해도 계속 돌긴함..
                .takeWhile(primeList,p -> p<=candidateRoot)
                .noneMatch(i->candidate%i==0);*/

        return takeWhile(primeList,p -> p<=candidateRoot)
                .stream()
                .noneMatch(i->candidate%i==0);
    }

    public static <T> List<T>  takeWhile(List<T> list, Predicate<T> predicate){
        int i=0;

        for(T t : list){
            if(!predicate.test(t)){
                return list.subList(0,i);
            }
            i++;
        }

        return list;
    }



    class CustomPrimeCollector implements Collector<Integer,Map<Boolean,List<Integer>>,Map<Boolean,List<Integer>>>{

        @Override
        public Supplier<Map<Boolean, List<Integer>>> supplier() {

            return () -> new HashMap<Boolean,List<Integer>>(){{put(true,new ArrayList<>());put(false,new ArrayList<>());}};
        }

        @Override
        public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
            return ((primeMap, integer) -> {
                List<Integer> primeList=primeMap.get(true);
                primeMap.get(isPrimeUpgrade(primeList,integer)).add(integer);
            });
        }

        @Override
        public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
            return null;
        }

        @Override
        public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
        }
    }


    static class CustomPrimeCollector2 implements Collector<Integer,List<Integer>,List<Integer>>{

        @Override
        public Supplier<List<Integer>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<Integer>, Integer> accumulator() {
            return (list,i) -> {
                if(isPrime(list,i)) list.add(i);
            };
        }

        @Override
        public BinaryOperator<List<Integer>> combiner() {
            return null;
        }

        @Override
        public Function<List<Integer>, List<Integer>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
        }

        <T> List<T> customTakeWhile(List<T> list,Predicate<T> p){
            for(int i=0;i<list.size();i++){
                if(!p.test(list.get(i))){
                    return list.subList(0,i);
                }
            }

            return list;
        }

        boolean isPrime(List<Integer> primeList,Integer candidate){
            int criteria=(int)Math.sqrt(candidate);
            return customTakeWhile(primeList,k -> k<=criteria)
                    .stream()
                    .noneMatch(i -> candidate%i==0);
        }
    }


}
