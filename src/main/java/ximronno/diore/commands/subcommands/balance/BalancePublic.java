package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.utils.AccountUtils;

public class BalancePublic extends DioreSubCommand {
    public BalancePublic(Diore plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "public";
    }

    @Override
    public String getDescription(@Nullable FileConfiguration config) {
        if(config == null) return ChatColor.BLUE + "Changes the visibility of your balance";
        return configManager.getFormattedString(config, "balance-public-description");
    }

    @Override
    public String getSyntax() {
        return "/balance public <bool>";
    }
    @Override
    public void perform(Player p, String[] args, Account acc, Language language) {

        if(args.length < 2) {

            p.sendMessage(configManager.getFormattedString(language.getConfig(), "balance-help-format")
                    .replace("<syntax>", getSyntax())
                    .replace("<description>", getDescription(language.getConfig())));

        }
        else {

            boolean publicToSet = Boolean.parseBoolean(args[1]);
            AccountUtils.setPublicBalance(p, acc, language.getConfig(), publicToSet);

        }

    }
}
