package com.ssmoker.smoker.test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String content;

    private String imageUrl;

    public Test(String name, String content, String imageUrl) {
        this.name = name;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
