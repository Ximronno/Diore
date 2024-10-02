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
    BALANCE_WITHDRAW(Commands.BALANCE, SubCommands.BALANCE_WITHDRAW),
    BALANCE_LEADERBOARD(Commands.BALANCE, SubCommands.BALANCE_LEADERBOARD),
    BALANCE_ADMIN(Commands.BALANCE, SubCommands.BALANCE_ADMIN),
    ADMIN_HELP(Commands.BALANCE, SubCommands.BALANCE_ADMIN, SubCommands.ADMIN_HELP),
    ADMIN_ADD(Commands.BALANCE, SubCommands.BALANCE_ADMIN, SubCommands.ADMIN_ADD),
    ADMIN_SET(Commands.BALANCE, SubCommands.BALANCE_ADMIN, SubCommands.ADMIN_SET);

    private final Permission permission;

    Permissions(Commands command) {
        permission = Bukkit.getPluginManager().getPermission("diore." + command.getName());
    }

    Permissions(Commands command, SubCommands subCommand) {
        permission = Bukkit.getPluginManager().getPermission("diore." + command.getName() + "." + subCommand.getName());
    }

    Permissions(Commands command, SubCommands subCommand, SubCommands subCommand2) {
        permission = Bukkit.getPluginManager().getPermission("diore." + command.getName() + "." + subCommand.getName() + "." + subCommand2.getName());
    }

    public Permission getPermission() {
        return permission;
    }

}
