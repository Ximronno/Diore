package ximronno.diore.api.account.managment;

import ximronno.diore.api.account.Account;

import java.util.Locale;

public interface AccountInfoFormatter {

    String getFormattedAmount(double amount, Locale locale);

    String getFormattedAmount(double amount);

    String getFormattedBalance(Account sender, Account target, Locale senderLocale);

    String getFormattedBalance(Account acc, Locale locale);

    String getFormattedBalanceStatus(Account acc, Locale locale);

}
