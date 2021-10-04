package com.study.reactiveprgramming.toby.chap5async2;

import com.study.reactiveprgramming.ReactivePrgrammingApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RemoteService {
    @RestController
    static class MyController{

    }

    public static void main(String[] args) {
        SpringApplication.run(RemoteService.class, args);
    }
}
