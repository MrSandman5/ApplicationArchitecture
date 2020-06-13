package com.example.galleryservice.model.gallery;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ClientOwnerPayment extends Payment {

    private Long reservation;
    private Long client;
    private Long owner;

    public ClientOwnerPayment(final Long reservation,
                  final Long client,
                  final Long owner,
                  final Double amount){
        super(amount);
        this.reservation = reservation;
        this.client = client;
        this.owner = owner;
    }

}
