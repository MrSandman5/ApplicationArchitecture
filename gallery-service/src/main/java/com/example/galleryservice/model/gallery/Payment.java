package com.example.galleryservice.model.gallery;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Payment {

    private long id;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;
    @Min(value = 0, message = "must be greater than or equal to zero")
    private double price;

    public Payment(final double price) {
        this.dateTime = LocalDateTime.now();
        this.price = price;
    }
}
