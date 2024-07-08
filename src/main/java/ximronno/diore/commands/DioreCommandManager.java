package ximronno.diore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ximronno.api.command.CommandManager;
import ximronno.api.command.SubCommand;
import ximronno.diore.Diore;
import ximronno.diore.commands.managers.Balance;

import java.util.ArrayList;
import java.util.List;

public abstract class DioreCommandManager extends CommandManager {

    protected final Diore plugin;

    public DioreCommandManager(Diore plugin) {
        this.plugin = plugin;
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

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completions = new ArrayList<>();

        if(strings.length == 1) {

            for(SubCommand subCommand : subCommands) {
                completions.add(subCommand.getName());
            }

        }



        return completions;
    }
}
