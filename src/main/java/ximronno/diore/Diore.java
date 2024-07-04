package ximronno.diore;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ximronno.diore.commands.managers.BalanceNew;
import ximronno.diore.hooks.HookManager;
import ximronno.diore.listeners.InventoryListener;
import ximronno.diore.listeners.PlayerJoinListener;
import ximronno.diore.listeners.TransactionsListener;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.ConfigManager;
import ximronno.diore.recipes.DioreNuggetFromDeepDiore;
import ximronno.diore.recipes.DioreNuggetFromDiore;
import ximronno.diore.tabcompleters.BalanceTabCompleter;

public final class Diore extends JavaPlugin {
    private static Diore instance;
    private AccountManager accountManager;
    private ConfigManager configManager;
    private NamespacedKey menuKey;
    @Override
    public void onEnable() {

        instance = this;

        accountManager = new AccountManager(this);

        configManager = new ConfigManager(this);

        menuKey = new NamespacedKey(this, "diore-menu-key");

        setupCommands();

        setupTabCompleters();

        setupListeners(getServer().getPluginManager());

        setupRecipes(getServer());

        if(Bukkit.getPluginManager().getPlugin("Vault") != null) {
            HookManager.getInstance().registerVault();
        }
        HookManager.getInstance().registerPlaceholder();

        Bukkit.getOnlinePlayers().forEach(player -> accountManager.getOrCreateAccount(player));

        this.saveDefaultConfig();

        configManager.loadLanguageCFGS();

    }

    @Override
    public void onDisable() {

        accountManager.getAccounts().forEach(account -> accountManager.saveAccountData(account.getOwner()));

        HookManager.getInstance().unregisterVault();
        HookManager.getInstance().unregisterPlaceholder();

    }
    private void setupCommands() {
        getCommand("balance").setExecutor(new BalanceNew(this));
    }
    private void setupTabCompleters() {
        getCommand("balance").setTabCompleter(new BalanceTabCompleter());
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
    public AccountManager getAccountManager() {
        return accountManager;
    }
    public ConfigManager getConfigManager() {
        return configManager;
    }
    public NamespacedKey getMenuKey() {
        return menuKey;
    }

    public static Diore getInstance() {
        return instance;
    }


}
