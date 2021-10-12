package com.study.reactiveprgramming.toby.chap9mono;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 테스트할때 Netty서버 사용을 위해 'org.springframework.boot:spring-boot-starter-web' 의존성 제거하기!
 * 이것만 테스트할때 사용가능.. 위의 의존성 제거하면 다른 클래스에 있는거 에러남..
 * */
@RestController
@Slf4j
public class MyMonoController {
    @GetMapping("/mono/hello")
    Mono<String> hello(){
        log.info("pos1");
//        Mono<String> m=Mono.just(generateHello()).log(); // log 또한 Operator 즉, 또하나의 pub..  Publisher -> Publisher(Operator) -> Publisher(Operator) ... -> Subscriber
        //Mono.just 인자로 있는 generateHello 메서드는 subscribe하고나서 실행되는 부분이아니다! Mono가 만들어질때 이미 실행이 된다!! 결과값이 Mono에 들어가있는것이고 그것을 subscribe할때 활용하는것이다!
        // Mono.just는 Mono라는 컨테이너에 데이터를 담은것뿐!
        Mono<String> m=Mono.fromSupplier(()->generateHello()).log();
        //Mono.fromSupplier 에 넘겨주는 콜백은 subscribe할때 실행한다!

        /*
            Mono에 담긴 결과값을 가져오고 싶을때는 block() 메소드를 사용하면된다
            block을 호출하면 내부적으로 구독을 하여서 결과를 꺼내올수있다..
            Mono의 결과값을 이렇게 꺼내온다면, 구독을 해서 결과를 가져올때까지 block 되기때문에 block이라는 이름을 사용한것!
            그러나, 가능한한 block은 사용하지말자!
        */

        //Publisher는 하나지만, Subscriber는 여러개가될수있다! 즉, 스트림과 달리 한번 소비했으면 끝이아니라, 구독하는대로 계속 소비할수있다!
        /*publishing 하는 소스 타입은 두가지
          - hot
            - 실시간으로 일어나는 이벤트들이 hot (구독한 때부터 데이터를 전달받게됨)
            - 구독한 시기에 따라 pub의 데이터는 다를수있음!
          - cold
            - 언제 구독을 하던지간에 pub의 데이터를 동일하게 가져옴
         */
        log.info("pos2");
        return m;
    }

    private String generateHello() {
        log.info("method generateHello()");
        return "Hello Mono";
    }
}
