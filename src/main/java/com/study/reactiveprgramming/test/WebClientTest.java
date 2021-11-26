package com.study.reactiveprgramming.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class WebClientTest implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("시작");
        WebClient webClient = WebClient.create();
        webClient.get().uri("http://localhost:8046/test/api")
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
                .doOnNext(log::info)
                .doOnError(t -> log.error("",t))
                .subscribe();
        log.info("끝");
    }
}
