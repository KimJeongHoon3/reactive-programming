package com.study.reactiveprgramming.etc;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.util.StopWatch;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summarizingInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EtcTest {
    List<Integer> list=new ArrayList<>();

    @Test
    void test(){
        //3
        //2 1
        //1 1 1


        // 8
        // 7 1
        // 6 2
        // 6 1 1
        // 5 3
        // 5 2 1
        // 5 1 1 1
        // 4 4
        // 4 3 1
        // 4 2 1 1
        // 4 1 1 1 1

        int i=8;

        divide(3);
        System.out.println(list);
    }

    private void divide(int i) {
        list.add(i);
        System.out.println(list);

        list.add(--i);
        int temp=0;
        temp++;
        list.add(temp);
        System.out.println(list);

        CompletableFuture<String> f;

        list.add(--i);
        temp=0;
        temp++;
        list.add(temp);
        list.add(temp);
        System.out.println(list);
    }


    @Test
    void testFlatMap(){
        String[] strArr={"Hello", "world"};

        /*List<String> collect = */Arrays.stream(strArr)
                .map(str -> str.split(""))
                .flatMap(arr -> Arrays.asList(arr).stream())
                .forEach(System.out::println);
//                .distinct()
//                .collect(Collectors.toList());


        

    }

    @Test
    void MonoFromRunnable() throws InterruptedException {
        Logger log= LoggerFactory.getLogger(EtcTest.class);
        Mono.just(errorMethod(log))
//        Mono.fromRunnable(() -> {
//            try {
//                log.info(()->"hi");
//                Thread.sleep(3000);
//                if(1==1) throw new RuntimeException();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }).onErrorResume(error -> error instanceof RuntimeException, err -> Mono.just("recovery"))
                .subscribe(obj -> log.info(()->"odj : " + obj));
        log.info(()->"hi2");
        Thread.sleep(5000);
        log.info(()->"hi3");
    }

    private String errorMethod(Logger log) throws InterruptedException {
        log.info(()->"hi");
        Thread.sleep(3000);
        if(1==1) throw new RuntimeException();
        return "hi";
    }

    @Test
    void completableFutureTest() {
        //completableFutures.toArray(new CompletableFuture[completableFutures.size()])
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
//                if (1 == 1) throw new Exception("hi1");
                System.out.println("1 - " + Thread.currentThread().getName());
            } catch (Exception e) {
                System.out.println("err - " + e.getMessage());
//                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }, executorService).thenAccept(v -> {
            try {
                Thread.sleep(1000);
                System.out.println("4 - " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.out.println("err - " + e.getMessage());
//                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<Void> voidCompletableFuture2 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("2 - " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.out.println("err - " + e.getMessage());
//                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, executorService).thenAccept(v -> {
            try {
                Thread.sleep(3000);
                System.out.println("5 - " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.out.println("err - " + e.getMessage());
//                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        ;

        CompletableFuture<Void> voidCompletableFuture3 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
                if (1 == 1) throw new Exception("hi3");
                System.out.println("3 - " + Thread.currentThread().getName());
            } catch (Exception e) {
                System.out.println("err - " + e.getMessage());
//                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, executorService).thenAccept(v -> {
            try {
                Thread.sleep(1000);
//                if (1 == 1) throw new Exception("hi");
                System.out.println("6 - " + Thread.currentThread().getName());
            } catch (Exception e) {
                System.out.println("err - " + e.getMessage());
//                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        ;


//        CompletableFuture<Void> voidCompletableFuture4 = CompletableFuture.runAsync(() -> {
//            try {
//                Thread.sleep(1000);
//                System.out.println("4 - "+Thread.currentThread().getName());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
//
//        }, executorService);
//
//        CompletableFuture<Void> voidCompletableFuture5 = CompletableFuture.runAsync(() -> {
//            try {
//                Thread.sleep(3000);
//                System.out.println("5 - "+Thread.currentThread().getName());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
//        }, executorService);
//
//        CompletableFuture<Void> voidCompletableFuture6 = CompletableFuture.runAsync(() -> {
//            try {
//                Thread.sleep(1000);
////                if (1 == 1) throw new Exception("hi");
//                System.out.println("6 - "+Thread.currentThread().getName());
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
//        }, executorService);

//        CompletableFuture<Void> voidCompletableFuture6 = CompletableFuture.runAsync(() -> {
//            try {
//                System.out.println("hihihihihi?????");
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
//        }, executorService);


        String res = CompletableFuture.supplyAsync(() -> "hi")
                .join();
        System.out.println(res);


        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Throwable> throwableList = Collections.synchronizedList(new ArrayList<>());
        CompletableFuture<Void> result = allOfTerminateOnFailure(throwableList, voidCompletableFuture, voidCompletableFuture2, voidCompletableFuture3);
//        CompletableFuture<Void> result2 = allOfTerminateOnFailure(voidCompletableFuture4, voidCompletableFuture5, voidCompletableFuture6);


        try {
            result.join();

//            result.get();
//            result.thenAccept((v) -> result2)
//                    .join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        System.out.println("exception size : " + throwableList.size());
        try {
//            executorService.shutdownNow();

            Thread.sleep(7000);

//            executorService = Executors.newFixedThreadPool(8);
//
//            CompletableFuture<Void> voidCompletableFuture4 = CompletableFuture.runAsync(() -> {
//                try {
//                    Thread.sleep(1000);
//                    System.out.println("1");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e);
//                }
//
//            }, executorService);
//            voidCompletableFuture4.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }/* catch (ExecutionException e) {
            e.printStackTrace();
        }*/
    }

    public static CompletableFuture allOfTerminateOnFailure(List<Throwable> throwableList, CompletableFuture<?>... futures) {
        CompletableFuture<?> failure = new CompletableFuture<>();
//        failure.exceptionally(ex -> {
//            Arrays.stream(futures).forEach(f -> f.cancel(true)); // CompletableFuture가 체이닝으로 되어있을때 cancel을 호출하면 하위스트림으로 넘어가지않는다!
//            return null;
//        });


        for (CompletableFuture<?> future : futures) {
            future.exceptionally(ex -> {
                throwableList.add(ex);
//                failure.completeExceptionally(ex);
                throw new RuntimeException("error");
//                return null;
            });
        }

        return CompletableFuture.allOf(futures);
//        return CompletableFuture.allOf(failure, CompletableFuture.allOf(futures));

    }


    @Test
    void completableTimeout() throws InterruptedException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture<Void> tCompletableFuture1 = completableTimeout(() -> {
            runnable(1, 5000);
            return null;
        }, 3, TimeUnit.SECONDS);

        CompletableFuture<Void> tCompletableFuture2 = completableTimeout(() -> {
            runnable(2, 1000);
            return null;
        }, 3, TimeUnit.SECONDS);

        CompletableFuture<Void> tCompletableFuture3 = completableTimeout(() -> {
            runnable(3, 2000);
            return null;
        }, 3, TimeUnit.SECONDS);

        try{
            CompletableFuture<Void> completableFuture = CompletableFuture.allOf(tCompletableFuture1, tCompletableFuture2, tCompletableFuture3);
            completableFuture.join();
        }catch(Exception e){
            e.printStackTrace();
        }

        stopWatch.stop();
        System.out.println("elapsedTime : "+stopWatch.getLastTaskTimeMillis());
        Thread.sleep(5000);
    }

    void runnable(int num, int elapsedMills){
        try {
            Thread.sleep(elapsedMills);
            System.out.println(num+"번 작업 - " + Thread.currentThread().getName());
        } catch (Exception e) {
            System.out.println("err - " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static <T> CompletableFuture<T> completableTimeout(Supplier<T> supplier, long delay, TimeUnit timeUnit){

        CompletableFuture<T> completableFuture = new CompletableFuture();

        Future<?> submit = executorService.submit(() -> {
            try{
                completableFuture.complete(supplier.get());
            }catch (Exception e){
                completableFuture.completeExceptionally(e);
            }
        });

        scheduledExecutorService.schedule(() -> {
            if(!completableFuture.isDone()){
                submit.cancel(true);
                completableFuture.completeExceptionally(new RuntimeException("타임아웃남"));
            }
        }, delay, timeUnit);

        return completableFuture;
    }

    @Test
    void timeunit(){
        TimeUnit seconds = TimeUnit.MILLISECONDS;
        long l = seconds.toSeconds(5000);
        System.out.println(l);
    }

    @Test
    void localDateTime(){
        long time = new Date().getTime();
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println(time);
        System.out.println(Timestamp.valueOf(now).getTime());
        System.out.println(format);

        System.out.println(Timestamp.valueOf(LocalDateTime.now()).getTime());

        LocalDateTime parse = LocalDateTime.parse("2022-07-25 05:46:39.610", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        System.out.println(parse.toInstant(ZoneOffset.UTC).toEpochMilli());
        System.out.println(Timestamp.valueOf(LocalDateTime.parse("2022-07-25 05:46:39", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).getTime());
        System.out.println(Timestamp.valueOf("2022-07-25 05:46:39").getTime());

        System.out.println(Timestamp.valueOf(LocalDateTime.now()).getTime());
        System.out.println(Timestamp.valueOf(LocalDateTime.now()).getNanos());
        System.out.println(Timestamp.valueOf(LocalDateTime.now()).getNanos());
    }

    @Test
    void nullTest(){
        String s = (String) null;
        System.out.println(s);
    }

    @Test
    void containTest(){
        AAA a = new AAA("a");
        AAA b = new AAA("a");

        System.out.println(a == b);
        System.out.println(a.equals(b));

        HashSet<AAA> l = new HashSet<>();
        System.out.println(l.add(a));
        System.out.println(l.add(b));
        System.out.println(l.size());

    }

    static class AAA {
        private String a;

        public AAA(String a) {
            this.a = a;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AAA aaa = (AAA) o;
            return Objects.equals(a, aaa.a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a);
        }
    }

    @Test
    void tempTest() {
        List<String> strings = Arrays.asList("가", "나", "다");
        System.out.println(strings.stream().collect(joining()));
    }

    @Test
    void localDateToDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate now1 = LocalDate.now();
//        System.out.println(now.atZone(ZoneId.systemDefault()));
        ZonedDateTime x = now.atZone(ZoneId.of("Asia/Seoul"));
        System.out.println(x);
//        System.out.println(now1.atStartOfDay().atZone(ZoneId.systemDefault()));
        ZonedDateTime x1 = now1.atStartOfDay().atZone(ZoneId.of("Asia/Seoul"));
        System.out.println(x1);

        Date from = Date.from(x.toInstant());
        Date from2 = Date.from(x1.toInstant());

        System.out.println(from);
        System.out.println(from2);
        LocalDate localDate = from.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        System.out.println(now);
        System.out.println(localDate);
//        assertEquals(now, localDate);

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(zonedDateTime.getZone());

        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        System.out.println(offsetDateTime.getOffset());

        System.out.println(Instant.now().toEpochMilli());
        System.out.println(System.currentTimeMillis());

        Date date = new Date();
        System.out.println(date);

    }

    private static final ZoneId ASIA_SEOUL_ZONE_ID = ZoneId.of("Asia/Seoul");
    @Test
    void date만나오기() {
//        Date date = new Date(2022,5,3,5,10,30);
        Date date = new Date();
//        System.out.println(date.getTime());
//        System.out.println(System.currentTimeMillis());
        Instant instant = Instant.now();
//        System.out.println(instant.toEpochMilli());
//
//        System.out.println(Instant.now());
        System.out.println("Instant.ofEpochMilli(date.getTime()).atZone(ASIA_SEOUL_ZONE_ID) : "+Instant.ofEpochMilli(date.getTime()).atZone(ASIA_SEOUL_ZONE_ID));
        LocalDate localDate = Instant.ofEpochMilli(date.getTime()).atZone(ASIA_SEOUL_ZONE_ID).toLocalDate();
        System.out.println(localDate);
        System.out.println("localDate.atStartOfDay(ASIA_SEOUL_ZONE_ID) : "+localDate.atStartOfDay(ASIA_SEOUL_ZONE_ID));
        System.out.println("localDate.atStartOfDay(ASIA_SEOUL_ZONE_ID).toInstant() : "+ localDate.atStartOfDay(ASIA_SEOUL_ZONE_ID).toInstant());
        System.out.println(date);
        System.out.println(toDate(localDate));

        System.out.println(LocalDate.now().atStartOfDay(ASIA_SEOUL_ZONE_ID).toInstant());
        System.out.println(LocalDateTime.now().atZone(ASIA_SEOUL_ZONE_ID).toInstant().atZone(ASIA_SEOUL_ZONE_ID));
    }

    @Test
    void 오늘date만() {
        LocalDateTime now = LocalDateTime.now(ASIA_SEOUL_ZONE_ID);
        LocalDateTime now1 = LocalDateTime.now();

        System.out.println(now);
        System.out.println(now1);


    }

    @Test
    void 오늘마지막dateTime() {
        LocalDate today = LocalDate.now(ASIA_SEOUL_ZONE_ID);
        LocalDateTime localDateTime = today.atTime(23, 59, 59, 0);

        System.out.println(toDate(localDateTime));
    }

    @Test
    void calendarTest() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance(Locale.KOREAN);
        cal.setTime(date);
//        cal.add(y, z);
        Date currentTime = cal.getTime();
        System.out.println("setTime호출 : "+currentTime);

        Calendar instance = Calendar.getInstance(Locale.KOREAN);
        System.out.println(instance.getTime());
    }

    @Test
    void date비교() {
        Date date = new Date();
        Date date2 = new Date();

        assertEquals(0, date.compareTo(date2));
    }

    @Test
    void formatter() {
        String s = "2022-03-18";
        LocalDate parse = LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println(parse);
    }
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ASIA_SEOUL_ZONE_ID).toInstant());
    }

    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ASIA_SEOUL_ZONE_ID)
                .toInstant());
    }
}
