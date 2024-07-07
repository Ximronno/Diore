package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.command.SubCommand;
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.commands.managers.Balance;
import ximronno.diore.impl.Languages;

public class BalanceHelp extends DioreSubCommand {
    public BalanceHelp(Diore plugin) {
        super(plugin);
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
    public void perform(Player p, String[] args, Account acc, Languages language) {

        p.sendMessage(configManager.getFormattedString(language.getCFG(), "balance-help-title"));
        for(SubCommand subCommand : Balance.getSubCommands()) {
            p.sendMessage(configManager.getFormattedString(language.getCFG(), "balance-help-format")
                    .replace("<syntax>", subCommand.getSyntax())
                    .replace("<description>", subCommand.getDescription(language.getCFG())));
        }

    }
}
