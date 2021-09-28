package com.study.reactiveprgramming.modernjava.cell;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

public class SimpleCell implements Subscriber<Integer>, Publisher<Integer> {
    String name;
    int value;
    List<Subscriber<? super Integer>> subscribers=new ArrayList<>();

    public SimpleCell(String name) {
        this.name = name;
    }

    @Override
    public void subscribe(Subscriber<? super Integer> s) {
        subscribers.add(s);
    }

    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(Integer integer) {
        this.value=integer;
        System.out.println(name+" : "+value);
        for(Subscriber<? super Integer> s :subscribers){
            s.onNext(integer);
        }
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
