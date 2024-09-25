package ximronno.bukkit.account.storage;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.storage.AccountLoader;

import java.sql.SQLException;
import java.util.*;

public class DioreAccountLoader implements AccountLoader {

    private final DioreAPI api;

    public DioreAccountLoader(DioreAPI api) {
        this.api = api;
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
