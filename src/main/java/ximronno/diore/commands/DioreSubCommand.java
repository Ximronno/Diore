package ximronno.diore.commands;

import org.bukkit.entity.Player;
import ximronno.api.command.SubCommand;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.diore.Diore;
import ximronno.diore.impl.Languages;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.DioreConfigManager;

public abstract class DioreSubCommand extends SubCommand {

    protected final Diore plugin;
    protected final DioreConfigManager configManager;
    protected final AccountManager accountManager;

    public DioreSubCommand(Diore plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.accountManager = plugin.getAccountManager();
    }

    @Override
    public void perform(Player p, String[] args) {
        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);

        if(acc == null) {
            p.sendMessage(configManager.getFormattedString("no-account-found"));
            return;
        }

        Language language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        perform(p, args, acc, language);
    }

    public abstract void perform(Player p, String[] args, Account acc, Language language);

}
