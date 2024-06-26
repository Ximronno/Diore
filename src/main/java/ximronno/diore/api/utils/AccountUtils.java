package ximronno.diore.api.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ximronno.diore.Diore;
import ximronno.diore.api.interfaces.Account;
import ximronno.diore.impl.Languages;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.ConfigManager;

public class AccountUtils {

    private static final Diore plugin = Diore.getInstance();
    private static final AccountManager accountManager = plugin.getAccountManager();
    private static final ConfigManager configManager = plugin.getConfigManager();

    public static boolean tryWithdraw(Player p, Account acc, FileConfiguration config, double amount) {
        if(!acc.withdraw(amount)) {
            p.sendMessage(configManager.getFormattedString("something-went-wrong", config));
            return false;
        }
        return true;
    }
    public static boolean tryDeposit(Player p, Account acc, FileConfiguration config, double amount) {
        if(!acc.deposit(amount)) {
            p.sendMessage(configManager.getFormattedString("something-went-wrong", config));
            return false;
        }
        return true;
    }
    public static boolean tryTransfer(Player sender, Account from, Account to, FileConfiguration config, double amount) {

        if(to == null) {
            sender.sendMessage(configManager.getFormattedString("player-has-no-account", config));
            return true;
        }
        if(isSameAccount(from, to)) {
            sender.sendMessage(configManager.getFormattedString("cannot-transfer-to-your-own-account", config));
            return true;
        }

        if(!from.transfer(to, amount)) {
            sender.sendMessage(configManager.getFormattedString("something-went-wrong", config));
            return false;
        }
        return true;
    }
    public static void setLanguage(Player p, Account acc, FileConfiguration config, Languages languageToSet) {

        acc.setLanguage(languageToSet);

        p.sendMessage(configManager.getFormattedString("language-set", config)
                .replace("<language>", languageToSet.getName()));
    }
    public static void setPublicBalance(Player p, Account acc, FileConfiguration config, boolean publicToSet) {
        acc.setPublicBalance(publicToSet);

        if(publicToSet) {
            p.sendMessage(configManager.getFormattedString("public-balance-enabled", config));
        }
        else {
            p.sendMessage(configManager.getFormattedString("public-balance-disabled", config));
        }
    }
    private static boolean isSameAccount(Account a1, Account a2) {
        return a1.getOwner().equals(a2.getOwner());
    }


}
