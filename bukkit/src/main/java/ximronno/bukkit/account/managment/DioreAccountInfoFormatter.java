package ximronno.bukkit.account.managment;

import ximronno.bukkit.message.type.FormatPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.managment.AccountInfoFormatter;
import ximronno.diore.api.message.MessageManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

public class DioreAccountInfoFormatter implements AccountInfoFormatter {

    private final MessageManager messageManager;

    private final DioreAPI api;

    public DioreAccountInfoFormatter(DioreAPI api) {
        this.messageManager = api.getMessageManager();
        this.api = api;
    }

    @Override
    public String getFormattedBalance(Account sender, Account target, Locale senderLocale) {
        if(target.isPrivateBalance()) {
            return formattedBalanceStatus(true, senderLocale);
        }
        else {
            return getFormattedBalance(target, senderLocale);
        }
    }

    @Override
    public String getFormattedBalance(Account acc, Locale locale) {
        double balance = acc.getBalance();
        return getFormattedAmount(balance, locale);
    }

    @Override
    public String getFormattedAmount(double amount, Locale locale) {
        if(amount < 0) {
            return messageManager.getMessage(FormatPaths.NEGATIVE_AMOUNT_FORMAT, locale, true, Map.of(
                    "{amount}", format(amount)
            ));
        }
        else {
            return messageManager.getMessage(FormatPaths.AMOUNT_FORMAT, locale, true, Map.of(
                    "{amount}", format(amount)
            ));
        }
    }

    @Override
    public String getFormattedAmount(double amount) {
        return getFormattedAmount(amount, api.getMessageManager().getMessageProvider().getDefaultLanguage());
    }

    @Override
    public String getFormattedBalanceStatus(Account acc, Locale locale) {
        return formattedBalanceStatus(acc.isPrivateBalance(), locale);
    }

    private String formattedBalanceStatus(boolean privateBool, Locale locale) {
        if(privateBool) {

            return messageManager.getMessage(FormatPaths.PRIVATE_FORMAT, locale, true);

        }
        else {

            return messageManager.getMessage(FormatPaths.PUBLIC_FORMAT, locale, true);

        }
    }

    private String format(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        String pattern = "#,##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);

        return decimalFormat.format(amount);
    }
}
