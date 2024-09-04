package ximronno.diore.api.account.managment;

import ximronno.diore.api.account.Account;

import java.util.*;

public interface AccountManager {

    Account getAccount(UUID uuid);

    void addAccount(Account account);

    void removeAccount(Account account);

    boolean hasAccount(UUID uuid);

    HashMap<UUID, Account> getAccountsMap();

    List<Account> getAccountsList();

    Account createNewAccount(UUID uuid);

    Collection<Account> getOnlineAccounts();

}
