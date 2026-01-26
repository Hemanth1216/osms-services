package com.example.learn.config;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class YamlProof {
    @Value("${app.proof:NOT_FOUND}")
    private String proof;

    @PostConstruct
    public void print() {
        System.out.println("YAML PROOF = " + proof);
    }
}
