package ximronno.diore.hooks;

import org.bukkit.Bukkit;

public class HookManager {

    private static HookManager instance = new HookManager();

    private VaultHook vaultHook;
    public void registerVault() {

        if(this.vaultHook == null) {
            this.vaultHook = new VaultHook();
        }

    }
    public void unregisterVault() {

        if(this.vaultHook == null) return;
        this.vaultHook.unregister();
        this.vaultHook = null;

    }
    public void registerPlaceholder() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return;
        new PlaceholderHook().register();
    }
    public static HookManager getInstance() {
        return instance;
    }
}
