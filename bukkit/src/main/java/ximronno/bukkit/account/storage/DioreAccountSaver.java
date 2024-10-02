package ximronno.bukkit.account.storage;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.storage.AccountSaver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DioreAccountSaver implements AccountSaver {

    protected final DioreAPI api;

    protected final JavaPlugin plugin;

    protected final Logger logger;

    private boolean autoSaving = false;

    public DioreAccountSaver(DioreAPI api, JavaPlugin plugin, Logger logger) {
        this.api = api;
        this.plugin = plugin;
        this.logger = logger;
    }

    @Override
    public boolean saveAccountToCFG(Account account) {
        try {
            api.getDataBase().putAccountIntoTable(account);
            return true;
        } catch (SQLException | IOException e) {
            if(api.getMainConfig().useLogger()) {
                logger.info("Error while saving account " + account.getUuid());
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public void saveAllAccounts() {
        for(Account acc : api.getAccountManager().getAccountsList()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    saveAccountToCFG(acc);
                }
            }.runTaskAsynchronously(plugin);
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
