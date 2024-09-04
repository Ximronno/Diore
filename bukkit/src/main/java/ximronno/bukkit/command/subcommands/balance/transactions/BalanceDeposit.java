package ximronno.bukkit.command.subcommands.balance.transactions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.command.DioreSubcommand;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.DiorePlugin;
import ximronno.diore.api.account.Account;

import java.util.Locale;

public class  BalanceDeposit extends DioreSubcommand {

    private final DiorePlugin plugin;

    public BalanceDeposit(DioreAPI api, DiorePlugin plugin) {
        super(api);
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "deposit";
    }

    @Override
    public Permission getSubCommandPermission() {
        return Bukkit.getPluginManager().getPermission("diore.balance.deposit");
    }

    @Override
    public String getDescription(@Nullable Locale locale) {
        return api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_DEPOSIT_DESCRIPTION, locale, true);
    }

    @Override
    public String getSyntax() {
        return "/balance deposit [(amount)]";
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

        api.getAccountController().deposit(p, acc, accLocale, amount);
        return true;
    }
}
