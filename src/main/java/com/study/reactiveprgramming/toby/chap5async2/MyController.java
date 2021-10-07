package com.study.reactiveprgramming.toby.chap5async2;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@Slf4j
public class MyController {
    AsyncRestTemplate asyncRestTemplate;
    public MyController(){
        asyncRestTemplate =new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));
    }

    @GetMapping("/async2/rest")
    public DeferredResult<String> rest(int idx){
        DeferredResult dr=new DeferredResult();
        ListenableFuture<ResponseEntity<String>> listenableFuture = asyncRestTemplate.getForEntity("http://localhost:8899/remote/service?data={data}", String.class, idx);
        listenableFuture.addCallback(res -> {
            //계속 이런식으로 쓰게되면 콜백 hell이 나타남..
            dr.setResult(res);
        },ex -> {
            dr.setErrorResult(ex);
        });
        return dr;
    }
}
