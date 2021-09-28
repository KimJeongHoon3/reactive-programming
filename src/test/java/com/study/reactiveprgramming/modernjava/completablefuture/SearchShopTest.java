package com.study.reactiveprgramming.modernjava.completablefuture;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}