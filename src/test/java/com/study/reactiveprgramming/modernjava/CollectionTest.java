package com.study.reactiveprgramming.modernjava;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class CollectionTest {
//    @Test
//    void 맵생성_java9(){
//        Map<String,String> map=Map.ofEntries(Map.entry("key","value"),
//                Map.entry("key2","value2"),
//                Map.entry("key3","value3"),
//                Map.entry("key3","values3"));
//
//        System.out.println(map);
//    }

    @Test
    void test_removeIf() {
        List<String> list = new ArrayList<>(Arrays.asList("hi", "hello", "hello"));
//        for(int i=0;i<list.size();i++){
//            String str=list.get(i);
//            if(str.equals("hello")){
//                list.remove(i);
//            }
//        }

        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            String str = iterator.next();
            if (str.equals("hello")) {
                iterator.remove();
            }
        }
//        list.removeIf(str->str.equals("hi"));

//        for(String s:list){
//            if(s.equals("hi")){
//                list.remove(s);
//            }
//        }
        System.out.println(list);
    }

    @Test
    void map_sorted() {
        Map<String, String> map = new HashMap<String, String>() {{
            put("Rapheal", "Star Wars");
            put("Cristina", "Matrix");
            put("Olivia", "James Bond");
        }};
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(System.out::println);

    }

    @Test
    void map_merge() {
        Map<String, String> map1 = new HashMap<String, String>() {{
            put("Rapheal", "Star Wars");
            put("Cristina", "Matrix");
            put("Olivia", "James Bond");
        }};
        Map<String, String> map2 = new HashMap<String, String>() {{
            put("Kim", "Star Wars");
            put("Cristina", "Matrix2");
            put("Park", "James Bond");
        }};
        map1.forEach((k, v) -> map2.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));

        System.out.println(map2);

    }

    @Test
    void map_remove() {
        Map<String, Integer> movies = new HashMap<>();
        movies.put("JamesBond", 20);
        movies.put("Matrix", 15);
        movies.put("Harry Potter", 5);

        movies.entrySet().removeIf((e) -> e.getValue() < 10); //응용좀하자..

        System.out.println(movies);
    }

    @Test
    void map_reduce() {
        ConcurrentHashMap<String, Integer> movies = new ConcurrentHashMap<>();
        movies.put("JamesBond", 20);
        movies.put("Matrix", 15);
        movies.put("Harry Potter", 5);
        movies.put("Harry Potter2", 1000);

        Integer maxVal = movies.reduceValues(1, Integer::max);
//        int maxVal=movies.reduceValuesToInt(1, f->f,0,Integer::max);
        System.out.println(maxVal);

    }

    @Test
    void stream_하나를_여러번사용() {
        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4).stream();

        Stream<Integer> integerStream = stream.filter(i -> i % 2 == 0);
        integerStream
                .forEach(i -> System.out.println("짝 : " + i));

        Stream<Integer> integerStream1 = stream
                .filter(i -> i % 2 == 1);
        integerStream1
                .forEach(i -> System.out.println("홀 : " + i));
    }

}
