package ximronno.bukkit.command;

import org.bukkit.entity.Player;
import ximronno.bukkit.message.type.ErrorMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.command.SubCommand;

import java.util.List;
import java.util.Locale;

public abstract class DioreSubcommand extends SubCommand {

    protected final DioreAPI api;

    public DioreSubcommand(DioreAPI api) {
        this.api = api;
    }

    @Override
    public boolean perform(Player p, String[] args) {

        Account acc = api.getAccount(p.getUniqueId());
        if(acc == null) {
            p.sendMessage(api.getMessageManager().getDefaultMessage(ErrorMessagesPaths.SENDER_NO_ACCOUNT, true));
            return false;
        }

        return perform(p, acc, acc.getLocale(), args);
    }

    @Override
    public List<String> getCompletion(Player p, String[] args, List<String> completion) {
        return completion;
    }

    public abstract boolean perform(Player p, Account acc, Locale accLocale, String[] args);

}
