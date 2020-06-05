package com.example.galleryservice.model.project;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ticket {

    private long id;
    private long client;
    private long expo;
    private double cost;

    public Ticket(final long client,
                  final long expo,
                  final double cost) {
        this.client = client;
        this.expo = expo;
        this.cost = cost;
    }

}
