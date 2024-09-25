package ximronno.bukkit.config;

import org.bukkit.plugin.java.JavaPlugin;
import ximronno.bukkit.message.type.MainConfigPaths;
import ximronno.diore.api.config.SQLConfig;

public class DioreSQLConfig implements SQLConfig {

    private final JavaPlugin plugin;

    public DioreSQLConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getConfig().getBoolean(MainConfigPaths.SQL_ENABLED.path());
    }

    @Override
    public String getHost() {
        return plugin.getConfig().getString(MainConfigPaths.SQL_HOST.path());
    }

    @Override
    public String getPort() {
        return plugin.getConfig().getString(MainConfigPaths.SQL_PORT.path());
    }

    @Override
    public String getDataBase() {
        return plugin.getConfig().getString(MainConfigPaths.SQL_DATABASE.path());
    }

    @Override
    public String getUsername() {
        return plugin.getConfig().getString(MainConfigPaths.SQL_USERNAME.path());
    }

    @Override
    public String getPassword() {
        return plugin.getConfig().getString(MainConfigPaths.SQL_PASSWORD.path());
    }

    @Override
    public long getLoadDelay() {
        return plugin.getConfig().getLong(MainConfigPaths.SQL_LOAD_DELAY.path());
    }

    @Override
    public boolean alwaysLoadOnJoin() {
        return plugin.getConfig().getBoolean(MainConfigPaths.SQL_ENABLED.path());
    }
}
