package ximronno.bukkit.command.subcommands.balance.settings;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.command.DioreSubcommand;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;

import java.util.List;
import java.util.Locale;

public class BalancePrivacy extends DioreSubcommand {
    public BalancePrivacy(DioreAPI api) {
        super(api);
    }

    @Override
    public String getName() {
        return "private";
    }

    @Override
    public String getDescription(@Nullable Locale locale) {
        return api.getMessageManager().getMessage(CommandMessagesPaths.BALANCE_PRIVATE_BALANCE_DESCRIPTION, locale, true);
    }

    @Override
    public String getSyntax() {
        return "/balance private [true|false]";
    }

    @Override
    public List<String> getCompletion(Player p, String[] args, List<String> completion) {
        if(args.length == 2) {
            completion.addAll(List.of("true", "false"));
        }
        return completion;
    }

    @Override
    public boolean perform(Player p, Account acc, Locale accLocale, String[] args) {
        if(args.length < 2) {
            return false;
        }

        boolean privateToSet = Boolean.parseBoolean(args[1]);
        api.getAccountController().setPrivateBalance(p, acc, accLocale, privateToSet);

        return true;
    }
}
