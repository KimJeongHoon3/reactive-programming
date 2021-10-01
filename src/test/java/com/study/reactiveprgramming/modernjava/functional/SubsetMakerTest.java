package com.study.reactiveprgramming.modernjava.functional;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubsetMakerTest {

    @Test
    void testSubset(){
        List<Integer> list=Arrays.asList(1,4,9); // => 1을 제외한 (4,9)의 부분집합 + 1을 포함한 (4,9)의 부분집합
        System.out.println(SubsetMaker.subset(list));
    }
}