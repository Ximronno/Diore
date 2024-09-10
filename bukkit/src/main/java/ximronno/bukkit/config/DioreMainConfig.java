package ximronno.bukkit.config;

import org.bukkit.plugin.java.JavaPlugin;
import ximronno.bukkit.message.type.MainConfigPaths;
import ximronno.diore.api.config.MainConfig;
import ximronno.diore.api.config.SQLConfig;

import java.util.Locale;

public class DioreMainConfig implements MainConfig {

    private final JavaPlugin plugin;

    private DioreSQLConfig sqlConfig;

    public DioreMainConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Locale getDefaultLanguage() {
        return new Locale(plugin.getConfig().getString(MainConfigPaths.DEFAULT_LANGUAGE.path()));
    }

    @Override
    public boolean useLogger() {
        return plugin.getConfig().getBoolean(MainConfigPaths.USE_LOGGER.path());
    }

    @Override
    public boolean useLocateClientLocale() {
        return plugin.getConfig().getBoolean(MainConfigPaths.TRY_DETECT_CLIENT_LANGUAGE.path());
    }

    @Override
    public boolean enableAutoSave() {
        return plugin.getConfig().getBoolean(MainConfigPaths.AUTO_SAVE.path());
    }

    @Override
    public int getAutoSaveTicks() {
        double minutes = plugin.getConfig().getDouble(MainConfigPaths.AUTO_SAVE_MINUTES.path());
        double seconds = minutes * 60;
        return (int) (seconds * 20);
    }

    @Override
    public int getMaxSavedRecentTransactions() {
        return plugin.getConfig().getInt(MainConfigPaths.MAX_SAVED_RECENT_TRANSACTIONS.path());
    }

    @Override
    public boolean loadSavedAccounts() {
        return plugin.getConfig().getBoolean(MainConfigPaths.LOAD_SAVED_ACCOUNTS.path());
    }

    @Override
    public boolean saveMissingPaths() {
        return plugin.getConfig().getBoolean(MainConfigPaths.SAVE_MISSING_PATHS.path());
    }

    @Override
    public SQLConfig getSQLConfig() {
        if(sqlConfig == null) {
            sqlConfig = new DioreSQLConfig(plugin);
        }
        return sqlConfig;
    }

    @Override
    public String getDiamondNuggetName() {
        return plugin.getConfig().getString(MainConfigPaths.DIAMOND_NUGGET_NAME.path());
    }

    @Override
    public boolean useDiamonds() {
        return plugin.getConfig().getBoolean(MainConfigPaths.USE_DIAMONDS.path());
    }

    @Override
    public boolean useDiamondsNuggets() {
        return plugin.getConfig().getBoolean(MainConfigPaths.USE_DIAMOND_NUGGETS.path());
    }
}
