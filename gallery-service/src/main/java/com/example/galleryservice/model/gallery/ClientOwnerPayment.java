package com.example.galleryservice.model.gallery;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ClientOwnerPayment extends Payment {

    @NotNull
    private Long reservation;
    @NotNull
    private Long client;
    @NotNull
    private Long owner;

    public ClientOwnerPayment(final Long reservation,
                  final Long client,
                  final Long owner,
                  final double amount){
        super(amount);
        this.reservation = reservation;
        this.client = client;
        this.owner = owner;
    }

}
