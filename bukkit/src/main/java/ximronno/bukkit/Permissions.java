package ximronno.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import ximronno.bukkit.command.Commands;
import ximronno.bukkit.command.subcommands.SubCommands;

public enum Permissions {

    BALANCE(Commands.BALANCE),
    BALANCE_HELP(Commands.BALANCE, SubCommands.BALANCE_HELP),
    BALANCE_INFO(Commands.BALANCE, SubCommands.BALANCE_INFO),
    BALANCE_LOCALE(Commands.BALANCE, SubCommands.BALANCE_LOCALE),
    BALANCE_PRIVACY(Commands.BALANCE, SubCommands.BALANCE_PRIVACY),
    BALANCE_DEPOSIT(Commands.BALANCE, SubCommands.BALANCE_DEPOSIT),
    BALANCE_TRANSFER(Commands.BALANCE, SubCommands.BALANCE_TRANSFER),
    BALANCE_WITHDRAW(Commands.BALANCE, SubCommands.BALANCE_WITHDRAW);

    private final Permission permission;

    Permissions(Commands command) {
        permission = Bukkit.getPluginManager().getPermission("diore." + command.getName());
    }

    Permissions(Commands command, SubCommands subCommand) {
        permission = Bukkit.getPluginManager().getPermission("diore." + command.getName() + "." + subCommand.getName());
    }

    public Permission getPermission() {
        return permission;
    }

}
