package ximronno.bukkit.command.subcommands.balance.admin.subcommands;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.Permissions;
import ximronno.bukkit.command.DioreSubcommand;
import ximronno.bukkit.command.subcommands.SubCommands;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.command.SubCommand;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminHelp extends DioreSubcommand {

    private final List<SubCommand> subCommands;

    public AdminHelp(List<SubCommand> subCommandList, DioreAPI api) {
        super(api);
        this.subCommands = subCommandList;
    }

    @Override
    public String getName() {
        return SubCommands.ADMIN_HELP.getName();
    }

    @Override
    public Permission getPermission() {
        return Permissions.ADMIN_HELP.getPermission();
    }

    @Override
    public String getDescription(@Nullable Locale locale) {
        return api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_ADMIN_HELP_DESCRIPTION, locale, true);
    }

    @Override
    public String getSyntax() {
        return "/balance admin help";
    }

    @Override
    public boolean perform(Player p, Account acc, Locale accLocale, String[] args) {
        p.sendMessage(api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_ADMIN_HELP_TITLE, accLocale, true));

        for(SubCommand subCommand : subCommands) {
            if(p.hasPermission(subCommand.getPermission())) {
                p.sendMessage(api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_HELP_FORMAT, accLocale, true,
                        Map.of("{syntax}", subCommand.getSyntax(),
                                "{description}", subCommand.getDescription(acc.getLocale()))));
            }
        }
        return true;
    }

}
