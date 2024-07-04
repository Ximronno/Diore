package ximronno.diore.impl;

import ximronno.diore.Diore;
import ximronno.api.events.DepositEvent;
import ximronno.api.events.TransferEvent;
import ximronno.api.events.WithdrawEvent;
import ximronno.api.interfaces.Account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class FundAccount implements Account {

    private UUID owner;
    private String name;
    private double balance;
    private Languages language;
    private boolean publicBalance;
    public FundAccount(UUID owner, String name, double balance, Languages language, boolean publicBalance) {
        this.owner = owner;
        this.name = name;
        this.balance = balance;
        this.language = language;
        this.publicBalance = publicBalance;
    }
    public FundAccount(UUID owner, String name, double balance, Languages language) {
        this(owner, name, balance, language, true);
    }
    public FundAccount(UUID owner, String name, double balance) {
        this(owner, name, balance, Languages.ENGLISH, true);

    }


    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public boolean transfer(Account account, double amount) {

        if(balance < amount) return false;

        balance -= amount;

        TransferEvent event = new TransferEvent(this, account, amount);
        Diore.getInstance().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) return false;

        account.receive(amount);

        return true;

    }

    @Override
    public boolean withdraw(double amount) {

        BigDecimal bd = new BigDecimal(Double.toString(amount));
        bd = bd.setScale(1, RoundingMode.HALF_UP);

        amount = bd.doubleValue();

        if(balance < amount) return false;

        WithdrawEvent event = new WithdrawEvent(this, amount);
        Diore.getInstance().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) return false;

        balance -= amount;

        return true;

    }

    @Override
    public boolean deposit(double amount) {

        BigDecimal bd = new BigDecimal(Double.toString(amount));
        bd = bd.setScale(1, RoundingMode.HALF_UP);

        amount = bd.doubleValue();

        DepositEvent event = new DepositEvent(this, amount);
        Diore.getInstance().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) return false;

        balance += amount;

        return true;
    }
    @Override
    public void receive(double amount) {
        balance += amount;
    }

    @Override
    public Languages getLanguage() {
        return this.language;
    }

    @Override
    public void setLanguage(Languages language) {
        this.language = language;
    }
    @Override
    public boolean isPublicBalance() {
        return publicBalance;
    }

    @Override
    public void setPublicBalance(boolean publicBalance) {
        this.publicBalance = publicBalance;
    }
}
