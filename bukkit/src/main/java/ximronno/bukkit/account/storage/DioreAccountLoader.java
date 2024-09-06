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

        File file = api.getConfigLoader().getFile(Path.of(DirectoriesPaths.ACCOUNTS.path() + uuid.toString() + ".yml"));

        if(!file.exists()) return null;

        FileConfiguration dataConfig = api.getConfigLoader().getFileConfiguration(file);

        List<Transaction> list = new ArrayList<>();

        dataConfig.getMapList(AccountConfigPaths.RECENT_TRANSACTIONS.path()).forEach(m -> m.forEach((k, v) -> list.add(new Transaction(Double.parseDouble(k.toString()), Long.parseLong(v.toString())))));

        double balance = dataConfig.getDouble(AccountConfigPaths.BALANCE.path());
        Locale locale = new Locale(dataConfig.getString(AccountConfigPaths.LOCALE.path()));
        boolean isPublicBalance = dataConfig.getBoolean(AccountConfigPaths.PRIVATE_BALANCE.path());

        if(logger != null) {
            logger.info("Loaded account for: " + uuid);
        }

        return DioreAccount.builder()
                .setRecentTransactions(list)
                .setUuid(uuid)
                .setBalance(balance)
                .setLocale(locale)
                .setPrivateBalance(isPublicBalance)
                .build();
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
