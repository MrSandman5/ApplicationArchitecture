package com.example.galleryservice.model.gallery;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ticket {

    private long id;
    private Long client;
    private Long expo;
    private double cost;

    public Ticket(final Long client,
                  final Long expo,
                  final double cost) {
        this.client = client;
        this.expo = expo;
        this.cost = cost;
    }

}
