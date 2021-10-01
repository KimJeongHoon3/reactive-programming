package com.study.reactiveprgramming.toby.chap3schedulers;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class IntervalEx {
    public static void main(String[] args) {
        Publisher<Integer> pub= sub->{
            sub.onSubscribe(new Subscription() {
                ScheduledExecutorService ses= Executors.newSingleThreadScheduledExecutor();
                int no=0;
                volatile boolean cancelled=false;
                @Override
                public void request(long n) {
                   ses.scheduleAtFixedRate(()->{
                       if(!cancelled){
                           sub.onNext(no++);
                       }else{
                           ses.shutdown();
                       }

                   },0,300, TimeUnit.MILLISECONDS);

                }

                @Override
                public void cancel() {
                    cancelled=true;
                }
            });
        };

        Publisher<Integer> takePub=take(pub,5);
        takePub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.debug("onNext : {}",integer);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError : {}",t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });
    }

    static Publisher<Integer> take(Publisher<Integer> pub, int threshold){
        return sub -> {
            pub.subscribe(new Subscriber<Integer>() {
                int count=0;
                Subscription subscription;
                @Override
                public void onSubscribe(Subscription s) {
                    subscription=s;
                    sub.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    count++;
                    sub.onNext(integer);

                    if(count==threshold){
                        subscription.cancel();
                    }
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onComplete() {

                }
            });
        };
    }
}
