package com.study.reactiveprgramming.modernjava.cell;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCellTest {

    @Test
    void createSimpleCell(){
        SimpleCell c1=new SimpleCell("c1");
        SimpleCell c2=new SimpleCell("c2");
        SumCell c3=new SumCell("c3");

        c1.subscribe((ProxySub)c3::setLeft);
        c2.subscribe((ProxySub)c3::setRight);

        c1.onNext(5);
        c2.onNext(10);
    }


    @FunctionalInterface
    interface ProxySub extends Subscriber<Integer> {

        @Override
        default void onSubscribe(Subscription s) {

        }

        @Override
        default void onError(Throwable t) {

        }

        @Override
        default void onComplete() {

        }
    }
}