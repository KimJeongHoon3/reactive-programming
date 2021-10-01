package com.study.reactiveprgramming.modernjava.completablefuture;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

class SearchShopTest {
    @Test
    void testSearchShop(){
        SearchShop searchShop=new SearchShop();
        System.out.println(searchShop.findPrices("oioi"));
    }

    @Test
    void testSearchShop2(){
        SearchShop searchShop=new SearchShop();
        System.out.println(searchShop.findPricesWithDiscount("oioi"));
    }

    @Test
    void 실시간으로_결과넘겨받음(){
        SearchShop searchShop=new SearchShop();
        ;
        CompletableFuture[] completableFutures=searchShop.findPricesWithDiscountAsArray_realtime("oioi");

        CompletableFuture.allOf(completableFutures).join();



    }
}