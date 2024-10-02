package ximronno.bukkit.message.type.menu;

import ximronno.bukkit.menu.Menus;
import ximronno.diore.api.polyglot.Path;

public enum MenuItemAdditionsPaths implements Path {

    LANGUAGE(Menus.LOCALE_SETTER_MENU, "equipped"),
    LANGUAGE_FORMAT("language", Menus.LOCALE_SETTER_MENU, "format"),
    LANGUAGE_DEFAULT("language", Menus.LOCALE_SETTER_MENU, "default"),
    RECENT_TRANSACTIONS(Menus.TRANSACTIONS_MENU, "format"),
    NO_RECENT_TRANSACTIONS("recent_transactions", Menus.TRANSACTIONS_MENU, "no_recent_transactions"),
    RECENT_TRANSACTION_TYPE("recent_transactions", Menus.TRANSACTIONS_MENU, "type"),
    LEADERBOARD_SORT_ARROW("leaderboard_sort", Menus.LEADERBOARD_MENU, "arrow_format"),
    LEADERBOARD_SENDER_NO_PLACE("leaderboard_sender", Menus.LEADERBOARD_MENU, "no_place");

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
