package ximronno.diore.commands.subcommands.balance.transactions;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.impl.Languages;
import ximronno.diore.utils.AccountUtils;

public class BalanceDeposit extends DioreSubCommand {
    public BalanceDeposit(Diore plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "deposit";
    }

    @Override
    public String getDescription(@Nullable FileConfiguration config) {
        if(config == null) return ChatColor.BLUE + "Deposits ores into your account";
        return configManager.getFormattedString(config, "balance-deposit-description");
    }

    @Override
    public String getSyntax() {
        return "/balance deposit <amount>";
    }
    @Override
    public void perform(Player p, String[] args, Account acc, Languages language) {

        if(args.length < 2) {

            p.sendMessage(configManager.getFormattedString(language.getCFG(), "balance-help-format")
                    .replace("<syntax>", getSyntax())
                    .replace("<description>", getDescription(language.getCFG())));


        }
        else {
            double amount;
            try {
                amount = Double.parseDouble(args[1]);
                AccountUtils.tryDeposit(p, acc, language.getCFG(), amount);
            } catch (Exception e) {
                p.sendMessage(configManager.getFormattedString(language.getCFG(), "invalid-amount"));
            }
        }

    }
}
