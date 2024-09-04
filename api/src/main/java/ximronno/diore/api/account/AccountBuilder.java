package ximronno.diore.api.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public abstract class AccountBuilder {

    protected UUID uuid;

    protected Locale locale;

    protected double balance;

    protected boolean publicBalance;

    public AccountBuilder(Locale defLocale) {
        this.locale = defLocale;
        this.balance = 0D;
        this.publicBalance = true;
    }

    public AccountBuilder setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public AccountBuilder setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public AccountBuilder setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public AccountBuilder setPublicBalance(boolean publicBalance) {
        this.publicBalance = publicBalance;
        return this;
    }


    public abstract Account build();

}
