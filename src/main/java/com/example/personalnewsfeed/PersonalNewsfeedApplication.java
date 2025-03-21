package com.example.personalnewsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PersonalNewsfeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalNewsfeedApplication.class, args);
    }

}
