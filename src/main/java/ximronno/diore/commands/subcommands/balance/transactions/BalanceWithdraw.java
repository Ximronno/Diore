package ximronno.diore.commands.subcommands.balance.transactions;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.utils.AccountUtils;

public class BalanceWithdraw extends DioreSubCommand {
    public BalanceWithdraw(Diore plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "withdraw";
    }

    @Override
    public String getDescription(@Nullable FileConfiguration config) {
        if(config == null) return ChatColor.BLUE + "Withdraws ores from your account";
        return configManager.getFormattedString(config, "balance-withdraw-description");
    }

    @Override
    public String getSyntax() {
        return "/balance withdraw <amount>";
    }
    @Override
    public void perform(Player p, String[] args, Account acc, Language language) {

        if(args.length < 2) {

            p.sendMessage(configManager.getFormattedString(language.getConfig(), "balance-help-format")
                    .replace("<syntax>", getSyntax())
                    .replace("<description>", getDescription(language.getConfig())));

        }
        else {
            double amount;
            try {
                amount = Double.parseDouble(args[1]);
                AccountUtils.tryWithdraw(p, acc, language.getConfig(), amount);
            } catch (Exception e) {
                p.sendMessage(configManager.getFormattedString(language.getConfig(), "invalid-amount"));
            }
        }

    }
}
