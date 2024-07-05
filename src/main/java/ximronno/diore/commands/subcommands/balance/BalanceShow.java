package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.guis.menus.AccountMenu;
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
        return configManager.getFormattedString("balance-show-description", config);
    }

    @Override
    public String getSyntax() {
        return "/balance | /balance show";
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

        p.sendMessage(configManager.getFormattedString("on-balance", language.getCFG()).replace("<balance>", accountManager.formatBalance(acc.getBalance())));

        if(plugin.getConfig().getBoolean("open-gui-on-commands")) {
            new MainMenu(plugin.getMenuKey()).open(p);
        }

    }
}
