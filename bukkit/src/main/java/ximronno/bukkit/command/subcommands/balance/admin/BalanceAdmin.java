package ximronno.bukkit.command.subcommands.balance.admin;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.Permissions;
import ximronno.bukkit.command.DioreSubcommand;
import ximronno.bukkit.command.subcommands.SubCommands;
import ximronno.bukkit.command.subcommands.balance.admin.subcommands.AdminAdd;
import ximronno.bukkit.command.subcommands.balance.admin.subcommands.AdminHelp;
import ximronno.bukkit.command.subcommands.balance.admin.subcommands.AdminSet;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.command.SubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BalanceAdmin extends DioreSubcommand {

    private final List<SubCommand> subCommands = new ArrayList<>();

    private final SubCommand helpCommand;

    public BalanceAdmin(DioreAPI api) {
        super(api);
        SubCommand help = new AdminHelp(subCommands, api);
        subCommands.add(help);
        subCommands.add(new AdminAdd(api));
        subCommands.add(new AdminSet(api));

        helpCommand = help;
    }

    @Override
    public String getName() {
        return SubCommands.BALANCE_ADMIN.getName();
    }

    @Override
    public Permission getPermission() {
        return Permissions.BALANCE_ADMIN.getPermission();
    }

    @Override
    public String getDescription(@Nullable Locale locale) {
        return api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_ADMIN_DESCRIPTION, locale, true);
    }

    @Override
    public String getSyntax() {
        return "/balance admin";
    }

    @Override
    public List<String> getCompletion(Player p, String[] args, List<String> completion) {
        if(args.length == 2) {
            for(SubCommand subCommand : subCommands) {
                completion.add(subCommand.getName());
            }
        }
        else if(args.length > 2) {
            for(SubCommand subCommand : subCommands) {
                if(subCommand.getName().equalsIgnoreCase(args[1])) {
                    completion = subCommand.getCompletion(p, args, completion);
                }
            }
        }
        return completion;
    }

    @Override
    public boolean perform(Player p, Account acc, Locale accLocale, String[] args) {
        if(args.length > 2) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().equalsIgnoreCase(args[1])) {
                    if (!p.hasPermission(subCommand.getPermission())) {
                        return true;
                    }
                    if (subCommand.perform(p, args)) {
                        return true;
                    }
                    break;
                }
            }
        }
        helpCommand.perform(p, args);
        return true;
    }

}
