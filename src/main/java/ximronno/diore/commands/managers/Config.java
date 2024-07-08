package ximronno.diore.commands.managers;

import ximronno.diore.Diore;
import ximronno.diore.commands.DioreCommandManager;
import ximronno.diore.commands.subcommands.config.ConfigReload;

public class Config extends DioreCommandManager {
    public Config(Diore plugin) {
        super(plugin);

        subCommands.add(new ConfigReload(plugin));


    }
}
