package ximronno.bukkit.command.subcommands.balance.admin.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ximronno.bukkit.command.DioreSubcommand;
import ximronno.bukkit.message.type.ErrorMessagesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;

import java.util.Locale;

public abstract class AdminPlayerSubcommand extends DioreSubcommand {

    public AdminPlayerSubcommand(DioreAPI api) {
        super(api);
    }

    @Override
    public boolean perform(Player p, Account acc, Locale accLocale, String[] args) {
        if(args.length != 4) {
            return false;
        }
        Player target = Bukkit.getPlayer(args[2]);
        if(target == null) {
            p.sendMessage(api.getMessageManager().getMessage(ErrorMessagesPaths.PLAYER_NOT_FOUND, accLocale, true));
            return true;
        }
        Account targetAcc;
        if(target.equals(p)) {
            targetAcc = acc;
        }
        else {
            targetAcc = api.getAccount(target.getUniqueId());
            if(targetAcc == null) {
                p.sendMessage(api.getMessageManager().getMessage(ErrorMessagesPaths.TARGET_NO_ACCOUNT, accLocale,true));
                return true;
            }
        }
        return perform(p, target, acc, targetAcc, accLocale, args);
    }

    public abstract boolean perform(Player p, Player target, Account acc, Account targetAcc, Locale accLocale, String[] args);
}
