package com.study.reactiveprgramming.toby.chap3schedulers;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import reactor.core.publisher.Flux;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SchedulerEx {
    public static void main(String[] args) {
        Publisher<Integer> pub= sub->{
            sub.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    log.info("Request");
                    sub.onNext(1);
                    sub.onNext(2);
                    sub.onNext(3);
                    sub.onNext(4);

                }

                @Override
                public void cancel() {
                }
            });
        };




        //Typically used for slow publisher e.g., blocking IO, fast consumer(s) scenarios.
        Publisher<Integer> subOnPub=sub -> {
            ExecutorService es= Executors.newSingleThreadExecutor(new CustomizableThreadFactory(){ // 스프링에서 ThreadFactory 잘 활용하기 좋도록 만들어놓은것이 CustomizableThreadFactory.. 내가 원하는부분만 변경하면됨
                @Override
                public String getThreadNamePrefix() {
                    return "subOn-";
                }
            });
            es.submit(()->pub.subscribe(sub));
        };

        //Typically used for fast publisher, slow consumer(s) scenarios.
        //그리고 publishOn에서 consumer를 더 빠르게하겠다고 여러 스레드를 써서 event의 순서를 뒤바꾸면안된다! (규칙임)
        Publisher<Integer> pubOnPub=sub -> {
            subOnPub.subscribe(new Subscriber<Integer>() {
                ExecutorService es=Executors.newSingleThreadExecutor(new CustomizableThreadFactory(){
                    @Override
                    public String getThreadNamePrefix() {
                        return "pubOn-";
                    }
                });
                @Override
                public void onSubscribe(Subscription s) {
                    sub.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    es.submit(()->sub.onNext(integer));
                }

                @Override
                public void onError(Throwable t) {
                    es.submit(()->sub.onError(t));
                    es.shutdown(); //우아한종료가능.. shutdownNow는 강제로 interrupt 발생
                }

                @Override
                public void onComplete() {
                    es.submit(()->sub.onComplete());
                    es.shutdown();
                }
            });
        };


        pubOnPub.subscribe(new Subscriber<Integer>() {
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


}
