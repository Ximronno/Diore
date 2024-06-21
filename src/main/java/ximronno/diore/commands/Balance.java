package ximronno.diore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ximronno.diore.Diore;
import ximronno.diore.api.interfaces.Account;
import ximronno.diore.impl.Languages;
import ximronno.diore.impl.TopBalance;
import ximronno.diore.model.AccountManager;

import java.util.List;


public class Balance implements CommandExecutor {

    private final Diore plugin;

    public Balance(Diore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {

        if(!(commandSender instanceof Player p)) return false;

        AccountManager am = Diore.getInstance().getAccountManager();

        Account acc = am.getAccount(p.getUniqueId()).orElse(null);

        if(acc == null) {
            p.sendMessage(plugin.getConfigManager().getFormattedString("no-account-found"));
            return true;
        }

        Languages language = acc.getLanguage();

        if(strings.length == 0) {
            p.sendMessage(plugin.getConfigManager().getFormattedString("on-balance", language.getCFG()).replace("<balance>", am.formatBalance(acc.getBalance())));
        }
        else if(strings.length == 1) {
            String func = strings[0].toUpperCase();

            if (func.equals("TOP")) {
                List<TopBalance> topBalances = am.getTopBalances();

                for (int i = 0; i < Math.min(topBalances.size(), 5); i++) {
                    TopBalance topBalance = topBalances.get(i);
                    p.sendMessage(i + 1 + ". " + topBalance.account().getName() + " - " + am.formatBalance(topBalance.balance()));
                }
            }

        }
        else if(strings.length == 2 && strings[0].toUpperCase().equals("LANGUAGE")) {
            String lang = strings[1].toUpperCase();
            Languages languageToSet;
            try {
                languageToSet = Languages.valueOf(lang);
            } catch (Exception e) {
                p.sendMessage(plugin.getConfigManager().getFormattedString("language-not-found", language.getCFG()));
                return true;
            }

            acc.setLanguage(languageToSet);

            p.sendMessage(plugin.getConfigManager().getFormattedString("language-set", language.getCFG())
                    .replace("<language>", languageToSet.name()));
        }
        else if(strings.length == 2 && strings[0].toUpperCase().equals("PUBLIC")) {

            boolean publicToSet = Boolean.parseBoolean(strings[1]);
            acc.setPublicBalance(publicToSet);

            if(publicToSet) {
                p.sendMessage(plugin.getConfigManager().getFormattedString("public-balance-enabled", language.getCFG()));
            }
            else {
                p.sendMessage(plugin.getConfigManager().getFormattedString("public-balance-disabled", language.getCFG()));
            }

        }
        else if(strings.length == 2) {
            String func = strings[0].toUpperCase();

            double amount;
            try {
                amount = Double.parseDouble(strings[1]);
            } catch (Exception e) {
                return false;
            }

            switch (func) {
                case "WITHDRAW":
                    if(!acc.withdraw(amount)) {
                        p.sendMessage(plugin.getConfigManager().getFormattedString("something-went-wrong", language.getCFG()));
                        return true;
                    }
                    break;
                case "DEPOSIT":
                    if(!acc.deposit(amount)) {
                        p.sendMessage(plugin.getConfigManager().getFormattedString("something-went-wrong", language.getCFG()));
                        return true;
                    }
                    break;
                default:
                    return false;
            }
        }
        else if(strings.length == 3) {
            String func = strings[0].toUpperCase();
            Player target = Bukkit.getPlayer(strings[1]);
            if(target == null) {
                p.sendMessage(plugin.getConfigManager().getFormattedString("player-not-found", language.getCFG()));
                return true;
            }
            if(isSamePlayer(p, target)) {
                p.sendMessage(plugin.getConfigManager().getFormattedString("cannot-transfer-to-yourself", language.getCFG()));
                return true;
            }

            Account targetAcc = am.getAccount(target.getUniqueId()).orElse(null);

            if(targetAcc == null) {
                p.sendMessage(plugin.getConfigManager().getFormattedString("player-has-no-account", language.getCFG()));
                return true;
            }

            double amount;
            try {
                amount = Double.parseDouble(strings[2]);
            } catch (Exception e) {
                return false;
            }

            if(isSameAccount(targetAcc, acc)) {
                p.sendMessage(plugin.getConfigManager().getFormattedString("cannot-transfer-to-your-own-account", language.getCFG()));
                return true;
            }

            switch (func) {
                case "TRANSFER":

                    if(!acc.transfer(targetAcc, amount)) {
                        p.sendMessage(plugin.getConfigManager().getFormattedString("something-went-wrong", language.getCFG()));
                        return true;
                    }

                    break;
                case "REQUEST":



                    break;
            }
        }
        else {
            return false;
        }

        return true;
    }
    private boolean isSamePlayer(Player p1, Player p2) {
        return p1.getUniqueId().equals(p2.getUniqueId());
    }
    private boolean isSameAccount(Account a1, Account a2) {
        return a1.getOwner().equals(a2.getOwner());
    }
}
