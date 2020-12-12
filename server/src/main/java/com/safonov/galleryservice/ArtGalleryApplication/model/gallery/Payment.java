package com.safonov.galleryservice.ArtGalleryApplication.model.gallery;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @Min(value = 0, message = "must be greater than or equal to zero")
    @Column(name = "price")
    private double price;

    public Payment(final double price) {
        this.dateTime = LocalDateTime.now();
        this.price = price;
    }
}