package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
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
        return configManager.getFormattedString(config, "balance-language-description");
    }

    @Override
    public String getSyntax() {
        return "/balance language <language>";
    }
    @Override
    public void perform(Player p, String[] args, Account acc, Language language) {

        if(args.length < 2) {

            p.sendMessage(configManager.getFormattedString(language.getConfig(), "balance-help-format")
                    .replace("<syntax>", getSyntax())
                    .replace("<description>", getDescription(language.getConfig())));

        }
        else {
            String lang = args[1].toUpperCase();

            Language languageToSet;
            try {
                languageToSet = Languages.valueOf(lang);
                AccountUtils.setLanguage(p, acc, languageToSet);
            } catch (Exception e) {
                p.sendMessage(plugin.getConfigManager().getFormattedString(language.getConfig(), "language-not-found"));
            }
        }

    }
}
