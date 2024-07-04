package ximronno.api.command;

import org.bukkit.command.CommandExecutor;

import java.util.ArrayList;

public abstract class CommandManager implements CommandExecutor {


    protected static ArrayList<SubCommand> subCommands = new ArrayList<>();


    public static ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }
}
