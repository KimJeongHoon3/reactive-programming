package com.study.reactiveprgramming.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
public class WebClientTest {

    @Test
    void testWebClient(){
        WebClient webClient = WebClient.create();
        webClient.get().uri("http://localhost:8046/test/api")
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
                .doOnNext(body-> System.out.println(Thread.currentThread().getName()+" - "+body))
                .subscribe();
    }

}
