package ximronno.api.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;

public abstract class CommandManager implements CommandExecutor, TabCompleter {
    protected final ArrayList<SubCommand> subCommands = new ArrayList<>();
    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }
}
