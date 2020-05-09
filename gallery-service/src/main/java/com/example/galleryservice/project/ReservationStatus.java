package com.example.galleryservice.project;

import javax.validation.constraints.NotNull;

public enum ReservationStatus {
    New, WaitPay, Payed, WaitCashierPay, WaitAccessManager;

    public static String statusToStr(@NotNull final ReservationStatus type) {
        switch (type) {
            case New:
                return "New";
            case WaitPay:
                return "WaitPay";
            case Payed:
                return "Payed";
            case WaitCashierPay:
                return "WaitCashierPay";
            case WaitAccessManager:
                return "WaitAccessManager";
            default:
                return null;
        }
    }

    public static ReservationStatus strToStatus(@NotNull final String st) {
        switch (st) {
            case "New":
                return New;
            case "WaitPay":
                return WaitPay;
            case "Payed":
                return Payed;
            case "WaitCashierPay":
                return WaitCashierPay;
            case "WaitAccessManager":
                return WaitAccessManager;
            default:
                return null;
        }
    }
}
