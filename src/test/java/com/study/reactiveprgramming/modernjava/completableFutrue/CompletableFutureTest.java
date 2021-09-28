package com.study.reactiveprgramming.modernjava.completableFutrue;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest {
    @Test
    void t(){
        CompletableFuture<String> completableFuture=new CompletableFuture<>();
        System.out.println(completableFuture.getNow("없으면이거"));
        completableFuture.complete("hi");
        System.out.println(completableFuture.getNow("없으면이거"));
    }


    @Test
    void testThenCombine() throws ExecutionException, InterruptedException {
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        int x=1337;

        CompletableFuture<Integer> a=new CompletableFuture<>();
        CompletableFuture<Integer> b=new CompletableFuture<>();
        CompletableFuture<Integer> c = a.thenCombine(b, (y, z) -> {
            System.out.println(Thread.currentThread()+" - a+b");
            return y + z;
        });

        executorService.submit(()->{
            System.out.println(Thread.currentThread()+" - a");
            a.complete(5);
        });

        executorService.submit(()->{
            System.out.println(Thread.currentThread()+" - b");
            b.complete(10);
        });

        System.out.println(Thread.currentThread()+" - c : "+c.get());
    }

}
