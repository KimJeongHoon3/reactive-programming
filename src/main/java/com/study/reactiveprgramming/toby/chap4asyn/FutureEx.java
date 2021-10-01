package com.study.reactiveprgramming.toby.chap4asyn;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;


@Slf4j
public class FutureEx {
    interface SuccessCallback{
        void onSuccess(String result);
    }

    interface ErrorCallback{
        void onError(Throwable e);
    }

    static class FutureTaskCallback extends FutureTask<String>{
        SuccessCallback successCallback;
        ErrorCallback errorCallback;

        public FutureTaskCallback(Callable<String> callable,SuccessCallback successCallback, ErrorCallback errorCallback) {
            super(callable);
            this.successCallback= Objects.requireNonNull(successCallback); //null 이면 NullPointerException 에러 던짐
            this.errorCallback=Objects.requireNonNull(errorCallback);
        }

        @Override
        protected void done() { //에러든 정상이던 처리완료되었을때 done은 실행됨.. 그래서 get을 호출할때 정상적으로 완료되었따면 정상결과값을 받을것이고, 그렇지않으면 에러를 호출함
            try {
                successCallback.onSuccess(get());
            } catch (InterruptedException e) { //InterruptedException은 작업을 수행하지말고 종료하라는 시그날임..
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                errorCallback.onError(e.getCause());
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService es= Executors.newCachedThreadPool();

        //java 5 시절..
//        FutureTask<String> f=new FutureTask<String>(()->{ //callable은 예외를 밖으로 던지기에 Runnable과 달리 try~catch를 안에서 바디에서 해줄필요없음
//            Thread.sleep(2000);
//            log.info("Async");
//            return "Hello";
//        }){
//            @Override
//            protected void done() { // 작업완료했을때 호출하는 hook메서드
//                try {
//                    System.out.println(get());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//        };


        //좀더 개선.. 성공과 실패를 callback으로 뺀다!
        FutureTaskCallback f=new FutureTaskCallback(()->{
            Thread.sleep(2000);
            if(1==1) throw new RuntimeException("Async ERROR");
            log.info("Async");
            return "Hello";
        }
        ,res -> log.info("onSuccess : {}",res)
        ,t -> log.error("onError : {}",t.getMessage(),t));

        es.execute(f);
        es.shutdown();
    }
}
