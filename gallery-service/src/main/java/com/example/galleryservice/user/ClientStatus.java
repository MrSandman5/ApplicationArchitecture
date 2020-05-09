package com.example.galleryservice.user;

import javax.validation.constraints.NotNull;

public enum ClientStatus {
    Ordinary, Corporate;

    public static String statusToStr(@NotNull final ClientStatus type) {
        switch (type) {
            case Ordinary:
                return "Ordinary";
            case Corporate:
                return "Corporate";
            default:
                return null;
        }
    }

    public static ClientStatus strToStatus(@NotNull final String st) {
        switch (st) {
            case "Ordinary":
                return Ordinary;
            case "Corporate":
                return Corporate;
            default:
                return null;
        }
    }
}
