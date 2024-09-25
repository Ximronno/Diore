package ximronno.bukkit.hooks;

import ximronno.diore.api.DiorePlugin;
import ximronno.diore.api.hook.HookManager;

public class DioreHookManager implements HookManager {

    private VaultHook vaultHook;

    private PlaceholderHook placeholderHook;

    private final DiorePlugin plugin;

    public DioreHookManager(DiorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerVault() {

        if(this.vaultHook == null) {
            this.vaultHook = new VaultHook(plugin.getAPI(), plugin.getJavaPlugin());
        }

    }

    @Override
    public void unregisterVault() {

        if(this.vaultHook != null) {
            this.vaultHook.unregister();
            this.vaultHook = null;
        }

    }

    @Override
    public void registerPlaceholders() {

        if(this.placeholderHook == null) {
            this.placeholderHook = new PlaceholderHook(plugin.getAPI());
        }

    }

    @Override
    public void unregisterPlaceholders() {

        if(this.placeholderHook != null) {
            this.placeholderHook.unregister();
            this.placeholderHook = null;
        }

    }


}
