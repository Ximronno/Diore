package ximronno.bukkit.account;


import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.AccountBuilder;
import ximronno.diore.api.account.Transaction;

import java.util.ArrayList;
import java.util.List;


public class DioreAccountBuilder extends AccountBuilder {

    protected List<Transaction> recentTransactions;

    public DioreAccountBuilder() {
        super(DioreAPI.getInstance().getMessageManager().getMessageProvider().getDefaultLanguage());
        this.recentTransactions = new ArrayList<>();
    }

    public AccountBuilder setRecentTransactions(List<Transaction> recentTransactions) {
        this.recentTransactions = recentTransactions;
        return this;
    }

    @Override
    public Account build() {
        assert uuid != null;
        return new DioreAccount(uuid, locale, balance, publicBalance, recentTransactions);
    }

}
