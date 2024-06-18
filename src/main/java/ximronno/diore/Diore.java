package ximronno.diore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ximronno.diore.commands.Balance;
import ximronno.diore.hooks.HookManager;
import ximronno.diore.listeners.PlayerJoinListener;
import ximronno.diore.listeners.TransactionsListener;
import ximronno.diore.model.AccountManager;
import ximronno.diore.tabcompleters.BalanceTabCompleter;

public final class Diore extends JavaPlugin {
    private static Diore instance;
    private AccountManager accountManager;
    @Override
    public void onEnable() {

        instance = this;

        accountManager = new AccountManager();

        setupCommands();

        setupTabCompleters();

        setupListeners(getServer().getPluginManager());

        if(Bukkit.getPluginManager().getPlugin("Vault") != null) {
            HookManager.getInstance().registerVault();
        }
        HookManager.getInstance().registerPlaceholder();

        Bukkit.getOnlinePlayers().forEach(player -> accountManager.getOrCreateAccount(player.getUniqueId()));

    }

    @Override
    public void onDisable() {

        accountManager.getAccounts().forEach(account -> accountManager.saveAccountData(account.getOwner()));

        HookManager.getInstance().unregisterVault();

    }
    private void setupCommands() {
        getCommand("balance").setExecutor(new Balance());
    }
    private void setupTabCompleters() {
        getCommand("balance").setTabCompleter(new BalanceTabCompleter());
    }
    private void setupListeners(PluginManager pM) {
        pM.registerEvents(new PlayerJoinListener(), this);
        pM.registerEvents(new TransactionsListener(), this);
    }
    public AccountManager getAccountManager() {
        return accountManager;
    }

    public static Diore getInstance() {
        return instance;
    }
}
