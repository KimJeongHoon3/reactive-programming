package com.study.reactiveprgramming.toby.chap2operators;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class PubSub2 {
    public static void main(String[] args) {
        Publisher<Integer> publisher= getPublisher();
        Publisher<Integer> reducePub=reducePub(publisher,1,(a,b)->a*b);
        Publisher<Integer> mapPub=mapPub(publisher,i -> i+1);
        Publisher<Integer> mapPub2=mapPub(mapPub,i -> i*3);
//        Publisher<Integer> reducePub=reducePub(mapPub2,0,(a,b)->a+b);
//        Publisher<String> mapPub3=mapPub(mapPub2, i-> i+" 끝 ");

        /***
         * logSub이 mapPub2를 구독하고, mapPub2 가 mapPub을 구독하고, mapPub이 publisher를 구독..
         * 데이터를 전달해줄때는 그래서 구독의 역순으로 전달이되고, Operator들이 중간에 데이터 가공또한 구독을 통해서 가능!
         * 발행자가 데이터나 기타 이벤트들을 자신의 구독자에게 넘겨줌으로써 최종구독자(logSub)가 데이터를 읽어오게됨!
         */

        reducePub.subscribe(logSub());
    }

    private static Publisher<Integer> reducePub(Publisher<Integer> publisher, Integer init, BinaryOperator<Integer> binaryOperator){ //이런거 제네릭으로바꿀때는 실제 구체 타입으로 변경해보고 하라!
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                publisher.subscribe(new Subscriber<Integer>() {
                    Integer total=init;
                    @Override
                    public void onSubscribe(Subscription s) {
                        sub.onSubscribe(s);

                    }

                    @Override
                    public void onNext(Integer integer) {
                        total=binaryOperator.apply(total,integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        sub.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        sub.onNext(total);
                        sub.onComplete();
                    }
                });
            }
        };

    }

    private static <T,R> Publisher<R> mapPub(Publisher<T> publisher, Function<T, R> function) {
        return new Publisher<R>() {
            @Override
            public void subscribe(Subscriber<? super R> sub) {
                log.debug("찐 스타트를 향해..");
                publisher.subscribe(new Subscriber<T>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        log.debug("대리 sub중.. - ");
                        sub.onSubscribe(s);
                    }

                    @Override
                    public void onNext(T value) {
                        log.debug("변형중 - {}",function.apply(value));
                        sub.onNext(function.apply(value));
                    }

                    @Override
                    public void onError(Throwable t) {
                        sub.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        sub.onComplete();
                    }
                });
            }
        };

    }

    private static <T> Subscriber<T> logSub() {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("찐 sub 시작");
                log.info("onSubscribe : ");
                s.request(Long.MAX_VALUE);

            }

            @Override
            public void onNext(T value) {
                log.info("onNext : {}", value);
            }

            @Override
            public void onError(Throwable t) {
                log.info("onError : ", t);
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        };
    }

    private static Publisher<Integer> getPublisher() {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> s) {
                log.debug("찐 스타트 ");
                List<Integer> list = Stream.iterate(1, i -> i + 1)
                        .limit(10)
                        .collect(Collectors.toList());

                s.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        try {
                            list.forEach(i -> {
                                s.onNext(i);
                            });

                            s.onComplete();
                        } catch (Exception e) {
                            s.onError(e);
                        }

                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }

}
