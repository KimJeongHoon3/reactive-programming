package com.study.reactiveprgramming.modernjava.completablefuture;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Shop {

    private final String name;
    Random random=new Random();

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
