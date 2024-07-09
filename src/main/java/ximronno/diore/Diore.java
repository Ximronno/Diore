package ximronno.diore;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ximronno.api.UpdateChecker;
import ximronno.api.command.CommandManager;
import ximronno.diore.commands.managers.Balance;
import ximronno.diore.commands.managers.Config;
import ximronno.diore.hooks.HookManager;
import ximronno.diore.listeners.InventoryListener;
import ximronno.diore.listeners.PlayerJoinListener;
import ximronno.diore.listeners.TransactionsListener;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.DioreConfigManager;
import ximronno.diore.recipes.DioreNuggetFromDeepDiore;
import ximronno.diore.recipes.DioreNuggetFromDiore;

public final class Diore extends JavaPlugin {
    private static Diore instance;
    private AccountManager accountManager;
    private DioreConfigManager configManager;
    private NamespacedKey menuKey;
    @Override
    public void onEnable() {

        instance = this;

        configManager = new DioreConfigManager(this);

        accountManager = new AccountManager(this);

        menuKey = new NamespacedKey(this, "diore-menu-key");

        setupCommandsAndTabCompleters();

        setupListeners(getServer().getPluginManager());

        setupRecipes(getServer());

        other();

        sendMessages();

    }

    @Override
    public void onDisable() {

        accountManager.getAccounts().forEach(account -> accountManager.saveAccountData(account.getOwner()));

        HookManager.getInstance().unregisterVault();
        HookManager.getInstance().unregisterPlaceholder();

    }
    private void setupCommandsAndTabCompleters() {
        PluginCommand balance = getCommand("balance");
        PluginCommand config = getCommand("config");

        CommandManager balanceCommandManager = new Balance(this);
        CommandManager configCommandManager = new Config(this);

        balance.setExecutor(balanceCommandManager);
        balance.setTabCompleter(balanceCommandManager);

        config.setExecutor(configCommandManager);
        config.setTabCompleter(configCommandManager);

    }
    private void setupListeners(PluginManager pM) {
        pM.registerEvents(new PlayerJoinListener(this), this);
        pM.registerEvents(new TransactionsListener(this), this);
        pM.registerEvents(new InventoryListener(), this);
    }
    private void setupRecipes(Server server) {

        server.addRecipe(new DioreNuggetFromDiore(new NamespacedKey(this, "diore-nugget-from-diore")));
        server.addRecipe(new DioreNuggetFromDeepDiore(new NamespacedKey(this, "diore-nugget-from-deep-diore")));

    }
    private void sendMessages() {

        new UpdateChecker(this, 117800).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("You are up to date!");
            } else {
                getLogger().info("There is a new update available: " + version + ".");
                getLogger().info("Download from: https://www.spigotmc.org/resources/diore.117800/ ");
            }
        });

        getLogger().severe("Expect bugs, this is still beta. Use at your own risk.");
        getLogger().severe("If you want to report bugs, please use https://github.com/ximronno/diore/issues");
        getLogger().info("Thanks for using Diore!");

    }
    private void other() {
        if(Bukkit.getPluginManager().getPlugin("Vault") != null) {
            HookManager.getInstance().registerVault();
        }
        HookManager.getInstance().registerPlaceholder();

        Bukkit.getOnlinePlayers().forEach(player -> accountManager.getOrCreateAccount(player));

        saveDefaultConfig();

        configManager.loadLanguageCFGS();
    }
    public AccountManager getAccountManager() {
        return accountManager;
    }
    public DioreConfigManager getConfigManager() {
        return configManager;
    }
    public NamespacedKey getMenuKey() {
        return menuKey;
    }

    public static Diore getInstance() {
        return instance;
    }


}
