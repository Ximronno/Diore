package ximronno.bukkit.command.manager;

import ximronno.bukkit.command.Commands;
import ximronno.bukkit.command.subcommands.balance.info.BalanceHelp;
import ximronno.bukkit.command.subcommands.balance.info.BalanceInfo;
import ximronno.bukkit.command.subcommands.balance.settings.BalanceLocale;
import ximronno.bukkit.command.subcommands.balance.settings.BalancePrivacy;
import ximronno.bukkit.command.subcommands.balance.transactions.BalanceDeposit;
import ximronno.bukkit.command.subcommands.balance.transactions.BalanceTransfer;
import ximronno.bukkit.command.subcommands.balance.transactions.BalanceWithdraw;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.DiorePlugin;
import ximronno.diore.api.command.BaseCommand;
import ximronno.diore.api.command.SubCommand;

public class BalanceCommand extends BaseCommand {

    public BalanceCommand(DioreAPI api, DiorePlugin plugin) {
        super(plugin.getJavaPlugin(), Commands.BALANCE.getName());

        SubCommand balanceInfo = new BalanceInfo(api);
        SubCommand balanceHelp = new BalanceHelp(getSubCommands(), api);

        subCommands.add(balanceHelp);
        subCommands.add(balanceInfo);
        subCommands.add(new BalanceLocale(api));
        subCommands.add(new BalancePrivacy(api));

        subCommands.add(new BalanceWithdraw(api));
        subCommands.add(new BalanceDeposit(api));
        subCommands.add(new BalanceTransfer(api));

        noLengthCommand = balanceInfo;
        helpCommand = balanceHelp;
    }

}
