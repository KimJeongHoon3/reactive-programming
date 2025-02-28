package com.study.reactiveprgramming.toby.doubledispatch;

public class DoubleDispatch {

    public static void main(String[] args) {
        AbstractTest abstractTest = new ConcreteTest1();
        abstractTest.run();
    }
}
