package com.cvizard.pdfmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PdfmakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdfmakerApplication.class, args);
    }

}
