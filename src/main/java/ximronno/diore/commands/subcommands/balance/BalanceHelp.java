package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.command.CommandManager;
import ximronno.api.command.SubCommand;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.commands.managers.Balance;

public class BalanceHelp extends DioreSubCommand {
    private final CommandManager manager;
    public BalanceHelp(Diore plugin, CommandManager manager) {
        super(plugin);
        this.manager = manager;
    }
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription(@Nullable FileConfiguration config) {
        if(config == null) return ChatColor.BLUE + "Shows all subcommands of /balance";
        return configManager.getFormattedString(config, "balance-help-description");
    }

    @Override
    public String getSyntax() {
        return "/balance help";
    }
    @Override
    public void perform(Player p, String[] args, Account acc, Language language) {

        p.sendMessage(configManager.getFormattedString(language.getConfig(), "balance-help-title"));
        for(SubCommand subCommand : manager.getSubCommands()) {
            p.sendMessage(configManager.getFormattedString(language.getConfig(), "balance-help-format")
                    .replace("<syntax>", subCommand.getSyntax())
                    .replace("<description>", subCommand.getDescription(language.getConfig())));
        }

    }
}
