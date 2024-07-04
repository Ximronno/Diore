package ximronno.diore.commands.subcommands.balance;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.impl.Languages;
import ximronno.diore.impl.TopBalance;

import java.util.List;

public class BalanceTop extends DioreSubCommand {
    public BalanceTop(Diore plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "top";
    }

    @Override
    public String getDescription(@Nullable FileConfiguration config) {
        if(config == null) return ChatColor.BLUE + "Shows top 5 balances";
        return configManager.getFormattedString("balance-top-description", config);
    }

    @Override
    public String getSyntax() {
        return "/balance top";
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

        List<TopBalance> topBalances = accountManager.getTopBalances();

        for (int i = 0; i < Math.min(topBalances.size(), 5); i++) {
            TopBalance topBalance = topBalances.get(i);
            p.sendMessage(plugin.getConfigManager().getFormattedString("top-list-format", language.getCFG())
                    .replace("<place>", String.valueOf(i + 1))
                    .replace("<player>", topBalance.account().getName())
                    .replace("<balance>", accountManager.formatBalance(topBalance.balance())));
        }

    }
}
