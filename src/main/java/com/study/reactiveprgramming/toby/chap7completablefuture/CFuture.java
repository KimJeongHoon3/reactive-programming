package com.study.reactiveprgramming.toby.chap7completablefuture;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CFuture {
    //CompletableFuture : 이름에서도 알수있듯이 비동기작업을 완료하는 작업을 수행가능
    //  - 비동기작업의 결과를 담고있는것이지, 비동기작업 자체는 아님..
    /*
        예외처리는?
            1. 예외일어나는걸 끝까지 계속넘김
            2. 의미있는값으로 변환해서 넘김

     */
    //CompletionStage : 하나의 비동기 작업 수행하고, 완료햇을때 여기에 의존적인 또다른 작업을 할수있또록 해주는 인터페이스(그런 메소드들을 제공해줌 ex. thenXXX)
        // 결과에 의존할수도있고, 각각 비동기작업하고 합치는것 등 모두 가능
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        CompletableFuture<Integer> f=CompletableFuture.completedFuture(1); //값을 넣어서 완료한놈을 넘겨줄수도있음
//        System.out.println(f.get());

//        CompletableFuture.runAsync(()->log.info("runAsync"))
//                .thenRun(()-> log.info("thenRun1"))
//                .thenRun(()-> log.info("thenRun2"));
        CompletableFuture.supplyAsync(()->{
                    log.info("runAsync");
                    if(1==1) throw new RuntimeException();
                    return 1;
                })
                .exceptionally(e->5) //바로 위에서 에러발생하면 5로 바꿔줌
                .thenCompose(s1-> { //flatmap과 유사..
                    log.info("thenApply1 {}",s1);
                    return CompletableFuture.completedFuture(s1+1);
                })
                .thenApply(s2-> { //map과 유사
                    log.info("thenApply2 {}",s2);
                    return s2*3;
                })
                .exceptionally(e -> -10 ) //에러나면 -10으로 변경시켜줌.. 만약 thenApply1에서 에러났으면 thneApply2는 넘어가고 여기서 예외를 -10으로 변경해준
                .thenAccept(s3-> {
                    log.info("thenAccept {}",s3);
                });

        log.info("exit");

        ForkJoinPool.commonPool().shutdown();
        ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);
    }
}
