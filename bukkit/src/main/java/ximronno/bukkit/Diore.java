package ximronno.bukkit;

import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ximronno.bukkit.command.manager.BalanceCommand;
import ximronno.bukkit.hooks.DioreHookManager;
import ximronno.bukkit.listeners.InventoryListener;
import ximronno.bukkit.listeners.OnPlayerJoinListener;
import ximronno.bukkit.listeners.TransactionsListener;
import ximronno.bukkit.util.ApiRegistrationUtil;
import ximronno.bukkit.util.HookManagerRegistrationUtil;
import ximronno.bukkit.util.KeyRegistrationUtil;
import ximronno.bukkit.util.PluginRegistrationUtil;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.DiorePlugin;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.config.MainConfig;
import ximronno.diore.api.hook.HookManager;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;
import ximronno.diore.api.polyglot.Path;

import java.util.Locale;
import java.util.UUID;

public final class Diore extends JavaPlugin implements DiorePlugin {

    private ApiDiore api;

    private NamespacedKey dioreKey;

    private boolean installedPlaceholderAPI;

    @Override
    public void onEnable() {

        api = new ApiDiore(this);
        ApiRegistrationUtil.register(api);

        PluginRegistrationUtil.register(this);

        saveDefaultConfig();

        loadListeners(getServer().getPluginManager());

        registerBaseCommands();

        for(Player target : getServer().getOnlinePlayers()) {

            Account acc = api.getAccountLoader().getAccountFromCFG(target.getUniqueId());
            if(acc == null) acc = api.getAccountManager().createNewAccount(target.getUniqueId());

            api.getAccountManager().addAccount(acc);

        }

        if(api.getMainConfig().enableAutoSave()) {
            api.getAccountSaver().enableAutoSaving(api.getMainConfig().getAutoSaveTicks());
        }

        if(api.getMainConfig().loadSavedAccounts()) {
            for(Account acc : api.getAccountLoader().savedAccounts()) {
                api.getAccountManager().addAccount(acc);
            }
        }

        this.dioreKey = new NamespacedKey(this, "key");
        KeyRegistrationUtil.register(dioreKey);

        HookManager manager = new DioreHookManager(this);
        HookManagerRegistrationUtil.register(manager);

        if(Bukkit.getPluginManager().getPlugin("Vault") != null) {
            if(api.getMainConfig().getHooksConfig().useVault()) {
                manager.registerVault();
            }
        }

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            if(api.getMainConfig().getHooksConfig().usePlaceholderAPI()) {
                this.installedPlaceholderAPI = true;
                manager.registerPlaceholders();
            }
        }

        new UpdateChecker(this, UpdateCheckSource.SPIGET, "117800")
                .setDownloadLink(117800)
                .setChangelogLink(117800)
                .setNotifyOpsOnJoin(true)
                .checkEveryXHours(12)
                .checkNow();
    }

    @Override
    public void onDisable() {
        HookManagerRegistrationUtil.unregister();
        KeyRegistrationUtil.unregister();
        PluginRegistrationUtil.unregister();

        for(Player target : getServer().getOnlinePlayers()) {
            if(target.getOpenInventory().getTopInventory().getHolder() instanceof Menu menu) {
                menu.close(target);
            }
        }

        api.getConfigSaver().saveAllStoredConfigs();

        for(Account account : api.getAccountManager().getOnlineAccounts()) {
            api.getAccountSaver().saveAccountToCFG(account);
        }

        api.getAccountSaver().disableAutoSaving();

        ApiRegistrationUtil.unregister();
    }

    private void registerBaseCommands() {
        new BalanceCommand(api, this);
    }

    private void loadListeners(PluginManager pM) {
        pM.registerEvents(new OnPlayerJoinListener(api, this), this);
        pM.registerEvents(new TransactionsListener(this), this);
        pM.registerEvents(new InventoryListener(), this);
    }

    @Override
    public String getMessage(Path path, Locale locale, boolean useChatColor) {
        return api.getMessageManager().getMessage(path, locale, useChatColor);
    }

    @Override
    public DioreAPI getAPI() {
        return api;
    }

    @Override
    public Account getAccount(UUID id) {
        return api.getAccount(id);
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public NamespacedKey getNamespacedKey() {
       return dioreKey;
    }

    public static ItemStack getDiamondNugget(int amount) {
        MainConfig config = DioreAPI.getInstance().getMainConfig();
        if(!config.useDiamondsNuggets() && !config.useDiamonds()) {
            return ItemBuilder.builder()
                    .setMaterial(Material.DIAMOND)
                    .setAmount(amount)
                    .build();
        }
        return ItemBuilder.builder()
                .setMaterial(Material.TUBE_CORAL)
                .setAmount(amount)
                .setDisplayName(config.getDiamondNuggetName())
                .addPersistentData(DiorePlugin.getKey(), "diamond_nugget")
                .build();
    }

    public static ItemStack getDiamondNuggetRaw(int amount) {
        MainConfig config = DioreAPI.getInstance().getMainConfig();
        return ItemBuilder.builder()
                .setMaterial(Material.TUBE_CORAL)
                .setAmount(amount)
                .setDisplayName(config.getDiamondNuggetName())
                .addPersistentData(DiorePlugin.getKey(), "diamond_nugget")
                .build();
    }

    @Override
    public boolean usingPlaceholderAPI() {
        return installedPlaceholderAPI;
    }
}
