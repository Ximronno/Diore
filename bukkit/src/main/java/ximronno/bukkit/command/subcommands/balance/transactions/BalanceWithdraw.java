package ximronno.bukkit.command.subcommands.balance.transactions;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.Permissions;
import ximronno.bukkit.command.DioreSubcommand;
import ximronno.bukkit.command.subcommands.SubCommands;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;

import java.util.Locale;

public class BalanceWithdraw extends DioreSubcommand {

    public BalanceWithdraw(DioreAPI api) {
        super(api);
    }

    @Override
    public String getName() {
        return SubCommands.BALANCE_WITHDRAW.getName();
    }

    @Override
    public Permission getPermission() {
        return Permissions.BALANCE_WITHDRAW.getPermission();
    }

    @Override
    public String getDescription(@Nullable Locale locale) {
        return api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_WITHDRAW_DESCRIPTION, locale, true);
    }

    @Override
    public String getSyntax() {
        return "/balance withdraw <amount>";
    }

    @Override
    public boolean perform(Player p, Account acc, Locale accLocale, String[] args) {
        if(args.length != 2) {
            return false;
        }

        double amount = -1;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (Exception ignored) {
        }

        api.getAccountController().withdraw(p, acc, accLocale, amount);
        return true;
    }
}
