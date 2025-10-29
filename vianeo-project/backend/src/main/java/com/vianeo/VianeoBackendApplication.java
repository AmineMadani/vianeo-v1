package com.vianeo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VianeoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VianeoBackendApplication.class, args);
    }
}