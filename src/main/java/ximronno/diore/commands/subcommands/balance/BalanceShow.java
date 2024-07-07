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

public class BalanceShow extends DioreSubCommand {
    public BalanceShow(Diore plugin) {
        super(plugin);
    }


    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription(@Nullable FileConfiguration config) {
        if(config == null) return ChatColor.BLUE + "Shows your balance in ores";
        return configManager.getFormattedString(config, "balance-show-description");
    }

    @Override
    public String getSyntax() {
        return "/balance | /balance show";
    }
    @Override
    public void perform(Player p, String[] args, Account acc, Languages language) {

        p.sendMessage(configManager.getFormattedString(language.getCFG(), "on-balance")
                .replace("<balance>", accountManager.formatBalance(acc.getBalance())));

        if(plugin.getConfig().getBoolean("open-gui-on-commands")) {
            new MainMenu(plugin.getMenuKey()).open(p);
        }

    }
}
