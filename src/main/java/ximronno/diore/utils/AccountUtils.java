package ximronno.diore.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.diore.Diore;
import ximronno.diore.model.ConfigManager;

public class AccountUtils {

    private static final Diore plugin = Diore.getInstance();
    private static final ConfigManager configManager = plugin.getConfigManager();

    public static boolean tryWithdraw(Player p, Account acc, FileConfiguration config, double amount) {
        if(!acc.withdraw(amount)) {
            p.sendMessage(configManager.getFormattedString(config, "something-went-wrong"));
            return false;
        }
        return true;
    }
    public static boolean tryDeposit(Player p, Account acc, FileConfiguration config, double amount) {
        if(!acc.deposit(amount)) {
            p.sendMessage(configManager.getFormattedString(config, "something-went-wrong"));
            return false;
        }
        return true;
    }
    public static boolean tryTransfer(Player sender, Account from, Account to, FileConfiguration config, double amount) {

        if(to == null) {
            sender.sendMessage(configManager.getFormattedString(config, "player-has-no-account"));
            return true;
        }
        if(isSameAccount(from, to)) {
            sender.sendMessage(configManager.getFormattedString(config, "cannot-transfer-to-your-own-account"));
            return true;
        }

        if(!from.transfer(to, amount)) {
            sender.sendMessage(configManager.getFormattedString(config, "something-went-wrong"));
            return false;
        }
        return true;
    }
    public static void setLanguage(Player p, Account acc, Language languageToSet) {

        acc.setLanguage(languageToSet);

        p.sendMessage(configManager.getFormattedString(languageToSet.getConfig(), "language-set")
                .replace("<language>", languageToSet.getName()));
    }
    public static void setPublicBalance(Player p, Account acc, FileConfiguration config, boolean publicToSet) {
        acc.setPublicBalance(publicToSet);

        if(publicToSet) {
            p.sendMessage(configManager.getFormattedString(config, "public-balance-enabled"));
        }
        else {
            p.sendMessage(configManager.getFormattedString(config, "public-balance-disabled"));
        }
    }
    private static boolean isSameAccount(Account a1, Account a2) {
        return a1.getOwner().equals(a2.getOwner());
    }


}
