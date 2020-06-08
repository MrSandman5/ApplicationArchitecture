package com.example.galleryservice.model.project;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Payment {

    private long id;
    private LocalDateTime date;
    private Double amount;

    public Payment(final Double amount) {
        this.date = LocalDateTime.now();
        this.amount = amount;
    }
}
