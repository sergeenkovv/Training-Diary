package com.ivan.trainingdiary;

import com.ivan.auditstarter.annotation.EnableAudit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAudit
@SpringBootApplication
public class TrainingDiaryApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrainingDiaryApplication.class, args);
    }
}