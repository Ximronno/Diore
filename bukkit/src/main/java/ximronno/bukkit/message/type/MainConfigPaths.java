package ximronno.bukkit.message.type;

import ximronno.bukkit.config.MainConfigCategories;
import ximronno.diore.api.polyglot.Path;

public enum MainConfigPaths implements Path {

    DEFAULT_LANGUAGE(MainConfigCategories.LANG, "default"),
    USE_LOGGER(MainConfigCategories.CONSOLE, "use_logger"),
    TRY_DETECT_CLIENT_LANGUAGE(MainConfigCategories.LANG, "try_detect_client"),
    AUTO_SAVE(MainConfigCategories.SAVE, "enabled"),
    AUTO_SAVE_MINUTES(MainConfigCategories.SAVE, "cooldown_minutes"),
    DIAMOND_NUGGET_NAME,
    LOAD_SAVED_ACCOUNTS(MainConfigCategories.SAVE, "load_saved_accounts"),
    MAX_SAVED_RECENT_TRANSACTIONS(MainConfigCategories.SAVE, "max_saved_transactions"),
    SAVE_MISSING_PATHS(MainConfigCategories.SAVE, "save_missing_paths"),
    SQL_ENABLED(MainConfigCategories.SQL, "enabled"),
    SQL_ALWAYS_LOAD_ON_JOIN(MainConfigCategories.SQL, "always_load_on_join"),
    SQL_HOST(MainConfigCategories.SQL, "host"),
    SQL_PORT(MainConfigCategories.SQL, "port"),
    SQL_DATABASE(MainConfigCategories.SQL, "database"),
    SQL_USERNAME(MainConfigCategories.SQL, "username"),
    SQL_PASSWORD(MainConfigCategories.SQL, "password"),
    SQL_LOAD_DELAY(MainConfigCategories.SQL, "load_delay"),
    HOOKS_USE_VAULT(MainConfigCategories.HOOKS, "vault.enabled"),
    HOOKS_USE_PLACEHOLDERAPI(MainConfigCategories.HOOKS, "placeholderapi.enabled"),
    GENERAL_MAX_WITHDRAW(MainConfigCategories.GENERAL, "max_withdraw"),
    GENERAL_DEFAULT_BALANCE(MainConfigCategories.GENERAL, "default_balance"),
    ENABLE_GUI(MainConfigCategories.GENERAL, "enable_gui"),
    STEAL_ON_KILL(MainConfigCategories.ON_DEATH, "killer_steal"),
    STEAL_PERCENTAGE(MainConfigCategories.ON_DEATH, "steal_perc"),
    MINIMUM_STEAL(MainConfigCategories.ON_DEATH, "min_amount_to_steal"),
    USE_DIAMONDS,
    USE_DIAMOND_NUGGETS;

    private final String path;

    MainConfigPaths() {
        this.path = name().toLowerCase();
    }
    MainConfigPaths(MainConfigCategories category, String path) {
        this.path = category.getName() + "." + path;
    }

    @Override
    public String path() {
        return path;
    }
}
