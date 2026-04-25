package com.eiousee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class WebProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebProjectApplication.class, args);
    }

}
