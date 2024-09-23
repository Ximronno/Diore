package ximronno.bukkit.account.managment;



import ximronno.bukkit.account.DioreAccount;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.managment.AccountManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DioreAccountManager implements AccountManager {

    private final ConcurrentHashMap<UUID, Account> accounts = new ConcurrentHashMap<>();

    private Logger logger;

    public DioreAccountManager(Logger logger, DioreAPI api) {
        if(api.getMainConfig().useLogger()) {
            this.logger = logger;
        }
    }

    @Override
    public Account getAccount(UUID uuid) {
        return accounts.get(uuid);
    }

    @Override
    public void addAccount(Account account) {
        if(!accounts.containsKey(account.getUuid())) {
            accounts.put(account.getUuid(), account);
            if (logger != null) {
                logger.info("Added account " + account.getUuid());
            }
        }
    }

    @Override
    public void removeAccount(Account account) {
        accounts.remove(account.getUuid());
        if (logger != null) {
            logger.info("Removed account " + account.getUuid());
        }
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return accounts.containsKey(uuid);
    }

    @Override
    public HashMap<UUID, Account> getAccountsMap() {
        return new HashMap<>(accounts);
    }

    @Override
    public List<Account> getAccountsList() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public Account createNewAccount(UUID uuid) {
        Account acc = DioreAccount.builder()
                .setUuid(uuid)
                .build();
        if (logger != null) {
            logger.info("Created new account " + uuid);
        }
        return acc;
    }

    @Override
    public Collection<Account> getOnlineAccounts() {
        return accounts.values();
    }
}
