package ximronno.bukkit.account;


import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DioreAccount extends Account {

    private List<Transaction> recentTransactions;

    private final int maxTransactions;

    public DioreAccount(UUID uuid, Locale locale, double balance, boolean publicBalance, List<Transaction> recentTransactions) {
        super(uuid, locale, balance, publicBalance);
        this.maxTransactions = DioreAPI.getInstance().getMainConfig().getMaxSavedRecentTransactions();
        setRecentTransactions(recentTransactions);
    }

    public static DioreAccountBuilder builder() {
        return new DioreAccountBuilder();
    }

    @Override
    public void deposit(double amount) {
        BigDecimal balance = BigDecimal.valueOf(getBalance());
        BigDecimal decimalAmount = BigDecimal.valueOf(amount);
        BigDecimal toDeposit = balance.add(decimalAmount);
        setBalance(toDeposit.doubleValue());
    }

    @Override
    public void withdraw(double amount) {
        BigDecimal balance = BigDecimal.valueOf(getBalance());
        BigDecimal decimalAmount = BigDecimal.valueOf(amount);
        BigDecimal toWithdraw = balance.subtract(decimalAmount);
        setBalance(toWithdraw.doubleValue());
    }

    @Override
    public void transfer(Account account, double amount) {
        this.withdraw(amount);
        account.deposit(amount);
    }


    @Override
    public List<Transaction> getRecentTransactions() {
        return recentTransactions;
    }

    @Override
    public void setRecentTransactions(List<Transaction> recentTransactions) {
        this.recentTransactions = recentTransactions;
        if(!DioreAPI.getInstance().getMainConfig().getSQLConfig().isEnabled()) {
            while(this.recentTransactions.size() > this.maxTransactions) {
                this.recentTransactions.remove(0);
            }
        }
    }

    @Override
    public void addRecentTransaction(Transaction transaction) {
        if(!DioreAPI.getInstance().getMainConfig().getSQLConfig().isEnabled()) {
            if(recentTransactions.size() >= this.maxTransactions) {
                recentTransactions.remove(0);
            }
        }
        recentTransactions.add(transaction);
    }
}
