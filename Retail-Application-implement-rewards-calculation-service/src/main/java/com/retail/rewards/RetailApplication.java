package com.retail.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts the Spring Boot rewards application.
 */
@SpringBootApplication
public class RetailApplication {

    /**
     * Application entry point.
     *
     * @param args runtime arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RetailApplication.class, args);
    }
}
