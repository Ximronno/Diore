package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.guis.menus.AccountMenu;

public class BalanceInfo extends DioreSubCommand {
    public BalanceInfo(Diore plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription(@Nullable FileConfiguration config) {
        if(config == null) return ChatColor.BLUE + "Shows account information";
        return configManager.getFormattedString(config, "balance-info-description");
    }

    @Override
    public String getSyntax() {
        return "/balance info";
    }
    @Override
    public void perform(Player p, String[] args, Account acc, Language language) {

        String langName = language.getName();

        FileConfiguration config = language.getConfig();

        String yes = configManager.getFormattedString(config, "menu-yes");
        String no = configManager.getFormattedString(config, "menu-no");

        config.getStringList("balance-info-list")
                .forEach(loreString -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', loreString)
                        .replace("<balance>", accountManager.formatBalance(acc.getBalance()))
                        .replace("<language>", langName)
                        .replace("<public>", acc.isPublicBalance() ? yes : no)));

        if(plugin.getConfig().getBoolean("open-gui-on-commands")) {
            new AccountMenu(plugin.getMenuKey()).open(p);
        }

    }
}
