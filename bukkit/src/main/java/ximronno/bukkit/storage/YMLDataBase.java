package ximronno.bukkit.storage;

import org.bukkit.configuration.file.FileConfiguration;
import ximronno.bukkit.account.DioreAccount;
import ximronno.bukkit.message.type.AccountConfigPaths;
import ximronno.bukkit.message.type.DirectoriesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.Transaction;
import ximronno.diore.api.polyglot.Path;
import ximronno.diore.api.storage.DataBase;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;
import java.util.logging.Logger;

public class YMLDataBase implements DataBase {

    private final DioreAPI api;

    private final Logger logger;

    public YMLDataBase(DioreAPI api, Logger logger) {
        this.api = api;
        this.logger = logger;
    }

    /**
     * @return always null
     */
    @Override
    public Connection getConnection() {
        return null;
    }

    /**
     * Does nothing
     */
    @Override
    public void createDioreTables() {
    }

    /**
     * Does nothing
     */
    @Override
    public void initializeDataBase() {
    }

    @Override
    public void putAccountIntoTable(Account acc) throws IOException{
        File file = api.getConfigLoader().getFile(Path.of(DirectoriesPaths.ACCOUNTS.path() + acc.getUuid().toString() + ".yml"));

        FileConfiguration dataConfig = api.getConfigLoader().getFileConfiguration(file);

        dataConfig.set(AccountConfigPaths.UUID.path(), acc.getUuid().toString());
        dataConfig.set(AccountConfigPaths.BALANCE.path(), acc.getBalance());
        dataConfig.set(AccountConfigPaths.LOCALE.path(), acc.getLocale().toString());
        dataConfig.set(AccountConfigPaths.PRIVATE_BALANCE.path(), acc.isPrivateBalance());

        dataConfig.set(AccountConfigPaths.RECENT_TRANSACTIONS.path(), acc.getRecentTransactions());

        dataConfig.save(file);
    }

    @Override
    public Account getAccountFromTable(UUID uuid) {
        File file = api.getConfigLoader().getFile(Path.of(DirectoriesPaths.ACCOUNTS.path() + uuid.toString() + ".yml"));

        if(!file.exists()) return null;

        FileConfiguration dataConfig = api.getConfigLoader().getFileConfiguration(file);

        List<Transaction> list = new ArrayList<>();

        List<?> unknownList = dataConfig.getList(AccountConfigPaths.RECENT_TRANSACTIONS.path());
        if(unknownList != null) {
            unknownList.forEach(e -> {
                if(e instanceof Transaction transaction) {
                    list.add(transaction);
                }
                else if(e instanceof Map<?,?> map) {
                    map.forEach((k, v) -> list.add(Transaction.valueOf(Double.parseDouble(k.toString()), Long.parseLong(v.toString()))));
                }
            });
        }

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

    /**
     * Does nothing
     */
    @Override
    public void addRecentTransaction(UUID uuid, Transaction transaction) {
    }
}
