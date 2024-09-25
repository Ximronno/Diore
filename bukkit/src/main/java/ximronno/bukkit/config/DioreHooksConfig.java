package ximronno.bukkit.config;

import org.bukkit.plugin.java.JavaPlugin;
import ximronno.bukkit.message.type.MainConfigPaths;
import ximronno.diore.api.config.HooksConfig;

public class DioreHooksConfig implements HooksConfig {

    private final JavaPlugin plugin;

    public DioreHooksConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean useVault() {
        return plugin.getConfig().getBoolean(MainConfigPaths.HOOKS_USE_VAULT.path());
    }

    @Override
    public boolean usePlaceholderAPI() {
        return plugin.getConfig().getBoolean(MainConfigPaths.HOOKS_USE_PLACEHOLDERAPI.path());
    }
}
