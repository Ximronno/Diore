package ximronno.bukkit.command.subcommands.balance.info;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.Permissions;
import ximronno.bukkit.command.DioreSubcommand;
import ximronno.bukkit.command.subcommands.SubCommands;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.SortingVariant;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.AccountLeaderBoard;
import ximronno.diore.api.account.managment.AccountInfoFormatter;
import ximronno.diore.api.message.MessageManager;

import java.util.*;

public class BalanceLeaderBoard extends DioreSubcommand {

    private final AccountLeaderBoard accountLeaderBoard;

    private final MessageManager messageManager;

    private final AccountInfoFormatter accountInfoFormatter;

    public BalanceLeaderBoard(DioreAPI api) {
        super(api);
        this.accountLeaderBoard = api.getAccountLeaderBoard();
        this.messageManager = api.getMessageManager();
        this.accountInfoFormatter = api.getAccountInfoFormatter();
    }

    @Override
    public String getName() {
        return SubCommands.BALANCE_LEADERBOARD.getName();
    }

    @Override
    public Permission getSubCommandPermission() {
        return Permissions.BALANCE_LEADERBOARD.getPermission();
    }

    @Override
    public String getDescription(@Nullable Locale locale) {
        return messageManager.getMessage(CommandMessagesPaths.BALANCE_LEADERBOARD_DESCRIPTION, locale, true);
    }

    @Override
    public String getSyntax() {
        return "/balance leaderboard [sortType]";
    }

    @Override
    public List<String> getCompletion(Player p, String[] args, List<String> completion) {
        if(args.length == 2) {
            Collection<String> collection = new ArrayList<>();
            for (SortingVariant sortVariant : SortingVariant.values()) {

                collection.add(sortVariant.name().toLowerCase());

            }
            completion.addAll(collection);
        }

        return completion;
    }

    @Override
    public boolean perform(Player p, Account acc, Locale accLocale, String[] args) {

        int limit = 5;

        SortingVariant sortType = SortingVariant.NO_SORT;
        if(args.length > 1) {
            try {
                sortType = SortingVariant.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }

        p.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_LEADERBOARD_HEADER, accLocale, true));

        int i = 1;
        for(Account target : accountLeaderBoard.getSortedLeaderBoard(sortType)) {
            if(i >= limit) break;

            p.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_LEADERBOARD_FORMAT, accLocale, true, Map.of(
                    "{i}", String.valueOf(i),
                    "{target_name}", Bukkit.getOfflinePlayer(target.getUuid()).getName(),
                    "{balance}", accountInfoFormatter.getFormattedAmount(target.getBalance(), accLocale)
            )));

            i++;
        }

        return true;
    }

}
