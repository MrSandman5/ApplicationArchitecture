package com.safonov.galleryservice.ArtGalleryApplication.configuration;

import java.time.format.DateTimeFormatter;

public final class Constants {

    public static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public enum Roles {
        ROLE_ADMIN,
        ROLE_CLIENT,
        ROLE_OWNER,
        ROLE_ARTIST
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
