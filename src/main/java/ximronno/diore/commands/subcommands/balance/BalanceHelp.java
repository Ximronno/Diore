package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.command.SubCommand;
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.commands.managers.BalanceNew;
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
        return configManager.getFormattedString("balance-help-description", config);
    }

    @Override
    public String getSyntax() {
        return "/balance help";
    }

    @Override
    public void perform(Player p, String[] args) {

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);

        if(acc == null) {
            p.sendMessage(configManager.getFormattedString("no-account-found"));
            return;
        }

        Languages language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        p.sendMessage(configManager.getFormattedString("balance-help-title", language.getCFG()));
        for(SubCommand subCommand : BalanceNew.getSubCommands()) {
            p.sendMessage(configManager.getFormattedString("balance-help-format", language.getCFG())
                    .replace("<syntax>", subCommand.getSyntax())
                    .replace("<description>", subCommand.getDescription(language.getCFG())));
        }


    }
}
