package ximronno.bukkit.config;

public enum MainConfigCategories {

    SAVE,
    CONSOLE,
    LANG,
    SQL,;

    public String getName() {
        return name().toLowerCase();
    }

}
