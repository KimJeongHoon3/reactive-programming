package com.study.reactiveprgramming.modernjava.completablefuture;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class ShopTest {
    @Test
    void testShop() throws ExecutionException, InterruptedException {
        Shop shop=new Shop("name");
        Future<Double> future = shop.getPriceAsync("abc");
        System.out.println(future.get());

        future = shop.getPriceAsync2("abc");
        System.out.println(future.get());
    }


}