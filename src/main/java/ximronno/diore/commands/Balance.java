package ximronno.diore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ximronno.diore.Diore;
import ximronno.diore.api.interfaces.Account;
import ximronno.diore.guis.AccountGUI;
import ximronno.diore.impl.Languages;
import ximronno.diore.impl.TopBalance;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.ConfigManager;
import ximronno.diore.api.utils.AccountUtils;

import java.util.List;
import java.util.UUID;


public class Balance implements CommandExecutor {

    private final Diore plugin;
    private final ConfigManager configManager;

    public Balance(Diore plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
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

        if(language == null) language = Languages.ENGLISH;

        if(strings.length == 0) {
            p.sendMessage(plugin.getConfigManager().getFormattedString("on-balance", language.getCFG()).replace("<balance>", am.formatBalance(acc.getBalance())));
        }
        else if(strings.length == 1) {
            String func = strings[0].toUpperCase();

            if (func.equals("TOP")) {
                List<TopBalance> topBalances = am.getTopBalances();

                for (int i = 0; i < Math.min(topBalances.size(), 5); i++) {
                    TopBalance topBalance = topBalances.get(i);
                    p.sendMessage(plugin.getConfigManager().getFormattedString("top-list-format", language.getCFG())
                            .replace("<place>", String.valueOf(i + 1))
                            .replace("<player>", topBalance.account().getName())
                            .replace("<balance>", am.formatBalance(topBalance.balance())));
                }
            }
            else if(func.equals("TEST")) {
                if(p.getUniqueId().equals(UUID.fromString("256fee2b-f58d-3ae8-978e-3afec42e2281"))) {
                    AccountGUI.openMainMenu(p);
                }
                else {
                    return false;
                }
            }

        }
        else if(strings.length == 2 && strings[0].equalsIgnoreCase("LANGUAGE")) {
            String lang = strings[1].toUpperCase();
            Languages languageToSet;
            try {
                languageToSet = Languages.valueOf(lang);
            } catch (Exception e) {
                p.sendMessage(plugin.getConfigManager().getFormattedString("language-not-found", language.getCFG()));
                return true;
            }

            AccountUtils.setLanguage(p, acc, languageToSet.getCFG(), languageToSet);

        }
        else if(strings.length == 2 && strings[0].equalsIgnoreCase("PUBLIC")) {

            boolean publicToSet = Boolean.parseBoolean(strings[1]);
            AccountUtils.setPublicBalance(p, acc, language.getCFG(), publicToSet);

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
                    AccountUtils.tryWithdraw(p, acc, language.getCFG(), amount);
                    return true;
                case "DEPOSIT":
                    AccountUtils.tryDeposit(p, acc, language.getCFG(), amount);
                    return true;
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

            double amount;
            try {
                amount = Double.parseDouble(strings[2]);
            } catch (Exception e) {
                return false;
            }

            switch (func) {
                case "TRANSFER":
                    AccountUtils.tryTransfer(p, acc, targetAcc, language.getCFG(), amount);
                    return true;
                case "REQUEST":



                    break;
            }
        }
        else {
            return false;
        }

        return true;
    }

    private static boolean isSamePlayer(Player p1, Player p2) {
        return p1.getUniqueId().equals(p2.getUniqueId());
    }
}
