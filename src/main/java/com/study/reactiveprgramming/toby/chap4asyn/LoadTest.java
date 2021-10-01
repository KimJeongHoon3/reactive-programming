package com.study.reactiveprgramming.toby.chap4asyn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LoadTest {
    static AtomicInteger atomicInteger=new AtomicInteger(0);
    public static void main(String[] args) throws InterruptedException {
        ExecutorService es= Executors.newFixedThreadPool(100);
        RestTemplate restTemplate=new RestTemplate();
        String url="http://localhost:8080/dr";
        StopWatch sw=new StopWatch();
        sw.start();

        for(int i=0;i<100;i++){
            es.execute(()->{
                int idx=atomicInteger.addAndGet(1);
                log.info("Thread {}",idx);

                StopWatch sw2=new StopWatch();
                sw2.start();

                restTemplate.getForEntity(url,String.class);

                sw2.stop();
                log.info("Elapsed : {} {}",idx,sw2.getTotalTimeSeconds());
            });
        }

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);
        sw.stop();

        log.info("Total Elapsed : {} ",sw.getTotalTimeSeconds());

    }
}
