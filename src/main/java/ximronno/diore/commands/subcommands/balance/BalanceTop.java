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
        return configManager.getFormattedString(config, "balance-top-description");
    }

    @Override
    public String getSyntax() {
        return "/balance top";
    }
    @Override
    public void perform(Player p, String[] args, Account acc, Languages language) {

        List<TopBalance> topBalances = accountManager.getTopBalances();


        p.sendMessage(configManager.getFormattedString(language.getCFG(), "top-list-title"));
        for (int i = 0; i < Math.min(topBalances.size(), 5); i++) {
            TopBalance topBalance = topBalances.get(i);
            p.sendMessage(plugin.getConfigManager().getFormattedString(language.getCFG(), "top-list-format")
                    .replace("<place>", String.valueOf(i + 1))
                    .replace("<player>", topBalance.account().getName())
                    .replace("<balance>", accountManager.formatBalance(topBalance.balance())));
        }

    }
}
