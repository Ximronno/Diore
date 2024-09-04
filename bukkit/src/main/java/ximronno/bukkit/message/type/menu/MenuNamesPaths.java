package ximronno.bukkit.message.type.menu;

import ximronno.bukkit.menu.Menus;
import ximronno.diore.api.polyglot.Path;

public enum MenuNamesPaths implements Path {

    ACCOUNT_MENU_NAME(Menus.ACCOUNT_MENU),
    LOCALE_SETTER_MENU_NAME(Menus.LOCALE_SETTER_MENU),
    MAIN_MENU_NAME(Menus.MAIN_MENU),
    PUBLIC_SETTER_MENU_NAME(Menus.PRIVACY_SETTER_MENU),
    TRANSACTIONS_MENU_NAME(Menus.TRANSACTIONS_MENU),
    DEPOSIT_MENU_NAME(Menus.DEPOSIT_MENU),
    WITHDRAW_MENU_NAME(Menus.WITHDRAW_MENU),
    TRANSFER_SELECTOR_MENU_NAME(Menus.TRANSFER_SELECTOR_MENU),
    TRANSFER_SUM_SELECTOR_MENU_NAME(Menus.TRANSFER_SUM_SELECTOR_MENU),
    TRANFER_CONFIRM_MENU_NAME(Menus.TRANSFER_CONFIRM_MENU),
    RECENT_TRANSACTIONS_MENU_NAME(Menus.RECENT_TRANSACTIONS_MENU);

    private final String path;

    MenuNamesPaths(Menus menu) {
        this.path = "menu." + menu.getName() + ".name";
    }

    @Override
    public String path() {
        return path;
    }

}
