package ximronno.bukkit.command.subcommands.balance.info;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.Permissions;
import ximronno.bukkit.command.DioreSubcommand;
import ximronno.bukkit.command.subcommands.SubCommands;
import ximronno.bukkit.menu.yap.MainMenu;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;

import java.util.Locale;
import java.util.Map;

public class BalanceInfo extends DioreSubcommand {
    public BalanceInfo(DioreAPI api) {
        super(api);
    }

    @Override
    public String getName() {
        return SubCommands.BALANCE_INFO.getName();
    }

    @Override
    public Permission getPermission() {
        return Permissions.BALANCE.getPermission();
    }

    @Override
    public String getDescription(@Nullable Locale locale) {
        return api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_INFO_DESCRIPTION, locale, true);
    }

    @Override
    public String getSyntax() {
        return "/balance info";
    }

    @Override
    public boolean perform(Player p, Account acc, Locale accLocale, String[] args) {
        for(String string : api.getMessageManager().getList(CommandMessagesPaths.BALANCE_INFO_LIST, accLocale, true, Map.of(
                "{balance}", api.getAccountInfoFormatter().getFormattedBalance(acc, accLocale),
                "{balance_status}", api.getAccountInfoFormatter().getFormattedBalanceStatus(acc, accLocale)))) {
            p.sendMessage(string);
        }

        new MainMenu().open(p);

        return true;
    }
}
