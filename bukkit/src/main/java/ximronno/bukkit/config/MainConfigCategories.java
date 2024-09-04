package ximronno.bukkit.config;

public enum MainConfigCategories {

    SAVE,
    CONSOLE,
    LANG,;

    public String getName() {
        return name().toLowerCase();
    }

}
