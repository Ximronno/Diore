package ximronno.diore.impl;

import ximronno.diore.Diore;
import ximronno.diore.api.events.DepositEvent;
import ximronno.diore.api.events.TransferEvent;
import ximronno.diore.api.events.WithdrawEvent;
import ximronno.diore.api.interfaces.Account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class FundAccount implements Account {

    private UUID owner;
    private String name;
    private double balance;
    public FundAccount(UUID owner, String name, double balance) {
        this.owner = owner;
        this.name = name;
        this.balance = balance;
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

}
