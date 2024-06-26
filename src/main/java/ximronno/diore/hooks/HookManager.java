package ximronno.diore.hooks;

import org.bukkit.Bukkit;
import ximronno.diore.Diore;

import java.util.logging.Level;

public class HookManager {

    private static final HookManager instance = new HookManager();

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
        Diore.getInstance().getLogger().log(Level.INFO,"Hooked into PlaceholderAPI");
        new PlaceholderHook().register();

    }
    public void unregisterPlaceholder() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return;
        new PlaceholderHook().unregister();
    }
    public static HookManager getInstance() {
        return instance;
    }
}
