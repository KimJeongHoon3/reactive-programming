package com.study.reactiveprgramming.toby.chap6async3;

import com.study.reactiveprgramming.ReactivePrgrammingApplication;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

@RestController
@Slf4j
public class MyController2 {
    AsyncRestTemplate asyncRestTemplate;
    public MyController2(){
        asyncRestTemplate =new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));
    }

    @Autowired
    ReactivePrgrammingApplication.MyService3 myService3;

    @Autowired
    ReactivePrgrammingApplication.MyService4 myService4_sync;

    ExecutorService es= Executors.newFixedThreadPool(100);

    @GetMapping("/async3/rest")
    public DeferredResult<String> rest(int idx){
        DeferredResult dr=new DeferredResult(new Long("100000"));

        toCF(asyncRestTemplate.getForEntity("http://localhost:8899/remote/service?data={data}", String.class, idx))
                .thenCompose(s -> toCF(asyncRestTemplate.getForEntity("http://localhost:8899/remote/service2?data={data}", String.class,s.getBody())))
                .thenApplyAsync(s -> myService4_sync.work(s.getBody()),es) //기존에 시간이 오래걸릴거같아서 서비스단에서 @Async를 사용하여 비동기로 만들어놓았던것들을 동기로 처리할수있음!(비동기 수행은 CompletableFuture를 사용하는것!) 그리고 필요에따라 적절한 스레드 풀도 유동적으로 지정할수있으니 좋음! 비동기 처리하는 곳을 컨트롤러에서 관리할수있겠네~
                .thenAccept(s -> dr.setResult(s))
                .exceptionally(e -> {
                    dr.setErrorResult(e.getMessage());
                    return null;
                });


//        Completion.from(asyncRestTemplate.getForEntity("http://localhost:8899/remote/service?data={data}", String.class, idx))
//                .andApply(res -> asyncRestTemplate.getForEntity("http://localhost:8899/remote/service2?data={data}", String.class,res.getBody()))
//                .andApply(res -> myService3.work(res.getBody()) )
//                .andError(e-> dr.setErrorResult(e.toString()))
//                .andAccept(r -> dr.setResult(r)); //r을 계속 ResponseEntity로 인식..



//        ListenableFuture<ResponseEntity<String>> listenableFuture = asyncRestTemplate.getForEntity("http://localhost:8899/remote/service?data={data}", String.class, idx);
//        listenableFuture.addCallback(res -> {
//            //계속 이런식으로 쓰게되면 콜백 hell이 나타남..
//            dr.setResult(res);
//        },ex -> {
//            dr.setErrorResult(ex);
//        });
        return dr;
    }

    private <T> CompletableFuture<T> toCF(ListenableFuture<T> listenableFuture) {
        CompletableFuture<T> completableFuture=new CompletableFuture();
        listenableFuture.addCallback(s -> completableFuture.complete(s), e-> completableFuture.completeExceptionally(e));
        return completableFuture;
    }


    public static class AcceptCompletion<T> extends Completion<T,Void>{
        Consumer<T> consumer;
        public AcceptCompletion(Consumer<T> consumer) {
            this.consumer = consumer;
        }

        @Override
        protected void run(T res) {
            consumer.accept(res);

        }
    }

    public static class ErrorCompletion<T> extends Completion<T,T>{
        Consumer<Throwable> errorConsumer;
        public ErrorCompletion(Consumer<Throwable> errorConsumer) {
            this.errorConsumer = errorConsumer;
        }

        @Override
        protected void run(T res) {
            next.run(res);
        }

        @Override
        protected void error(Throwable err) {
            errorConsumer.accept(err);
        }
    }

    public static class ApplyCompletion<T,S> extends Completion<T,S>{
        Function<T, ListenableFuture<S>> function;

        public ApplyCompletion(Function<T, ListenableFuture<S>> function) {
            this.function = function;
        }

        @Override
        protected void run(T res) {
            ListenableFuture<S> listenableFuture = function.apply(res);
            listenableFuture.addCallback(
                    r -> complete(r),
                    e -> error(e)
            );
        }
    }


    public static class Completion<T,S>{

        Completion next;

        public void andAccept(Consumer<S> con){
            Completion<S,Void> c=new AcceptCompletion<S>(con);
            this.next=c;
        }

        public Completion<T,T> andError(Consumer<Throwable> errorConsumer){
            Completion<T,T> c=new ErrorCompletion<T>(errorConsumer);
            this.next=c;
            return c;
        }

        public <U> Completion<S,U> andApply(Function<S,ListenableFuture<U>> function){
            Completion<S,U> c=new ApplyCompletion<S,U>(function);
            this.next=c;
            return c;
        }

        public static <T,S> Completion<T,S> from(ListenableFuture<S> lf) {
            Completion<T,S> com=new Completion();
            lf.addCallback(
                    res -> com.complete(res)
                    , err -> com.error(err)
            );
            return com;
        }

        protected void error(Throwable err) {
            if(next!=null) next.error(err);
        }

        protected void complete(S res) {
            if(next!=null){
                next.run(res);
            }
        }

        protected void run(T res) {

        }
    }
}
