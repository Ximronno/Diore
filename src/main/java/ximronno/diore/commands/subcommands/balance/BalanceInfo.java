package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.guis.menus.AccountMenu;
import ximronno.diore.impl.Languages;

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
        return configManager.getFormattedString("balance-info-description", config);
    }

    @Override
    public String getSyntax() {
        return "/balance info";
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

        String langName = language.getName();

        FileConfiguration config = language.getCFG();

        String yes = configManager.getFormattedString("menu-yes", config);
        String no = configManager.getFormattedString("menu-no", config);

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
