package com.example.galleryservice.model.project;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OwnerArtistPayment extends Payment {

    private long expo;
    private long owner;
    private long artist;

    public OwnerArtistPayment(final long expo,
                              final long owner,
                              final long artist,
                              final double amount) {
        super(amount);
        this.expo = expo;
        this.owner = owner;
        this.artist = artist;
    }
}
