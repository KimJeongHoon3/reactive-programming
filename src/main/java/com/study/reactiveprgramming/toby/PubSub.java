package com.study.reactiveprgramming.toby;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;

public class PubSub {
    //publisher <-- Observable
    //subscriber <-- Observer
    //   publisher는 subscriber로부터 요구에따라 일련의 연속적인 요소들을 제공해주는놈이다
    //subscription : publisher와 subscriber에서 중간다리역할을 해줌.. Subscription을 통해서 데이터를 요청하면, 해당 데이터만큼 push를 해줌.
    //   Observer와 매우 유사하지만, 한 없이 push하는것이아닌 Subscription을 통해서 subscriber가 데이터를 요청하기때문에, pub과 sub의 속도차이가 난다면 이를 통해서 조절가능(back pressure라고함)
    //   subscription은 request라는 함수를 통해서 위의 작업이 이루어지는데, 리턴으 void다. 즉, iterable처럼 데이터를 pull하는 방식이아니라는것! push를 얼만큼 하냐에 대한 이야기임


    // onSubscribe onNext* (onError | onComplete)
    // => 시작은 onSubscrbie는 반드시 호출해야하며, onNext는 여러번 호출가능하고, OnError나 OnComplete는 옵션인데 둘중에 하나만 선택가능하다 라는 뜻

    public static void main(String[] args) {
        Iterable<Integer> iterable=Arrays.asList(1,2,3,4,5);

        Publisher publisher=new Publisher() {

            @Override
            public void subscribe(Subscriber s) {
                Iterator<Integer> iter=iterable.iterator();

                s.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                      try{
                          new Thread(()->{ //여기서 스레드 사용한다면 고민해볼게많음!
                              for(int i=0;i<n;i++){
                                  if(iter.hasNext()){
                                      s.onNext(iter.next());
                                  }else{
                                      s.onComplete();
                                  }
                              }
                          }).start();
                      }catch(Exception e){
                          s.onError(e);
                      }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<Integer> s=new Subscriber<Integer>() {
            Subscription s;
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println(Thread.currentThread().getName()+" | onSubscribe");
                this.s=s;
                this.s.request(1);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(Thread.currentThread().getName()+" | onNext : "+integer);
                this.s.request(1);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println(Thread.currentThread().getName()+" | onComplete");
            }
        };

        publisher.subscribe(s);
    }
}
