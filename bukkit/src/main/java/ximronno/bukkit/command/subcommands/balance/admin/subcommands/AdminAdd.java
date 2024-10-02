package ximronno.bukkit.command.subcommands.balance.admin.subcommands;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.Permissions;
import ximronno.bukkit.command.subcommands.SubCommands;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminAdd extends AdminPlayerSubcommand {

    public AdminAdd(DioreAPI api) {
        super(api);
    }

    @Override
    public String getName() {
        return SubCommands.ADMIN_ADD.getName();
    }

    @Override
    public Permission getPermission() {
        return Permissions.ADMIN_ADD.getPermission();
    }

    @Override
    public String getDescription(@Nullable Locale locale) {
        return api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_ADMIN_ADD_DESCRIPTION, locale, true);
    }

    @Override
    public String getSyntax() {
        return "/balance admin add <player> <amount>";
    }

    @Override
    public List<String> getCompletion(Player p, String[] args, List<String> completion) {
        if(args.length == 3) {
            return null;
        }
        return completion;
    }

    @Override
    public boolean perform(Player p, Player target, Account acc, Account targetAcc, Locale accLocale, String[] args) {
        double amount;
        try {
            amount = Double.parseDouble(args[3]);
        } catch (NumberFormatException exception) {
            p.sendMessage(api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_INVALID_AMOUNT, accLocale, true));
            return true;
        }

        targetAcc.deposit(amount);

        if(!p.equals(target)) {
            p.sendMessage(api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_ADMIN_ADD_SEND_FORMAT, accLocale, true, Map.of(
                    "{amount}", api.getAccountInfoFormatter().getFormattedAmount(amount, accLocale),
                    "{target}", target.getDisplayName()
            )));
        }
        target.sendMessage(api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_ADMIN_ADD_FORMAT, accLocale, true, Map.of(
                "{amount}", api.getAccountInfoFormatter().getFormattedAmount(amount, accLocale),
                "{sender}", p.getDisplayName()
        )));

        return true;
    }
}
