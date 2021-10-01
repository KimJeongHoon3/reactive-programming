package com.study.reactiveprgramming.modernjava.functional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SubsetMaker {

    static List<List<Integer>> subset(List<Integer> list){
        if(list.isEmpty()){
            List<List<Integer>> l=new ArrayList<>();
            l.add(Collections.emptyList());
            return l;
        }

        Integer first=list.get(0); //1 4
        List<Integer> rest=list.subList(1,list.size()); // 4,9  9

        List<List<Integer>> sub1=subset(rest);
        List<List<Integer>> sub2=insertAll(first,sub1);

        return concat(sub1,sub2);
    }

    private static List<List<Integer>> concat(List<List<Integer>> sub1, List<List<Integer>> sub2) {
        List<List<Integer>> newList=new ArrayList(sub1);
        newList.addAll(sub2);
        return newList;
    }

    private static List<List<Integer>> insertAll(Integer first, List<List<Integer>> sub1) {
        return sub1.stream()
                .map(list -> {
                    ArrayList<Integer> newList=new ArrayList(list);
                    newList.add(first);
                    return Arrays.asList(newList.toArray(new Integer[0]));
                })
                .collect(Collectors.toList());
    }
}
