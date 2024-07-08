package ximronno.diore.impl;

import ximronno.api.events.DepositEvent;
import ximronno.api.events.TransferEvent;
import ximronno.api.events.WithdrawEvent;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.api.interfaces.Transaction;
import ximronno.diore.Diore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FundAccount implements Account {

    private UUID owner;
    private String name;
    private double balance;
    private Language language;
    private boolean publicBalance;
    private final List<Transaction> transactions;

    public FundAccount(UUID owner, String name, double balance, Language language, boolean publicBalance, List<Transaction> transactions) {
        this.owner = owner;
        this.name = name;
        this.balance = balance;
        this.language = language != null ? language : Languages.ENGLISH;
        this.publicBalance = publicBalance;
        this.transactions = transactions != null ? transactions : new ArrayList<>();
    }

    public FundAccount(UUID owner, String name, double balance, Language language, boolean publicBalance) {
        this(owner, name, balance, language, publicBalance, new ArrayList<>());
    }
    public FundAccount(UUID owner, String name, double balance) {
        this(owner, name, balance, Languages.ENGLISH, true, new ArrayList<>());

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

        TransferEvent event = new TransferEvent(this, account, amount);
        Diore.getInstance().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) return false;

        balance -= amount;
        addTransaction(new Transaction(-amount, System.currentTimeMillis()));
        account.receive(amount);

        return true;

    }

    @Override
    public boolean withdraw(double amount) {

        BigDecimal bd = new BigDecimal(Double.toString(amount));
        bd = bd.setScale(1, RoundingMode.FLOOR);

        amount = bd.doubleValue();

        if(balance < amount) return false;

        WithdrawEvent event = new WithdrawEvent(this, amount);
        Diore.getInstance().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) return false;

        balance -= amount;
        addTransaction(new Transaction(-amount, System.currentTimeMillis()));

        return true;

    }

    @Override
    public boolean deposit(double amount) {

        BigDecimal bd = new BigDecimal(Double.toString(amount));
        bd = bd.setScale(1, RoundingMode.FLOOR);

        amount = bd.doubleValue();

        DepositEvent event = new DepositEvent(this, amount);
        Diore.getInstance().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) return false;

        balance += amount;
        addTransaction(new Transaction(amount, System.currentTimeMillis()));

        return true;
    }
    @Override
    public void receive(double amount) {
        balance += amount;
        addTransaction(new Transaction(amount, System.currentTimeMillis()));
    }

    @Override
    public Language getLanguage() {
        return this.language;
    }

    @Override
    public void setLanguage(Language language) {
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

    @Override
    public void addTransaction(Transaction transaction) {
        if(transactions.size() == 5) transactions.remove(0);
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}
