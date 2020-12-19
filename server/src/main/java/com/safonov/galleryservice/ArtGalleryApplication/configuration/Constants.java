package com.safonov.galleryservice.ArtGalleryApplication.configuration;

import java.time.format.DateTimeFormatter;

public final class Constants {

    public static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

    public enum EditSettings {
        Name,
        Info,
        StartTime,
        EndTime
    }
}
