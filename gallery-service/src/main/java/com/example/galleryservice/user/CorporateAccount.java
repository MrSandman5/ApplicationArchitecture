package com.example.galleryservice.user;

import com.example.galleryservice.exceptions.NoSuchMoneyOnAccountException;
import com.example.galleryservice.project.Expo;
import lombok.SneakyThrows;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class CorporateAccount {

    private final Map<Expo, Double> balance = new HashMap<>();
    @NotNull
    private final String accountID;

    public CorporateAccount(@NotNull final String accountID) {
        this.accountID = accountID;
    }

    public double getBalance(@NotNull final Expo expo) {
        return balance.get(expo);
    }

    public String getAccountId() {
        return accountID;
    }

    public void deposit(@NotNull final Expo expo, final double amount) {
        if (this.balance.containsKey(expo)){
            final double tmp = this.balance.get(expo);
            this.balance.put(expo, tmp + amount);
        }
        else {
            this.balance.put(expo, amount);
        }
    }

    @SneakyThrows
    public void withdraw(@NotNull final Expo expo, final double amount) {
        if (this.balance.containsKey(expo)){
            final double tmp = this.balance.get(expo);
            this.balance.put(expo, tmp - amount);
        }
        else {
            throw new NoSuchMoneyOnAccountException("No enough money to withdraw!");
        }
    }

    public void transfer(@NotNull final Expo expo,
                         final double amount,
                         @NotNull final PrivateAccount to){
        withdraw(expo, amount);
        to.deposit(amount);
    }
}
