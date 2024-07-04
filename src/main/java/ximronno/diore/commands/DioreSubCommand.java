package ximronno.diore.commands;

import ximronno.api.command.SubCommand;
import ximronno.diore.Diore;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.ConfigManager;

public abstract class DioreSubCommand extends SubCommand {

    protected final Diore plugin;
    protected final ConfigManager configManager;
    protected final AccountManager accountManager;

    public DioreSubCommand(Diore plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.accountManager = plugin.getAccountManager();
    }

}
