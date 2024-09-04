package ximronno.diore.api.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand implements TabExecutor {

    protected final ArrayList<SubCommand> subCommands = new ArrayList<>();

    protected SubCommand helpCommand;

    protected SubCommand noLengthCommand;

    public BaseCommand(JavaPlugin plugin, String commandName) {
        plugin.getCommand(commandName).setExecutor(this);
        plugin.getCommand(commandName).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) {
            return true;
        }
        if(strings.length == 0) {
            if(noLengthCommand != null) {
                return noLengthCommand.perform(player, strings);
            }
        }
        for(SubCommand subCommand : subCommands) {
            if(subCommand.getName().equalsIgnoreCase(strings[0])) {
                if(subCommand.perform(player, strings)) {
                    return true;
                }
                break;
            }
        }

        helpCommand.perform(player, strings);

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
        else if(strings.length > 1) {

            for(SubCommand subCommand : subCommands) {
                if(subCommand.getName().equalsIgnoreCase(strings[0])) {
                   completions = subCommand.getCompletion((Player) commandSender, strings, completions);
                }
            }

        }


        return completions;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }



}
