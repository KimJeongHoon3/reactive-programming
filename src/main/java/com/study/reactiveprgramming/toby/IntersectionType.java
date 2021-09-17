package com.study.reactiveprgramming.toby;

import java.util.function.Consumer;

public class IntersectionType {
    interface DelegateTo<T>{
        T delegate();
    }

    interface Hello extends DelegateTo<String>{
        default void hello(){
            System.out.println("Hello "+delegate());
        }
    }

    interface UpperCase extends DelegateTo<String>{
        default void uppercase(){
            System.out.println(delegate().toUpperCase());
        }
    }

    public static void main(String[] args) {
        run((DelegateTo<String> & Hello & UpperCase)()->"hi",d -> {
            d.hello();
            d.uppercase();
        });
    }

    private static <T extends DelegateTo<S>,S> void run(T t, Consumer<T> consumer) {
        consumer.accept(t);
    }


}
