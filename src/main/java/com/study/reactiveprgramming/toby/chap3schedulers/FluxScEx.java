package com.study.reactiveprgramming.toby.chap3schedulers;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
public class FluxScEx {
    public static void main(String[] args) throws InterruptedException {
//        Flux.range(1,10)  //왜 혼재해있을때, subscribeOn이 먼저 수행되지..?
//                .publishOn(Schedulers.newSingle("pub"))
//                .log()
//                .subscribeOn(Schedulers.newSingle("sub"))
//                .log()
//                .subscribe(System.out::println);

//        Thread.sleep(2000);
//        Flux.range(1,10)        //subscribeOn을 원천 소스 바로 아래에서 진행하니깐 얘는 별도 스레드로 1~10까지 빠르게 소비하느라 정신없음.. 그 뒤에 publishOn은 신경안씀...
//                .subscribeOn(Schedulers.newSingle("subbb"))
//                .log()
//                .publishOn(Schedulers.newSingle("pubbb"))
//                .log()
//                .subscribe(System.out::println);


//        Flux.interval(Duration.ofMillis(200))
//                .log()
//                .take(10)
//                .log()
//                .subscribe(l -> log.info("{}",l));


        Flux.just(1,2,3,4,5,6)
//                .publishOn(Schedulers.boundedElastic())
                .map(integer -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return integer*10;
                }).log()
                .publishOn(Schedulers.parallel())
                .map(integer -> integer+1).log()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(integer -> log.info("{}",integer));

        Thread.sleep(10000);

    }
}
