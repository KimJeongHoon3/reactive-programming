package com.study.reactiveprgramming.modernjava.completablefuture;

public class Quote {
    private final String shopName;
    private final double price;
    private final Discount.Code code;

    public Quote(String shopName, double price, Discount.Code code){
        this.shopName = shopName;
        this.price = price;
        this.code = code;
    }

    public static Quote parse(String s){
        String[] split=s.split(":");
        return new Quote(split[0].trim(),Double.parseDouble(split[1].trim()),Discount.Code.valueOf(split[2].trim()));
    }

    public String getShopName() {
        return shopName;
    }

    public double getPrice() {
        return price;
    }

    public Discount.Code getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "shopName='" + shopName + '\'' +
                ", price=" + price +
                ", code=" + code +
                '}';
    }
}
