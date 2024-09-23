package ximronno.bukkit.account.storage;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import ximronno.bukkit.account.DioreAccount;
import ximronno.bukkit.message.type.AccountConfigPaths;
import ximronno.bukkit.message.type.DirectoriesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.storage.AccountLoader;
import ximronno.diore.api.account.Transaction;
import ximronno.diore.api.polyglot.Path;

import java.io.File;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class DioreAccountLoader implements AccountLoader {

    protected final DioreAPI api;

    protected Logger logger;

    public DioreAccountLoader(DioreAPI api, Logger logger) {
        this.api = api;
        if(api.getMainConfig().useLogger()) {
            this.logger = logger;
        }
    }

    @Override
    public Account getAccountFromCFG(UUID uuid) {
        try {
            return api.getDataBase().getAccountFromTable(uuid);
        } catch (SQLException e) {
            return null;
        }

    }

    @Override
    public List<Account> savedAccounts() {
        List<Account> preLoadedAccounts = new ArrayList<>();
        for(OfflinePlayer target : Bukkit.getOfflinePlayers()) {
            if(api.getAccountManager().hasAccount(target.getUniqueId())) continue;

            Account targetAcc = getAccountFromCFG(target.getUniqueId());
            if(targetAcc == null) continue;

            preLoadedAccounts.add(targetAcc);
        }
        return preLoadedAccounts;
    }

}
