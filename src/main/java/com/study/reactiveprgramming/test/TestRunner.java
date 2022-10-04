package com.study.reactiveprgramming.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
public class TestRunner {
    @Autowired
    MyBeanCreationTest myBeanCreationTest;

    public void method1() {
        myBeanCreationTest.method1();
    }
}
