package com.safonov.galleryservice.ArtGalleryApplication.configuration;

import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;

public final class Constants {

    public static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public enum Roles {
        ROLE_ADMIN("admin"),
        ROLE_CLIENT("client"),
        ROLE_OWNER("owner"),
        ROLE_ARTIST("artist");

        private final String role;

        Roles(@NotNull final String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
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
