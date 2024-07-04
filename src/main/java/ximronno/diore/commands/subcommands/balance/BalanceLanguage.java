package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.impl.Languages;
import ximronno.diore.utils.AccountUtils;

public class BalanceLanguage extends DioreSubCommand {

    public BalanceLanguage(Diore plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "language";
    }

    @Override
    public String getDescription(@Nullable FileConfiguration config) {
        if(config == null) return ChatColor.BLUE + "Changes the language of your account";
        return configManager.getFormattedString("balance-language-description", config);
    }

    @Override
    public String getSyntax() {
        return "/balance language <language>";
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

        if(args.length < 2) {

            p.sendMessage(configManager.getFormattedString("balance-help-format", language.getCFG())
                    .replace("<syntax>", getSyntax())
                    .replace("<description>", getDescription(language.getCFG())));

        }
        else {
            String lang = args[1].toUpperCase();

            Languages languageToSet;
            try {
                languageToSet = Languages.valueOf(lang);
                AccountUtils.setLanguage(p, acc, languageToSet);
            } catch (Exception e) {
                p.sendMessage(plugin.getConfigManager().getFormattedString("language-not-found", language.getCFG()));
            }
        }

    }
}
