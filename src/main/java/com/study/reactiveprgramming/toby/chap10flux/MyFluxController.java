package com.study.reactiveprgramming.toby.chap10flux;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@RestController
public class MyFluxController {

    @GetMapping("events/{id}")
    Mono<List<Event>> event(@PathVariable long id){
        List<Event> list=Arrays.asList(new Event(1L,"event1"),new Event(2L,"event2"));
        return Mono.just(list);
    }

    @GetMapping("events/flux/{id}")
    Flux<Event> eventFlux(@PathVariable long id){
        List<Event> list=Arrays.asList(new Event(1L,"event1"),new Event(2L,"event2"));
        return Flux.fromIterable(list);
    }


    @GetMapping(value = "events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> events(){
        return Flux
//                .<Event>generate(sink -> sink.next(new Event(System.currentTimeMillis(),"value"))) //sink라는 오브젝트를 통해서 계속 생성해서 데이터를 넘겨줄수있음..
                .<Event,Long>generate(()->1L,(id,sink) -> {  //위의 generate는 상태값을 가질수없으므로 이전의값을 참조하여 무언가를 할수없는데, generate는 state를 가지고 이전의값을 참조하여 활용할수있다!
                    sink.next(new Event(id,"value"+id));
                    return id+1;
                })
                .delayElements(Duration.ofSeconds(1))
                .take(10);
    }

    @GetMapping(value = "events/zip", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> eventsZip(){
        Flux<Event> generate = Flux.<Event, Long>generate(() -> 1L, (id, sink) -> {  //위의 generate는 상태값을 가질수없으므로 이전의값을 참조하여 무언가를 할수없는데, generate는 state를 가지고 이전의값을 참조하여 활용할수있다!
            sink.next(new Event(id, "value" + id));
            return id + 1;
        });

        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

        return Flux.zip(generate,interval).map(tu -> tu.getT1()).take(10); //zip을 활용한 delay

    }

    @GetMapping(value = "events/zip2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> eventsZip2(){
        Flux<String> generate = Flux.generate(sink -> sink.next("value"));

        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

        return Flux.zip(generate,interval).map(tu -> new Event(tu.getT2(),tu.getT1())).take(10); //이렇게 데이터를 조합하는게 zip 사용하는데 있어서 좀더 일반적

    }

    @Data
    @AllArgsConstructor
    public static class Event{
        long id;
        String value;
    }
}
