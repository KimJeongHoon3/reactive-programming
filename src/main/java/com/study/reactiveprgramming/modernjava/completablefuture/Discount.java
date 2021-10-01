package com.study.reactiveprgramming.modernjava.completablefuture;

import static com.study.reactiveprgramming.modernjava.completablefuture.Shop.delay;
import static com.study.reactiveprgramming.modernjava.completablefuture.Shop.randomDelay;

public class Discount {
    public enum Code{
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote){
        return quote.getShopName() + " price is "+ Discount.apply(quote.getPrice(),quote.getCode());
    }

    private static double apply(double price, Code code) {
        randomDelay();
        return price * (100-code.percentage) / 100;
    }

}
