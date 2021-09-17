package com.study.reactiveprgramming.modernjava;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LambdaTest {
    @Test
    void 람다클래스_확인(){
        int a=10;
        Runnable runnable1=()->{
            int b=5;
        };

        Runnable runnable2=new Runnable() {
            @Override
            public void run() {
                int a=5;
            }
        };

        tempMethod(()-> System.out.println("lambda1"));
        tempMethod(()-> System.out.println("lambda2"));
        tempMethod(new Runnable() {
            @Override
            public void run() {
                System.out.println("anonymous");
            }
        });
        tempMethod(new RunnableImpl());
        tempMethod(new External());
        tempMethod(this::methodRef);
    }

    private void methodRef(){
        System.out.println("메서드 참조");
    }

    private void tempMethod(Runnable runnable){
        runnable.run();
        Class<? extends Runnable> aClass = runnable.getClass();
        System.out.println("class name : "+aClass.getName());
        System.out.println("class hashcode : "+aClass.hashCode());
        System.out.println("canonical class name : "+aClass.getCanonicalName());
        System.out.println("enclosing class name : "+aClass.getEnclosingClass());
        System.out.println();

    }

    static class RunnableImpl implements Runnable{

        @Override
        public void run() {
            System.out.println("static class");
        }
    }
}

class External implements Runnable{

    @Override
    public void run() {
        System.out.println("external class");
    }
}
