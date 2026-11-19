package com.mandi.produce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProduceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProduceServiceApplication.class, args);
    }
}
