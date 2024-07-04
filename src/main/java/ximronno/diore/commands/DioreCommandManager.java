package ximronno.diore.commands;

import ximronno.api.command.CommandManager;
import ximronno.diore.Diore;

public abstract class DioreCommandManager extends CommandManager {

    protected final Diore plugin;

    public DioreCommandManager(Diore plugin) {
        this.plugin = plugin;
    }
}
