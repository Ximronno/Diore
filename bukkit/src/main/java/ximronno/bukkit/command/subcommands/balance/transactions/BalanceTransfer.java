package ximronno.bukkit.command.subcommands.balance.transactions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.Permissions;
import ximronno.bukkit.command.DioreSubcommand;
import ximronno.bukkit.command.subcommands.SubCommands;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.bukkit.message.type.ErrorMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BalanceTransfer extends DioreSubcommand {

    public BalanceTransfer(DioreAPI api) {
        super(api);
    }

    @Override
    public String getName() {
        return SubCommands.BALANCE_TRANSFER.getName();
    }

    @Override
    public Permission getSubCommandPermission() {
        return Permissions.BALANCE_TRANSFER.getPermission();
    }

    @Override
    public String getDescription(@Nullable Locale locale) {
        return api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_TRANSFER_DESCRIPTION, locale, true);
    }

    @Override
    public String getSyntax() {
        return "/balance transfer [(player)] [(amount)]";
    }

    @Override
    public List<String> getCompletion(Player p, String[] args, List<String> completion) {
        if(args.length == 2) {
            return null;
        }
        return completion;
    }

    @Override
    public boolean perform(Player p, Account acc, Locale accLocale, String[] args) {
        if(args.length != 3) {
            return false;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if(target == null) {
            p.sendMessage(api.getMessageManager().getDefaultMessage(ErrorMessagesPaths.PLAYER_NOT_FOUND, true));
            return true;
        }

        Account targetAcc = api.getAccount(target.getUniqueId());

        double amount = -1;
        try {
            amount = Double.parseDouble(args[2]);
        } catch (Exception ignored) {
        }

        api.getAccountController().transfer(p, target, acc, targetAcc, accLocale, amount);
        return true;
    }
}
