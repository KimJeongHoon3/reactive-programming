package com.study.reactiveprgramming.toby.chap5async2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/async2/rest")
    public String rest(int idx){
        return "async2 rest "+idx;
    }
}
