package com.study.reactiveprgramming.etc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketTest {

    @Test
    void test() throws IOException {
        ServerSocket s = new ServerSocket(8011);
        s.accept();

    }
}
