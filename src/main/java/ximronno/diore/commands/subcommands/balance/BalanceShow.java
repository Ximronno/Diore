package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.guis.menus.MainMenu;

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
    public void perform(Player p, String[] args, Account acc, Language language) {

        p.sendMessage(configManager.getFormattedString(language.getConfig(), "on-balance")
                .replace("<balance>", accountManager.formatBalance(acc.getBalance())));

        if(plugin.getConfig().getBoolean("open-gui-on-commands")) {
            new MainMenu(plugin.getMenuKey()).open(p);
        }

    }
}
