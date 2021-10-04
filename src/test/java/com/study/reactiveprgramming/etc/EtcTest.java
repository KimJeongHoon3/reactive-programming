package com.study.reactiveprgramming.etc;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EtcTest {
    List<Integer> list=new ArrayList<>();

    @Test
    void test(){
        //3
        //2 1
        //1 1 1


        // 8
        // 7 1
        // 6 2
        // 6 1 1
        // 5 3
        // 5 2 1
        // 5 1 1 1
        // 4 4
        // 4 3 1
        // 4 2 1 1
        // 4 1 1 1 1

        int i=8;

        divide(3);
        System.out.println(list);
    }

    private void divide(int i) {
        list.add(i);
        System.out.println(list);

        list.add(--i);
        int temp=0;
        temp++;
        list.add(temp);
        System.out.println(list);

        CompletableFuture<String> f;

        list.add(--i);
        temp=0;
        temp++;
        list.add(temp);
        list.add(temp);
        System.out.println(list);
    }
}
