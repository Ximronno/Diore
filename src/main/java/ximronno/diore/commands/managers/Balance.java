package ximronno.diore.commands.managers;

import ximronno.diore.Diore;
import ximronno.diore.commands.DioreCommandManager;
import ximronno.diore.commands.subcommands.balance.*;
import ximronno.diore.commands.subcommands.balance.transactions.BalanceDeposit;
import ximronno.diore.commands.subcommands.balance.transactions.BalanceTransfer;
import ximronno.diore.commands.subcommands.balance.transactions.BalanceWithdraw;


public class Balance extends DioreCommandManager {
    public Balance(Diore plugin) {
        super(plugin);

        subCommands.add(new BalanceShow(plugin));
        subCommands.add(new BalanceHelp(plugin, this));

        subCommands.add(new BalanceGUI(plugin));
        subCommands.add(new BalanceInfo(plugin));
        subCommands.add(new BalanceTop(plugin));

        subCommands.add(new BalanceDeposit(plugin));
        subCommands.add(new BalanceWithdraw(plugin));
        subCommands.add(new BalancePublic(plugin));
        subCommands.add(new BalanceLanguage(plugin));

        subCommands.add(new BalanceTransfer(plugin));
    }

}
