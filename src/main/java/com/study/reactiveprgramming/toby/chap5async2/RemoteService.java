package com.study.reactiveprgramming.toby.chap5async2;

import com.study.reactiveprgramming.ReactivePrgrammingApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RemoteService {
    @RestController
    static class MyController{
        @GetMapping("/remote/service")
        String remote(String data) throws InterruptedException {
            Thread.sleep(2000);
//            if(1==1){
//                throw new RuntimeException("일부러 에러");
//            }
            return data +" | remote";
        }

        @GetMapping("/remote/service2")
        String remote2(String data) throws InterruptedException {
            Thread.sleep(2000);
            return data +" | remote2";
        }
    }

    public static void main(String[] args) {
        System.setProperty("server.port","8899");
        System.setProperty("server.tomcat.threads.max","100");
        SpringApplication.run(RemoteService.class, args);
    }
}
