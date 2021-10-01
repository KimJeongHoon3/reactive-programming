package com.study.reactiveprgramming.toby.chap2operators;

import reactor.core.publisher.Flux;

public class ReactorEx {
    public static void main(String[] args) throws InterruptedException {
        Flux.<Integer>create(e->{
            e.next(1);
            e.next(2);
            e.next(3);
            e.next(4);
            e.complete();
        })
        .log()
        .map(s->s*10)
        .log()
        .reduce(1,(a,b)->a+b)
        .log()
        .subscribe(integer -> System.out.println(integer),Throwable::printStackTrace);

    }
}
