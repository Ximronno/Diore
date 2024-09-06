package ximronno.bukkit.account.storage.database;

import ximronno.bukkit.account.storage.DioreAccountLoader;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;

import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class DioreSQLAccountLoader extends DioreAccountLoader {

    public DioreSQLAccountLoader(DioreAPI api, Logger logger) {
        super(api, logger);
    }

    @Override
    public Account getAccountFromCFG(UUID uuid) {
        try {
            return api.getDataBase().getAccountFromTable(uuid);
        } catch (SQLException e) {
            return null;
        }
    }
}
