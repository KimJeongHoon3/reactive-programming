package com.study.reactiveprgramming.modernjava.completablefuture;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
public class Shop {

    private final String name;
    static Random random=new Random();

    private Discount.Code code;

    public Shop(String name) {

        this.name = name;
    }

    public Shop(String name,Discount.Code code) {
        this.name = name;
        this.code=code;
    }

    public Future<Double> getPriceAsync(String product){
        CompletableFuture<Double> completableFuture=new CompletableFuture<>();
        new Thread(()->{
            try{
                double price=calculatePrice(product);
//                if(price!=0)
//                    throw new RuntimeException("일부러 에러");
                completableFuture.complete(price);
            }catch(Exception e){
                completableFuture.completeExceptionally(e);
            }

        }).start();
        return completableFuture;
    }

    public Future<Double> getPriceAsync2(String product){

        return CompletableFuture.supplyAsync(()->calculatePrice(product));
    }

    private double calculatePrice(String product){
        delay();
        return random.nextDouble()*product.charAt(0) + product.charAt(1);
    }

    public static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void randomDelay() {
        int mills=500 + random.nextInt(2000);
        try {
            log.info("randomDelay : "+mills);
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }


    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public String getPriceForDiscount(String product) {
        return name+":"+calculatePrice(product)+":"+code;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                ", code=" + code +
                '}';
    }
}
