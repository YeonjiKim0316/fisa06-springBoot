package com.example.fisa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Book {

    @Id // primary key를 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    private int page;
    private String title;
    private String author;
    private String genre;
    private int price;

}
