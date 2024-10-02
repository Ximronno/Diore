package ximronno.bukkit.command.subcommands.balance.settings;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.Permissions;
import ximronno.bukkit.command.DioreSubcommand;
import ximronno.bukkit.command.subcommands.SubCommands;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;

import java.util.List;
import java.util.Locale;

public class BalanceLocale extends DioreSubcommand {
    public BalanceLocale(DioreAPI api) {
        super(api);
    }

    @Override
    public String getName() {
        return SubCommands.BALANCE_LOCALE.getName();
    }

    @Override
    public Permission getPermission() {
        return Permissions.BALANCE_LOCALE.getPermission();
    }

    @Override
    public String getDescription(@Nullable Locale locale) {
        return api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_LOCALE_DESCRIPTION, locale, true);
    }

    @Override
    public String getSyntax() {
        return "/balance locale <language>";
    }

    @Override
    public List<String> getCompletion(Player p, String[] args, List<String> completion) {
        if(args.length == 2) {
            api.getMessageManager().getMessageProvider().getProvidedLanguages().forEach(s -> completion.add(s.toString()));
        }
        return completion;
    }

    @Override
    public boolean perform(Player p, Account acc, Locale accLocale, String[] args) {
        if(args.length < 2) {
            return false;
        }

        Locale locale = new Locale(args[1]);
        api.getAccountController().setLocale(p, acc, locale);

        return true;
    }
}
