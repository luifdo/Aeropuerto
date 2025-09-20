package com.pw.aerropuerto;

import org.springframework.boot.SpringApplication;

public class TestAerropuertoApplication {

    public static void main(String[] args) {
        SpringApplication.from(AerropuertoApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
