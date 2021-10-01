package com.study.reactiveprgramming.modernjava.functional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;


public class RecursionTest {
    @Test
    void 팩토리얼_스트림(){
        System.out.println(IntStream.rangeClosed(1,5)
                .reduce(1,(left, right) -> left*right));
    }

    @Test
    void 팩토리얼_외부반복(){
        int n=5;

        long r=1;
        for(int i=2;i<=n;i++){
            r*=i;
        }

        System.out.println(r);
    }

    @Test
    void 팩토리얼_재귀(){
        System.out.println(factorial(30));
    }

    private long factorial(int i) {
        return i==1 ? 1 : i*factorial(i-1);
    }

    @Test
    void 팩토리얼_재귀_꼬리최적화(){
        System.out.println(factorialTailRecursive(30));
    }

    static long factorialTailRecursive(int n){
        return factorial_tail(1,n);
    }

    private static long factorial_tail(long accumulator,int i){
        return i==1 ? accumulator : factorial_tail(accumulator*i,--i);
    }
}
