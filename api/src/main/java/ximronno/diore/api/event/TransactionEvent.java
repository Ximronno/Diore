package ximronno.diore.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.AccountResponse;

public abstract class TransactionEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

     protected final Account account;

     protected boolean isCancelled;

     protected AccountResponse response = AccountResponse.SUCCESS;

     protected double amount;

    public TransactionEvent(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public AccountResponse getResponse() {
        return response;
    }

    public void setResponse(AccountResponse response) {
        this.response = response;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
