package ru.otus.fin.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
        System.out.printf("Адрес api: %n%s%n",
                "http://localhost:8686/api/v1");

    }

}
