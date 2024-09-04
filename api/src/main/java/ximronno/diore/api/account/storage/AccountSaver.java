package ximronno.diore.api.account.storage;

import ximronno.diore.api.account.Account;

public interface AccountSaver {

    boolean saveAccountToCFG(Account account);

    void saveAllAccounts();

    void enableAutoSaving(int ticks);

    void disableAutoSaving();
}
