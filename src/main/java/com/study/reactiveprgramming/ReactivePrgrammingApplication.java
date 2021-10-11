package com.study.reactiveprgramming;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Queue;
import java.util.concurrent.*;

@SpringBootApplication
@Slf4j
@EnableAsync
public class ReactivePrgrammingApplication {

    @Service
    public static class MyService3{
        @Async
        public ListenableFuture<String> work(String req){
            return new AsyncResult<>(req + " | asyncService");
        }
    }

    @Service
    public static class MyService4{
        public String work(String req) {
            log.info("req : "+req);
            return req + " | asyncService";
        }
    }

    @Service
    public static class MyService5{
        @Async
        public CompletableFuture<String> work(String req){
            return CompletableFuture.completedFuture(req + " | asyncService");
        }
    }

    @Component
    public static class MyService{
        @Async // 클라이언트가 요청(Http)한 작업이 장시간 진행된다면 Async를 사용하는것이좋음!
        public ListenableFuture<String> hello() throws InterruptedException { //결과 가져오지않을거면 그냥 void 해도 상관없음
            log.info("hello()");
            Thread.sleep(1000);
            return new AsyncResult<>("hello");
        }
    }

    @RestController
    public static class MyController{
        @GetMapping("/callable")
        public Callable<String> callable(){
            log.info("callable");
            return ()->{
                log.info("async");
                Thread.sleep(2000);
                return "Hello";
            };
        }

        @GetMapping("/sync")
        public String sync() throws InterruptedException {
            log.info("sync");
            Thread.sleep(2000);
            return "Hello";
        }

        //DeferredResult
        //  지연된 응답을 준다..
        //  워커 스레드로 따로 잡고잇는게 아닌, DeferredResult에 결과를 셋팅하면 실행됨..
        //  사용자 입장에서는 특정 응답을 기다리고있다가 원하는 이벤트가 발생했을때 응답을 받을수있다..
            // 채팅 같은거 구현할수있다함.. 다른 누군가가 메세지 쓸것을 기다렸다가 누군가가 쓰게되었을때(이벤트발생) 응답을 받을수있음..

        Queue<DeferredResult<String>> results=new ConcurrentLinkedQueue<>();

        @GetMapping("/dr")
        public DeferredResult<String> dr(){
            log.info("dr");
            DeferredResult<String> dr=new DeferredResult<>();
            results.add(dr);
            return dr;
        }

        @GetMapping("/dr/size")
        public String drCount(){
            return String.valueOf(results.size());
        }

        @GetMapping("/dr/event")
        public String drEvent(String msg){
            for(DeferredResult<String> dr:results){
                dr.setResult("Hello "+ msg);
                results.remove(dr);
            }

            return "Ok";
        }
        /////////////////////////////////////////////


        // emitter
        //   http의 스트림방식 표쥰에 따라서 만든거라함..
        @GetMapping("/emitter")
        public ResponseBodyEmitter emitter(){
            ResponseBodyEmitter emitter = new ResponseBodyEmitter();

            Executors.newSingleThreadExecutor().submit(()->{
                try{
                    for(int i=0;i<=50;i++){
                        emitter.send("<p>Stream "+ i +"</p>");
                        Thread.sleep(100);
                    }
                }catch(Exception e){}
            });

            return emitter;
        }


        //////////////////////////////////////
    }


    public static void main(String[] args) {
        SpringApplication.run(ReactivePrgrammingApplication.class, args);
    }

    @Autowired MyService myService;
    @Autowired MyService3 myService3;

    @Bean
    ApplicationRunner run(){ //컨트롤러 같은 역할이라 생각하면됨
        return args -> {
            log.info("run()");
            ListenableFuture<String> listenableFuture=myService3.work("hi");
            listenableFuture.addCallback(res -> log.info("success : {}", res),
                    ex -> log.error("error : {}",ex.getMessage(),ex));

            log.info("exit");
        };
    }

//    static class
}
