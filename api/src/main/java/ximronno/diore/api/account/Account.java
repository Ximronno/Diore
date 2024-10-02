package ximronno.diore.api.account;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public abstract class Account {

    protected final UUID uuid;

    private Locale locale;

    private double balance;

    private boolean privateBalance;

    public Account(UUID uuid, Locale locale, double balance, boolean privateBalance){
        this.uuid = uuid;
        this.locale = locale;
        this.balance = balance;
        this.privateBalance = privateBalance;
    }

    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    public abstract void transfer(Account account, double amount);

    public boolean canWithdraw(double amount) {
        return amount <= balance;
    }

    public boolean canTransfer(double amount) {
        return amount <= balance;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isPrivateBalance() {
        return privateBalance;
    }

    public void setPrivateBalance(boolean privateBalance) {
        this.privateBalance = privateBalance;
    }

    public abstract List<Transaction> getRecentTransactions();

    public abstract void addRecentTransaction(Transaction transaction);

    public abstract void setRecentTransactions(List<Transaction> recentTransactions);
}
