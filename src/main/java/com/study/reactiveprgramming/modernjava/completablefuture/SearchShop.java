package com.study.reactiveprgramming.modernjava.completablefuture;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SearchShop {
    List<Shop> shops= Arrays.asList(new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"),
            new Shop("New1"),
            new Shop("New2"),
            new Shop("New3"),
            new Shop("New4"));

    List<Shop> shops2= Arrays.asList(new Shop("BestPrice", Discount.Code.GOLD),
            new Shop("LetsSaveBig",Discount.Code.GOLD),
            new Shop("MyFavoriteShop",Discount.Code.DIAMOND),
            new Shop("BuyItAll", Discount.Code.NONE),
            new Shop("New1", Discount.Code.PLATINUM),
            new Shop("New2", Discount.Code.SILVER),
            new Shop("New3", Discount.Code.NONE),
            new Shop("New4", Discount.Code.NONE));

    ExecutorService executorService= Executors.newFixedThreadPool(Math.min(shops.size(),100));

    public List<String> findPrices(String product){
//        return shops.stream().parallel()
//                .map(shop -> String.format("%s price is %.2f", shop.getName(),shop.getPrice(product)))
//                .collect(Collectors.toList());

        //CompletableFuture를 사용하여 개선한 버전
//         List<CompletableFuture<String>> futures=shops.stream()
//                .map(shop -> CompletableFuture.supplyAsync(()->String.format("%s price is %.2f", shop.getName(),shop.getPrice(product))))
//                .collect(Collectors.toList());
//         return futures.stream()
//                 .map(CompletableFuture::join)
//                 .collect(Collectors.toList());

        //CompletableFuture를 사용하여 개선한 버전2 (스레드풀의 스레드 갯수 지정)
        List<CompletableFuture<String>> futures=shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(()->{
                    log.info(shop.getName());
                    return String.format("%s price is %.2f", shop.getName(),shop.getPrice(product));
                },executorService))
                .collect(Collectors.toList());
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public List<String> findPricesWithDiscount(String product){
        log.info("start");
        List<CompletableFuture<String>> futures=shops2.stream()
                .map(shop -> CompletableFuture.supplyAsync(()->{ //새로운 스레드 worker 1 할당
                    log.info("map1 - "+shop.toString());
                    return shop.getPriceForDiscount(product);
                },executorService))
                .map(future -> future.thenApply( s ->{
                    log.info("map2 - "+s); // worker 1
                    return Quote.parse(s);
                }))
                .map(future -> future.thenCompose(quote -> {
                    log.info("map3 - "+quote.toString()); // worker 1
                    return CompletableFuture.supplyAsync(()->{
                        log.info("map3 new completableFuture - "+quote.toString()); //새로운 스레드 worker 2 할당
                        return Discount.applyDiscount(quote);
                    },executorService);
                }) )
                .collect(Collectors.toList());
        log.info("거의다끝");
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

    }

    public CompletableFuture[] findPricesWithDiscountAsArray_realtime(String product){
        log.info("start");
        return shops2.stream()
                .map(shop -> CompletableFuture.supplyAsync(()->{ //새로운 스레드 worker 1 할당
                    log.info("map1 - "+shop.toString());
                    return shop.getPriceForDiscount(product);
                },executorService))
                .map(future -> future.thenApply( s ->{
                    log.info("map2 - "+s); // worker 1
                    return Quote.parse(s);
                }))
                .map(future -> future.thenCompose(quote -> {
                    log.info("map3 - "+quote.toString()); // worker 1
                    return CompletableFuture.supplyAsync(()->{
                        log.info("map3 new completableFuture - "+quote.toString()); //새로운 스레드 worker 2 할당
                        return Discount.applyDiscount(quote);
                    },executorService);
                }))
                .map(future -> future.thenAccept(log::info)) //worker 2
//                .toArray(size -> new CompletableFuture[size]);
                .toArray(CompletableFuture[]::new);


    }
}
