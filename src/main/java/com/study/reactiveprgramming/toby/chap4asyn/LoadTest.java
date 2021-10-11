package com.study.reactiveprgramming.toby.chap4asyn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LoadTest {
    static AtomicInteger atomicInteger=new AtomicInteger(0);
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        ExecutorService es= Executors.newFixedThreadPool(100);
        RestTemplate restTemplate=new RestTemplate();
//        String url="http://localhost:8080/dr";
        String url2="http://localhost:8080/async3/rest?idx={idx}";
        String url3="http://localhost:8080/webflux/rest?idx={idx}";

        CyclicBarrier cyclicBarrier=new CyclicBarrier(101);

        for(int i=0;i<100;i++){
            es.submit(()->{
                int idx=atomicInteger.addAndGet(1);
                cyclicBarrier.await();

                log.info("Thread {}",idx);

                StopWatch sw2=new StopWatch();
                sw2.start();

//                restTemplate.getForEntity(url,String.class);
//                String res = restTemplate.getForObject(url2, String.class, idx);
                String res = restTemplate.getForObject(url3, String.class, idx);

                sw2.stop();
                log.info("Elapsed : {} {} / {}",idx,sw2.getTotalTimeSeconds(),res);
                return null;
            });
        }

        cyclicBarrier.await();

        StopWatch sw=new StopWatch();
        sw.start();

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        sw.stop();

        log.info("Total Elapsed : {} ",sw.getTotalTimeSeconds());

    }
}
