package com.study.reactiveprgramming.toby;

import java.util.function.Consumer;

public class IntersectionType2 {
    interface Pair<T>{
        T getFirst();
        T getSecond();
        void setFirst(T first);
        void setSecond(T second);
    }

    interface DelegateTo<T>{ //이 인터페이스를 통해서 범용적으로 사용할수있음!!!@#!@#
        T delegate();
    }

    interface ForwardingPair<T> extends DelegateTo<Pair<T>>{
        default void addAction(){
            System.out.println(delegate().getFirst()+"동적추가");
            System.out.println(delegate().getSecond()+"동적추가");
        }
    }

    static class Name implements Pair<String>{
        String firstName;
        String lastName;

        public Name(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @Override
        public String getFirst() {
            return this.firstName;
        }

        @Override
        public String getSecond() {
            return this.lastName;
        }

        @Override
        public void setFirst(String first) {
            this.firstName=first;
        }

        @Override
        public void setSecond(String second) {
            this.lastName=second;
        }
    }

    public static void main(String[] args) {
        Pair<String> name=new Name("Kim","JH");

        run((DelegateTo<Pair<String>> & ForwardingPair<String>) ()->name,d -> {
            System.out.println(d.delegate().getFirst());
            System.out.println(d.delegate().getSecond());
            d.addAction();
        });
    }

    private static <T extends DelegateTo<S>,S> void run(T t, Consumer<T> consumer){
        consumer.accept(t);
    }
}
