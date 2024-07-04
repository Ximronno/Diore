package ximronno.diore.commands.managers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ximronno.api.command.SubCommand;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreCommandManager;
import ximronno.diore.commands.subcommands.balance.*;
import ximronno.diore.commands.subcommands.balance.transactions.*;


public class BalanceNew extends DioreCommandManager {
    public BalanceNew(Diore plugin) {
        super(plugin);

        subCommands.add(new BalanceShow(plugin));
        subCommands.add(new BalanceHelp(plugin));

        subCommands.add(new BalanceGUI(plugin));
        subCommands.add(new BalanceInfo(plugin));
        subCommands.add(new BalanceTop(plugin));

        subCommands.add(new BalanceDeposit(plugin));
        subCommands.add(new BalanceWithdraw(plugin));
        subCommands.add(new BalancePublic(plugin));
        subCommands.add(new BalanceLanguage(plugin));

        subCommands.add(new BalanceTransfer(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(commandSender instanceof Player p) {

            if (strings.length == 0) {

                subCommands.get(0).perform(p, strings);

            }
            else {

                for(SubCommand subCommand : subCommands) {
                    if(subCommand.getName().equalsIgnoreCase(strings[0])) {
                        subCommand.perform(p, strings);
                    }
                }

            }

        }

        return true;
    }
}
