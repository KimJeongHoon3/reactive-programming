package com.study.reactiveprgramming.modernjava;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    void test_flatmap() {
        String[] strArr = {"Hello", "World", "hihi", "hill", "hell"};

        Flux.fromArray(strArr)
//                .concatMap(s -> Flux.fromArray(s.split("")).subscribeOn(Schedulers.boundedElastic()))
                .flatMap(s -> Flux.fromArray(s.split("")).subscribeOn(Schedulers.boundedElastic()))
                .subscribe(System.out::println);


//        Arrays.stream(strArr)
//                .flatMap(s -> Arrays.stream(s.split("")))
//                .forEach(System.out::println);
//        Map<String, Long> collect = Arrays.stream(strArr)
//                .flatMap(str -> Arrays.stream(str.split("").clone()))
////                .distinct()
//                .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
//        System.out.println(collect);
    }

    @Test
    void test_flatmap2() {
        String[] strArr = {"Hello", "World"};

        Map<String, Long> collect = Arrays.stream(strArr)
                .flatMap(arr -> Arrays.stream(arr.split("")))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(collect);

    }

    @Test
    void p166_2번_3번() {
        // 두개의 숫자 리스트가 있을때 모든 숫장 쌍의 리스트를 반환
        // [1,2,3], [3,4] => [(1,3),(1,4),(2,3),(2,4),(3,3),(3,4)]
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(3, 4);

        List<int[]> pairList = list1.stream()
                .flatMap(i -> list2.stream().filter(j -> (i + j) % 3 == 0).map(j -> new int[]{i, j}))
                .collect(Collectors.toList());

        for (int[] pair : pairList) {
            System.out.println(Arrays.toString(pair));
        }
    }

    @Test
    void 합집합() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(3, 4);

        List<int[]> collect = list1.stream()
                .flatMap(a -> list2.stream().map(b -> new int[]{a, b}))
                .collect(Collectors.toList());

        collect.forEach(intArr -> System.out.println(Arrays.toString(intArr)));
    }

    @Test
    void p177_스트림_실전연습() {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        //1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순
        transactions.stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .forEach(System.out::println);

        System.out.println("-----------------");

        //2. 거래자가 근무하는 모든 도시를 중복없이 나열
        transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getCity)
                .distinct()
                .forEach(System.out::println);

        System.out.println("-----------------");
        //3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬
        transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .forEach(System.out::println);

        System.out.println("-----------------");

        //4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환
        transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getName)
                .distinct()
                .sorted()
                .forEach(System.out::println);

        System.out.println("-----------------");

        //5. 밀라노에 거래자가 있는가?
        boolean val = transactions.stream()
                .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));

        System.out.println("5 : " + val);

        System.out.println("-----------------");
        //6. 케임브리지에 거주하는 거래자의 모든트랜잭션 값을 출력
        List<Integer> list = transactions.stream()
                .filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .collect(Collectors.toList());
        System.out.println(list);

        System.out.println("-----------------");

        //7. 전체 트랜잭션중 최댓값?
        transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max)
                .ifPresent(System.out::println);



        System.out.println("-----------------");
        //8. 전체 트랜잭션중 최솟값?
        transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::min)
                .ifPresent(System.out::println);

        Stream<String> stringStream = Stream.of("hi", "heu", "aa");

        Optional<Transaction> reduce = transactions.stream()
                .reduce(BinaryOperator.maxBy(Comparator.comparingInt(Transaction::getValue)));

        Map<Trader, Optional<Transaction>> collect = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getTrader, () -> new TreeMap<>(Comparator.comparing(Trader::getName).reversed()) , Collectors.maxBy(Comparator.comparingInt(Transaction::getValue))));
        System.out.println("collect : " + collect);

        Optional<Transaction> collect1 = transactions.stream()
                .collect(Collectors.maxBy(Comparator.comparingInt(Transaction::getValue)));

        Optional<Transaction> collect2 = transactions.stream()
                .collect(Collectors.reducing(BinaryOperator.maxBy(Comparator.comparingInt(Transaction::getValue))));

        Map<Trader, Integer> collect3 = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getTrader, Collectors.mapping(Transaction::getValue, Collectors.summingInt(Integer::intValue))));
        System.out.println("collect3 : "+collect3);
        collect3 = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getTrader, Collectors.summingInt(Transaction::getValue)));
        System.out.println("collect3 : "+collect3);

    }


    private static class Trader {
        private final String name;
        private final String city;

        public Trader(String name, String city) {
            this.name = name;
            this.city = city;
        }

        public String getName() {
            return name;
        }

        public String getCity() {
            return city;
        }

        @Override
        public String toString() {
            return "Trader{" +
                    "name='" + name + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }

    private static class Transaction {
        private final Trader trader;
        private final int year;
        private final int value;

        public Transaction(Trader trader, int year, int value) {
            this.trader = trader;
            this.year = year;
            this.value = value;
        }

        public Trader getTrader() {
            return trader;
        }

        public int getYear() {
            return year;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "trader=" + trader +
                    ", year=" + year +
                    ", value=" + value +
                    '}';
        }
    }

    @Test
    void 피타고라스() {
        //100 이하의 값으로 피타고라스 만들수 있는 값들은?
        List<double[]> list = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a ->
                        IntStream.rangeClosed(a, 100)
                                .mapToObj(b -> new double[]{a, b, Math.sqrt((a * a + b * b))})
                                .filter(arr -> arr[2] % 1 == 0)
                ).collect(Collectors.toList());

        for (double[] intArr : list) {
            System.out.println(Arrays.toString(intArr));
        }
    }

    @Test
    void 피타() {
        List<double[]> collect = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100).mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)}))
                .filter(dArr -> dArr[2] % 1 == 0 && dArr[2] <= 100)
                .collect(Collectors.toList());

        for (double[] dArr : collect) {
            System.out.println(Arrays.toString(dArr));
        }

    }

    @Test
    void 피보나치() {
        Stream.iterate(new int[]{0, 1}
                        , t -> new int[]{t[1], t[0] + t[1]}) //다음값에 사용하기위해 저장이 필요
                .limit(20)
                .forEach(intArr -> System.out.println(Arrays.toString(intArr))
//                .forEach(intArr -> System.out.println(intArr[0])
                );
    }

    @Test
    void 피보() {
        List<int[]> collect = Stream.iterate(new int[]{1, 1},
                        i -> new int[]{i[1], i[0] + i[1]})
                .limit(20)
                .collect(Collectors.toList());

        for (int[] intArr : collect) {
            System.out.println(intArr[0]);
        }
//        System.out.println(pibo(10));
//        Stream

    }

    private int pibo(int i) {
        if (i == 1) {
            return 1;
        }

        return i + pibo(i - 1);
    }

    @Test
    void n을_1_2_3_으로구성() {
//        IntStream.rangeClosed(1,10)
//        IntStream.rangeClosed(1,10)
//                .filter(a -> a+b+c==10 )


        // f(1) 1
        // f(2) 1+1 2
        // f(3) 1+1+1 1+2 2+1 3
        // f(4) (1+1+1+1) (1+1+2) (1+2+1) (1+3) (2+1+1) (2+2) (3+1) => 3+2+1
        // f(5) => f(4) + f(3) + f(2)
        // f(6) => f(5) + f(4) + f(3)
        // f(7) => f(6) + f(5) + f(4)
    }

    @Test
    void n을_구성하는_모든자연수() {


    }


    @Test
    void testIntStream() {

        IntStream range = IntStream.range(1, 10);
        System.out.println(Stream.of("ab", "cd", "ef").reduce(new StringBuilder(), (builder, s) -> builder.append(s), StringBuilder::append));

        StringBuilder collect = Stream.of("ab", "cd", "ef").collect(StringBuilder::new, (container, s) -> container.append(s), StringBuilder::append);
        System.out.println(collect);


        Optional<Integer> collect1 = Stream.of(1, 2, 3, 5, 6).collect(Collectors.maxBy(Comparator.comparingInt(Integer::intValue)));
        System.out.println(collect1.get());

        ArrayList<String> list = new ArrayList<>();
        LinkedList<String> linkedList = new LinkedList<>();
//        linkedList.subList()

    }
}
