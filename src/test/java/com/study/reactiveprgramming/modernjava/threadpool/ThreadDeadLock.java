package com.study.reactiveprgramming.modernjava.threadpool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class ThreadDeadLock {

    static ExecutorService executorService= Executors.newSingleThreadExecutor();
    @Test
    void 데드락() throws Exception {
        Future<String> f=executorService.submit(new TempCallable());
        System.out.println(f.get());
    }

    static class TempCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            Future<String> future1=executorService.submit(()->{
                Thread.sleep(2000);
                return "abc";
            });

            return future1.get();
        }
    }
}
