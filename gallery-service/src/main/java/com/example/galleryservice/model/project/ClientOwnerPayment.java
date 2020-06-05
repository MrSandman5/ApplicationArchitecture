package com.example.galleryservice.model.project;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ClientOwnerPayment extends Payment {

    private long reservation;
    private long client;
    private long owner;

    public ClientOwnerPayment(final long reservation,
                  final long client,
                  final long owner,
                  final double amount){
        super(amount);
        this.reservation = reservation;
        this.client = client;
        this.owner = owner;
    }

}
