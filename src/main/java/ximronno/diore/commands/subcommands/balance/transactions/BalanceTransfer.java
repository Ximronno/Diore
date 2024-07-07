package ximronno.diore.commands.subcommands.balance.transactions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.commands.DioreSubCommand;
import ximronno.diore.impl.Languages;
import ximronno.diore.utils.AccountUtils;

public class BalanceTransfer extends DioreSubCommand {
    public BalanceTransfer(Diore plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "transfer";
    }

    @Override
    public String getDescription(@Nullable FileConfiguration config) {
        if(config == null) return ChatColor.BLUE + "Transfers ores to another player";
        return configManager.getFormattedString(config, "balance-transfer-description");
    }

    @Override
    public String getSyntax() {
        return "/balance transfer <player> <amount>";
    }
    @Override
    public void perform(Player p, String[] args, Account acc, Languages language) {

        if(args.length < 3) {

            p.sendMessage(configManager.getFormattedString(language.getCFG(), "balance-help-format")
                    .replace("<syntax>", getSyntax())
                    .replace("<description>", getDescription(language.getCFG())));

        }
        else {

            Player target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                p.sendMessage(plugin.getConfigManager().getFormattedString(language.getCFG(), "player-not-found"));
                return;
            }
            if(isSamePlayer(p, target)) {
                p.sendMessage(plugin.getConfigManager().getFormattedString(language.getCFG(), "cannot-transfer-to-yourself"));
                return;
            }

            Account targetAcc = accountManager.getAccount(target.getUniqueId()).orElse(null);

            double amount;
            try {
                amount = Double.parseDouble(args[2]);
                AccountUtils.tryTransfer(p, acc, targetAcc, language.getCFG(), amount);
            } catch (Exception e) {
                p.sendMessage(configManager.getFormattedString(language.getCFG(), "invalid-amount"));
            }
        }

    }

    private boolean isSamePlayer(Player p1, Player p2) {
        return p1.getUniqueId().equals(p2.getUniqueId());
    }
}
