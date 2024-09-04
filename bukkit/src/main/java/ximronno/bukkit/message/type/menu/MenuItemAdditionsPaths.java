package ximronno.bukkit.message.type.menu;

import ximronno.bukkit.menu.Menus;
import ximronno.diore.api.polyglot.Path;

public enum MenuItemAdditionsPaths implements Path {


    LANGUAGE(Menus.LOCALE_SETTER_MENU, "equipped"),
    LANGUAGE_FORMAT("language", Menus.LOCALE_SETTER_MENU, "format"),
    LANGUAGE_DEFAULT("language", Menus.LOCALE_SETTER_MENU, "default"),
    RECENT_TRANSACTIONS(Menus.TRANSACTIONS_MENU, "format"),
    NO_RECENT_TRANSACTIONS("recent_transactions", Menus.TRANSACTIONS_MENU, "no_recent_transactions"),;


    private final String path;

    MenuItemAdditionsPaths(Menus menu, String addition) {
        this.path = "menu." + menu.name().toLowerCase() + ".item." + name().toLowerCase() + "." + addition;
    }
    MenuItemAdditionsPaths(String item, Menus menu, String addition) {
        this.path = "menu." + menu.name().toLowerCase() + ".item." + item + "." + addition;
    }

    @Override
    public String path() {
        return path;
    }

}