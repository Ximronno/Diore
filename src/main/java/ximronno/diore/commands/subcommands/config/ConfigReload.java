package ximronno.diore.commands.subcommands.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.impl.Languages;

public class ConfigReload extends DioreSubCommand {
    public ConfigReload(Diore plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription(@Nullable FileConfiguration config) {
        if(config == null) return ChatColor.BLUE + "Reloads the config";
        return configManager.getFormattedString(config, "config-reload-description");
    }

    @Override
    public String getSyntax() {
        return "/config reload";
    }
    @Override
    public void perform(Player p, String[] args, Account acc, Language language) {

        for(Language lang : Languages.values()) {

            lang.reloadConfig();

        }
        plugin.reloadConfig();

        p.sendMessage(configManager.getFormattedString(language.getConfig(), "config-reloaded"));

    }
}
