package ximronno.bukkit.account.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ximronno.bukkit.message.type.AccountConfigPaths;
import ximronno.bukkit.message.type.DirectoriesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.storage.AccountSaver;
import ximronno.diore.api.polyglot.Path;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DioreAccountSaver implements AccountSaver {

    protected final DioreAPI api;

    private final JavaPlugin plugin;

    protected final Logger logger;

    private boolean autoSaving = false;

    public DioreAccountSaver(DioreAPI api, JavaPlugin plugin, Logger logger) {
        this.api = api;
        this.plugin = plugin;
        this.logger = logger;
    }

    @Override
    public boolean saveAccountToCFG(Account account) {
        File file = api.getConfigLoader().getFile(Path.of(DirectoriesPaths.ACCOUNTS.path() + account.getUuid().toString() + ".yml"));

        FileConfiguration dataConfig = api.getConfigLoader().getFileConfiguration(file);

        dataConfig.set(AccountConfigPaths.UUID.path(), account.getUuid().toString());
        dataConfig.set(AccountConfigPaths.BALANCE.path(), account.getBalance());
        dataConfig.set(AccountConfigPaths.LOCALE.path(), account.getLocale().toString());
        dataConfig.set(AccountConfigPaths.PRIVATE_BALANCE.path(), account.isPrivateBalance());

        List<Map<?,?>> mapList = new ArrayList<>();
        account.getRecentTransactions().forEach(t -> mapList.add(t.serialize()));

        dataConfig.set(AccountConfigPaths.RECENT_TRANSACTIONS.path(), mapList);

        try {
            dataConfig.save(file);
            return true;
        } catch (IOException e) {
            if(api.getMainConfig().useLogger()) {
                logger.info("Error while saving account " + account.getUuid());
            }
            return false;
        }
    }

    @Override
    public void saveAllAccounts() {
        for(Account acc : api.getAccountManager().getAccountsList()) {
            saveAccountToCFG(acc);
        }
        if(api.getMainConfig().useLogger()) {
            logger.info("Saved all accounts (" + api.getAccountManager().getAccountsList().size() + ")");
        }
    }

    @Override
    public void enableAutoSaving(int ticks) {
        if(autoSaving) return;
        autoSaving = true;

        if(api.getMainConfig().useLogger()) {
            logger.info("Enabled auto saving every " + ticks + " ticks");
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!autoSaving) cancel();

                saveAllAccounts();

            }

        }.runTaskTimer(plugin, 0, ticks);

    }

    @Override
    public void disableAutoSaving() {
        if(autoSaving) autoSaving = false;
    }
}
