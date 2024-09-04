package ximronno.diore.api;


import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.managment.AccountController;
import ximronno.diore.api.account.managment.AccountInfoFormatter;
import ximronno.diore.api.account.storage.AccountLoader;
import ximronno.diore.api.account.managment.AccountManager;
import ximronno.diore.api.account.storage.AccountSaver;
import ximronno.diore.api.config.ConfigLoader;
import ximronno.diore.api.config.ConfigSaver;
import ximronno.diore.api.config.MainConfig;
import ximronno.diore.api.message.MessageLoader;
import ximronno.diore.api.message.MessageManager;

import java.util.UUID;

public interface DioreAPI {

    Account getAccount(UUID uuid);

    AccountManager getAccountManager();

    AccountController getAccountController();

    AccountInfoFormatter getAccountInfoFormatter();

    ConfigLoader getConfigLoader();

    ConfigSaver getConfigSaver();

    AccountLoader getAccountLoader();

    AccountSaver getAccountSaver();

    MessageLoader getMessageLoader();

    MessageManager getMessageManager();

    MainConfig getMainConfig();

    static DioreAPI getInstance(){
        return DioreProvider.getInstance();
    }

}
