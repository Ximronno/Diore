package ximronno.diore.api.account.storage;

import ximronno.diore.api.account.Account;

import java.util.List;
import java.util.UUID;

public interface AccountLoader {

    Account getAccountFromCFG(UUID uuid);

    List<Account> savedAccounts();

}
