package com.safonov.galleryservice.ArtGalleryApplication.configuration;

public final class Constants {

    public enum UserType {
        Client,
        Owner,
        Artist
    }

    public enum ReservationStatus {
        New,
        Payed,
        Closed
    }

    public enum ExpoStatus {
        New,
        Opened,
        Closed
    }
}
