package com.study.reactiveprgramming.test;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class LogTest {
    public static void main(String[] args) throws InterruptedException {
        Mono.just("abc1")
                .map(s->s+"d")
//                .publishOn(Schedulers.single())
//                .log()
                .doOnNext(log::info)
                .map(s->s+"e")
                .doOnNext(log::info)
                .subscribe();

        Mono.just("abc2")
                .map(s->s+"d")
//                .publishOn(Schedulers.single())
                .log()
                .doOnNext(log::info)
                .map(s->s+"e")
                .doOnNext(log::info)
                .subscribe();
        Thread.sleep(2000);
    }
}
