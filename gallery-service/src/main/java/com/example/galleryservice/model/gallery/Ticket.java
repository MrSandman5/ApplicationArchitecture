package com.example.galleryservice.model.gallery;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class Ticket {

    private long id;
    @NotNull
    private Long client;
    @NotNull
    private Long expo;
    @Min(value = 0, message = "must be greater than or equal to zero")
    private double cost;

    public Ticket(final Long client,
                  final Long expo,
                  final double cost) {
        this.client = client;
        this.expo = expo;
        this.cost = cost;
    }

}
