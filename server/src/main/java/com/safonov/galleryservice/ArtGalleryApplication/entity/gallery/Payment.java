package com.safonov.galleryservice.ArtGalleryApplication.entity.gallery;

import com.safonov.galleryservice.ArtGalleryApplication.entity.AbstractEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class Payment extends AbstractEntity {

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "dateTime", nullable = false)
    private LocalDateTime dateTime;

    @Min(value = 0, message = "must be greater than or equal to zero")
    @Column(name = "price", nullable = false)
    private Double price;

    public Payment(final Double price) {
        this.dateTime = LocalDateTime.now();
        this.price = price;
    }
}
