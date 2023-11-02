package com.envers.enverspilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EnversPilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnversPilotApplication.class, args);
    }

}
