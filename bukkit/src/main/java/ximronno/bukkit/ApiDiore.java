package ximronno.bukkit;

import ximronno.bukkit.account.DioreAccountLeaderBoard;
import ximronno.bukkit.account.managment.DioreAccountInfoFormatter;
import ximronno.bukkit.account.storage.DioreAccountLoader;
import ximronno.bukkit.account.managment.DioreAccountManager;
import ximronno.bukkit.account.storage.DioreAccountSaver;
import ximronno.bukkit.config.DioreConfigLoader;
import ximronno.bukkit.config.DioreConfigSaver;
import ximronno.bukkit.config.DioreMainConfig;
import ximronno.bukkit.message.DioreMessageLoader;
import ximronno.bukkit.message.DioreMessageManager;
import ximronno.bukkit.account.managment.DioreAccountController;
import ximronno.bukkit.storage.MySQLDataBase;
import ximronno.bukkit.storage.YMLDataBase;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.AccountLeaderBoard;
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
import ximronno.diore.api.storage.DataBase;

import java.sql.SQLException;
import java.util.UUID;

public class ApiDiore implements DioreAPI {

    private final AccountManager accountManager;

    private final AccountLeaderBoard accountLeaderBoard;

    private final AccountController accountController;

    private final AccountInfoFormatter accountInfoFormatter;

    private final ConfigLoader configLoader;

    private final ConfigSaver configSaver;

    private final MessageLoader messageLoader;

    private final AccountLoader accountLoader;

    private final AccountSaver accountSaver;

    private final MainConfig mainConfig;

    private DataBase dataBase;

    private final MessageManager messageManager;

    public ApiDiore(Diore plugin) {

        mainConfig = new DioreMainConfig(plugin);

        if(mainConfig.getSQLConfig().isEnabled()) {
            dataBase = new MySQLDataBase(this, plugin.getLogger());
            try {
                dataBase.initializeDataBase();
            } catch (SQLException e) {
                dataBase = new YMLDataBase(this, plugin.getLogger());
                plugin.getLogger().severe("Unable to use DB storage, using YML storage instead");
                e.printStackTrace();
            }
        }
        else {
            dataBase = new YMLDataBase(this, plugin.getLogger());
        }

        accountLoader = new DioreAccountLoader(this);
        accountSaver = new DioreAccountSaver(this, plugin, plugin.getLogger());

        configLoader = new DioreConfigLoader(plugin);
        configLoader.loadMessagesConfigs();

        configSaver = new DioreConfigSaver(this, plugin.getLogger());

        messageLoader = new DioreMessageLoader(this);

        messageManager = new DioreMessageManager(messageLoader.generatePolyglotConfig(), plugin.getLogger(), this);

        accountManager = new DioreAccountManager(plugin.getLogger(), this);

        accountLeaderBoard = new DioreAccountLeaderBoard(this);

        accountController = new DioreAccountController(this, plugin);

        accountInfoFormatter = new DioreAccountInfoFormatter(this);

    }

    @Override
    public Account getAccount(UUID uuid) {
        return accountManager.getAccount(uuid);
    }

    @Override
    public AccountManager getAccountManager() {
        return accountManager;
    }

    @Override
    public AccountLeaderBoard getAccountLeaderBoard() {
        return accountLeaderBoard;
    }

    @Override
    public AccountController getAccountController() {
        return accountController;
    }

    @Override
    public AccountInfoFormatter getAccountInfoFormatter() {
        return accountInfoFormatter;
    }

    @Override
    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    @Override
    public ConfigSaver getConfigSaver() {
        return configSaver;
    }

    @Override
    public AccountLoader getAccountLoader() {
        return accountLoader;
    }

    @Override
    public AccountSaver getAccountSaver() {
        return accountSaver;
    }

    @Override
    public MessageLoader getMessageLoader() {
        return messageLoader;
    }

    @Override
    public MessageManager getMessageManager() {
        return messageManager;
    }

    @Override
    public MainConfig getMainConfig() {
        return mainConfig;
    }

    @Override
    public DataBase getDataBase() {
        return dataBase;
    }

}
