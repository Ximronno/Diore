package ximronno.bukkit.command.subcommands.balance.info;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.command.DioreSubcommand;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.command.SubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BalanceHelp extends DioreSubcommand {

    private final ArrayList<SubCommand> subCommands;

    public BalanceHelp(ArrayList<SubCommand> subCommands, DioreAPI api) {
        super(api);
        this.subCommands = subCommands;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public Permission getSubCommandPermission() {
        return Bukkit.getPluginManager().getPermission("diore.balance.help");
    }

    @Override
    public String getDescription(@Nullable Locale locale) {
        return api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_HELP_DESCRIPTION, locale, true);
    }

    @Override
    public String getSyntax() {
        return "/balance help";
    }

    @Override
    public boolean perform(Player p, Account acc, Locale accLocale, String[] args) {
        p.sendMessage(api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_HELP_TITLE, accLocale, true));

        for(SubCommand subCommand : subCommands) {
            p.sendMessage(api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_HELP_FORMAT, accLocale, true,
                    Map.of("{syntax}", subCommand.getSyntax(),
                            "{description}", subCommand.getDescription(acc.getLocale()))));
        }
        return true;
    }

}
