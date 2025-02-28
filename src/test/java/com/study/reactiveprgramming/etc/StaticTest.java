package com.study.reactiveprgramming.etc;

import org.junit.jupiter.api.Test;

public class StaticTest {
    static StaticTestObjectA s = new StaticTestObjectA();
    @Test
    void testStatic() {
        s.print1();
        StaticTestObjectB staticTestObjectB = (StaticTestObjectB)s;
        staticTestObjectB.print();
    }

    static class StaticTestObjectA extends StaticTestObjectB{
        void print1(){
            System.out.println("A");
        }
    }

    static class StaticTestObjectB {
        void print(){
            System.out.println("B");
        }

    }
}
