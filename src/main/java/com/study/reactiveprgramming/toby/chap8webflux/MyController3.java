package com.study.reactiveprgramming.toby.chap8webflux;

import com.study.reactiveprgramming.ReactivePrgrammingApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
public class MyController3 {
    WebClient webClient=WebClient.create();

    @Autowired
    ReactivePrgrammingApplication.MyService3 myService3;

    @Autowired
    ReactivePrgrammingApplication.MyService5 myService5_completableFuture;

    @Autowired
    ReactivePrgrammingApplication.MyService4 myService4_sync;

    ExecutorService es= Executors.newFixedThreadPool(100);

    public MyController3(){
      /*  HttpClient httpClient = HttpClient
                .create()
                .wiretap(true);

        webClient=WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();*/
    }

/*
    웹플럭스 사용시 주의점
    - 구독을 어딘가에서 진행해야만 발행되어 로직이 수행된다! 그냥 발행만 있다면 아무것도 하지않는다!(webclient 사용시 주의..)

    Flux : event driven 으로 이벤트 발생시 "여러개"의 데이터들을 받아서 처리하기위해 사용 (1:10:00 좀더 참고)
    Mono : Mono는 "하나" 라는 뜻. 기존의 전통적인 DB나 API에서 결과 하나 가져오는것에 리엑티브 스타일에서 굳이 여러개로 만들지않게 대응하기위해서 Mono를 사용

    low level의 코드가 드러나지않는게 엄청난 장점이지만, 그만큼 내부적으로 어떻게 돌아가는지 이해가 안되면 문제의 원인 찾기가 어려우므로 로그를 찍으면서 흐름을 반드시 잘 파악할것!
*/


    @GetMapping("/webflux/rest")
    public Mono<String> rest(int idx){ //spring은 Mono로 전달해주면, 이를 알아서 subscribe함..

        return webClient.get().uri("http://localhost:8899/remote/service?data={data}",idx)
                //여기를 처음 호출하는 놈은 톰켓(만약 netty서버라면 netty 스레드.. 스프링 내부적으로 구독을 진행하니깐)
                //webclient는 netty를 기반으로 동작하기때문에, 이제 요청을 위한 채널이 등록되고 eventLoop에 등록되므로 netty의 스레드 사용
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode()==HttpStatus.OK ){
                        return clientResponse.bodyToMono(String.class);
                    }else{
                        return Mono.error(new RuntimeException("에러 | "+idx)); //이 에러는 에러1, 에러2, 에러3 을 거쳐서 Exception Handler 로 넘어가게됨.. Exception Handler에서는 doOnError의 스레드가 아니라 톰켓의 스레드이다..( "normal_async.png" 참고 )
                    }

                })
                .doOnNext(s -> log.info("1번요청 : "+s)) //여기서는 응답한 값을 볼수잇는데, 이때의 스레드는 요청할때와 같은 스레드일거임(netty의 채널은 eventloop에 등록되는데, 이 eventLoop는 항상 동일한 스레드로 처리됨.. 그래서 데이터 전송시 사용한 스레드와, 데이터 받았을때의 스레드는 동일.. 그리고 이로 인하여 모든 이벤트의 순서를 보장한다.. )
                .doOnError( e -> log.info("에러1 "+e.getMessage()))
                .flatMap(res -> webClient.get().uri("http://localhost:8899/remote/service2?data={data}",res).exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class)))
                .doOnNext(s -> log.info("2번요청 : "+s)) // 여기는 remote/service 에 전송했던 스레드와는 다를수있지만, service2를 요청한 스레드와는 같을거임
                .doOnError( e -> log.info("에러2 "+e.getMessage()))
                .flatMap(res -> Mono.fromCompletionStage(myService5_completableFuture.work(res))) //여기서 block이 있다면 당연 스레드 하나를 점유하게되는거기때문에 요청이 eventLoop(디폴트는 Runtime.getRuntime().availableProcessors()*2) 갯수를 초과하게된다면 전체적인 서비스는 현저히 느려진다.. 그래서 비동기 사용! 스프링의 디폴트 스레드풀 사용
                .doOnNext(s -> log.info("3번 비동기 메서드 : "+s))
                .doOnError( e -> log.info("에러3 "+e.getMessage()));

        /*
         - 여기서 map을 사용하지않고 flatMap을 사용할수밖에 없는이유!
           - map을 사용하면 이렇게 된다
            Mono<ClientResponse> exchange = webClient.get().uri("aa", "data").exchange();
            Mono<Mono<String>> map = exchange.map(clientResponse -> clientResponse.bodyToMono(String.class));
            bodyToMono(String.class)를 호출하게되면 Mono<String> 이 리턴타입이다.. 그렇기때문에 여기서와 같이 map안에서 사용하게되면, Mono<Mono<String>>이 되어버린다.. 그렇기때문에 flatMap을 사용해야했다!!! (flatMap은 리턴해준거를 평면화하기때문에 다시 Mono<String>을 리턴할때 이와 동일하게 Mono<String>으로 만들어주기때문에!!)
            *함수가 컨테이너를 감싸서(ex. Mono<XX>, Flux<XX>, List<XX>..등) 리턴한다면 flatMap을 통해서 평면화시켜라!!

         - 비동기 메서드가 CompletableFuture를 반환할때, Mono로 계속 연결하기위해서는 Mono.fromCompletionStage() 를 사용하라! CompletableFuture는 CompletionStage를 구현하고있기때문에 가능!

        */


//       webClient.get().uri().exchangeToMono(clientResponse -> )

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String error(Exception e){
        log.error("exception handler : "+e.getMessage());
        return e.getMessage();
    }


}
