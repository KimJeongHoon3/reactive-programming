package com.study.reactiveprgramming.toby;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ob {
    // Iterable <--> Observable (duality 상대성)
    // Pull          Push

    // Observable을 사용하면, pull이 아니라, push 방식이기때문에, 멀티스레드 구성하기 좋음

    /*
    * 하지만 단점이 있어서 개선을 함
    *
    * 1. Event에 끝이없다!! 완료의 개념이없음!
    * 2. Error 처리가 어렵다 (전파가 되어도.. 어디서 잡을거냐..)
    *
    * */

    static class IntObservable extends Observable implements Runnable{ //Observable : source -> event(data)를 Observer 에게 던짐

        @Override
        public void run() {
            for(int i=1;i<=10;i++){
                setChanged();
                notifyObservers(i);      // push
                // <--> int i=it.next(); // pull 위의것과 좌우가바뀜..  Iterable은 파리미터없고, 리턴타입이있음.. i를 파라미터로 넘기냐 i를 받느
            }
        }
    }

    public static void main(String[] args) {
        Observer ob= new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        };

        ExecutorService executorService= Executors.newSingleThreadExecutor();

        IntObservable io = new IntObservable();
        io.addObserver(ob);

//        io.run();
        executorService.submit(io); // 별도의 스레드 사용할수있도록 변경하기가 매우 용이함
        executorService.shutdown();
    }
}
