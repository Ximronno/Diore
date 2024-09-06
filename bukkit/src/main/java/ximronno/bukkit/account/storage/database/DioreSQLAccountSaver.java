package ximronno.bukkit.account.storage.database;

import org.bukkit.plugin.java.JavaPlugin;
import ximronno.bukkit.account.storage.DioreAccountSaver;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;

import java.sql.SQLException;
import java.util.logging.Logger;

public class DioreSQLAccountSaver extends DioreAccountSaver {

    public DioreSQLAccountSaver(DioreAPI api, JavaPlugin plugin, Logger logger) {
        super(api, plugin, logger);
    }

    @Override
    public boolean saveAccountToCFG(Account account) {
        try {
            api.getDataBase().putAccountIntoTable(account);
            return true;
        } catch (SQLException e) {
            if(api.getMainConfig().useLogger()) {
                logger.info("Error while saving account " + account.getUuid());
            }
            return false;
        }
    }

}
