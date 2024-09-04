package ximronno.diore.api.account.managment;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ximronno.diore.api.account.Account;

import java.util.Locale;

public interface AccountController {

    boolean setLocale(Player p, Account acc, Locale locale);

    boolean setPrivateBalance(Player p, Account acc, Locale locale, boolean publicToSet);

    boolean withdraw(Player p, Account acc, Locale locale, double amount);

    boolean deposit(Player p, Account acc, Locale locale, double amount);

    boolean transfer(Player sender, OfflinePlayer target, Account from, Account to, Locale senderLocale, double amount);

}
