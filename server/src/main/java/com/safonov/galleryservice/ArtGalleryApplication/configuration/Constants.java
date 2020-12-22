package com.safonov.galleryservice.ArtGalleryApplication.configuration;

import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;

public final class Constants {

    public static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public enum Roles {
        ROLE_CLIENT("ROLE_CLIENT"),
        ROLE_OWNER("ROLE_OWNER"),
        ROLE_ARTIST("ROLE_ARTIST"),
        ROLE_ADMIN("ROLE_ADMIN");

        private final String code;

        Roles(@NotNull final String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
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

    public enum EditSettings {
        Name,
        Info,
        StartTime,
        EndTime
    }
}
