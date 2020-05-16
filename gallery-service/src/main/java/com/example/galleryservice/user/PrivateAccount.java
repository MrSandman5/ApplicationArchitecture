package com.example.galleryservice.user;

import com.example.galleryservice.exceptions.NoSuchMoneyOnAccountException;
import com.example.galleryservice.project.Expo;
import lombok.SneakyThrows;

import javax.validation.constraints.NotNull;

public class PrivateAccount {

    private double balance;
    @NotNull
    private final String accountID;

    public PrivateAccount(final double balance, @NotNull final String accountID) {
        this.balance = balance > 0 ? balance : 0;
        this.accountID = accountID;
    }

    public PrivateAccount(@NotNull final String accountID) {
        this.accountID = accountID;
        this.balance = 0;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(final double balance) {
        this.balance = balance;
    }

    public String getAccountId() {
        return accountID;
    }

    public void deposit(final double amount) {
        balance += amount;
    }

    @SneakyThrows
    public void withdraw(final double amount) {
        if (balance < amount){
            throw new NoSuchMoneyOnAccountException("No enough money to withdraw!");
        }
        balance -= amount;
    }

    public void transfer(@NotNull final Expo expo,
                         final double amount,
                         @NotNull final CorporateAccount to){
        withdraw(amount);
        to.deposit(expo, amount);
    }

}
