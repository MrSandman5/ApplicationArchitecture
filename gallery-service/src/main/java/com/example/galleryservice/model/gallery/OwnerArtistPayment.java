package com.example.galleryservice.model.gallery;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OwnerArtistPayment extends Payment {

    @NotNull
    private Long expo;
    @NotNull
    private Long owner;
    @NotNull
    private Long artist;

    public OwnerArtistPayment(final Long expo,
                              final Long owner,
                              final Long artist,
                              final double amount) {
        super(amount);
        this.expo = expo;
        this.owner = owner;
        this.artist = artist;
    }
}
