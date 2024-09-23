package ximronno.bukkit.config;

public enum MainConfigCategories {

    SAVE,
    CONSOLE,
    LANG,
    SQL,
    HOOKS,;

    public String getName() {
        return name().toLowerCase();
    }

}
