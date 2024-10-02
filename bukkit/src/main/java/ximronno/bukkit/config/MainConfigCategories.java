package ximronno.bukkit.config;

public enum MainConfigCategories {

    SAVE,
    CONSOLE,
    LANG,
    SQL,
    HOOKS,
    GENERAL,
    ON_DEATH;

    public String getName() {
        return name().toLowerCase();
    }

}
