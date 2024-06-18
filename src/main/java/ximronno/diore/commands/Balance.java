package ximronno.diore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ximronno.diore.Diore;
import ximronno.diore.api.interfaces.Account;
import ximronno.diore.impl.TopBalance;
import ximronno.diore.model.AccountManager;

import java.util.List;


public class Balance implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player)) return false;

        Player p = (Player) commandSender;

        AccountManager am = Diore.getInstance().getAccountManager();

        Account acc = am.getAccount(p.getUniqueId()).orElse(null);

        if(acc == null) {
            p.sendMessage("You have no account");
            return true;
        }

        if(strings.length == 0) {
            p.sendMessage("Your balance: " + am.formatBalance(acc.getBalance()));
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
        else if(strings.length == 2) {
            String func = strings[0].toUpperCase();

            double amount = 0;
            try {
                amount = Double.parseDouble(strings[1]);
            } catch (Exception e) {
                return false;
            }

            switch (func) {
                case "WITHDRAW":
                    if(!acc.withdraw(amount)) {
                        p.sendMessage("Something went wrong");
                        return true;
                    }
                    break;
                case "DEPOSIT":
                    if(!acc.deposit(amount)) {
                        p.sendMessage("Something went wrong");
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
                p.sendMessage("Player not found");
                return true;
            }
            if(isSamePlayer(p, target)) {
                p.sendMessage("You can't transfer money to yourself");
                return true;
            }

            Account targetAcc = am.getAccount(target.getUniqueId()).orElse(null);

            if(targetAcc == null) {
                p.sendMessage("Player has no account");
                return true;
            }

            double amount = Double.parseDouble(strings[2]);

            if(isSameAccount(targetAcc, acc)) {
                p.sendMessage("You can't transfer money to your own account");
                return true;
            }

            switch (func) {
                case "TRANSFER":

                    if(!acc.transfer(targetAcc, amount)) {
                        p.sendMessage("Something went wrong");
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
