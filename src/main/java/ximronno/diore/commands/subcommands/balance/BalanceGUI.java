package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.guis.menus.MainMenu;
import ximronno.diore.impl.Languages;

public class BalanceGUI extends DioreSubCommand {
    public BalanceGUI(Diore plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public String getDescription(@Nullable FileConfiguration config) {
        if(config == null) return ChatColor.BLUE + "Opens the balance GUI";
        return configManager.getFormattedString(config, "balance-gui-description");
    }

    @Override
    public String getSyntax() {
        return "/balance gui";
    }
    @Override
    public void perform(Player p, String[] args, Account acc, Languages language) {

        if(plugin.getConfig().getBoolean("enable-gui")) {
            new MainMenu(plugin.getMenuKey()).open(p);
        }
        else {
            p.sendMessage(configManager.getFormattedString(language.getCFG(), "gui-disabled"));
        }

    }

}
