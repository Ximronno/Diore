package ximronno.bukkit.message.type.menu;

import ximronno.bukkit.menu.Menus;
import ximronno.diore.api.polyglot.Path;

public enum MenuItemNamesPaths implements Path {

    BACK_BUTTON,
    NEXT_PAGE_BUTTON,
    PREVIOUS_PAGE_BUTTON,
    PLAYER_HEAD(Menus.MAIN_MENU),
    GITHUB_ISSUES(Menus.MAIN_MENU),
    LEADERBOARD_HEAD(Menus.MAIN_MENU),
    TRANSACTIONS(Menus.ACCOUNT_MENU),
    LOCALE_SETTER(Menus.ACCOUNT_MENU),
    PRIVACY_SETTER_TRUE(Menus.ACCOUNT_MENU),
    PRIVACY_SETTER_FALSE(Menus.ACCOUNT_MENU),
    DEPOSIT(Menus.TRANSACTIONS_MENU),
    WITHDRAW(Menus.TRANSACTIONS_MENU),
    TRANSFER(Menus.TRANSACTIONS_MENU),
    RECENT_TRANSACTIONS(Menus.TRANSACTIONS_MENU),
    DEPOSIT_ALL(Menus.DEPOSIT_MENU),
    DEPOSIT_HALF(Menus.DEPOSIT_MENU),
    DEPOSIT_QUARTER(Menus.DEPOSIT_MENU),
    DEPOSIT_CUSTOM(Menus.DEPOSIT_MENU),
    DEPOSIT_CUSTOM_AMOUNT(Menus.DEPOSIT_MENU),
    WITHDRAW_ALL(Menus.WITHDRAW_MENU),
    WITHDRAW_HALF(Menus.WITHDRAW_MENU),
    WITHDRAW_QUARTER(Menus.WITHDRAW_MENU),
    WITHDRAW_CUSTOM(Menus.WITHDRAW_MENU),
    WITHDRAW_CUSTOM_AMOUNT(Menus.WITHDRAW_MENU),
    TRANSFER_ALL(Menus.TRANSFER_SUM_SELECTOR_MENU),
    TRANSFER_HALF(Menus.TRANSFER_SUM_SELECTOR_MENU),
    TRANSFER_QUARTER(Menus.TRANSFER_SUM_SELECTOR_MENU),
    TRANSFER_CUSTOM(Menus.TRANSFER_SUM_SELECTOR_MENU),
    TRANSFER_CUSTOM_AMOUNT(Menus.TRANSFER_SUM_SELECTOR_MENU),
    TRANSFER_SELECTOR_HEAD(Menus.TRANSFER_SELECTOR_MENU),
    TRANSFER_SELECTOR_NO_ACCOUNTS(Menus.TRANSFER_SELECTOR_MENU),
    TRANSFER_SUM_SELECTOR_HEAD(Menus.TRANSFER_SUM_SELECTOR_MENU),
    TRANSFER_CONFIRM_HEAD(Menus.TRANSFER_CONFIRM_MENU),
    TRANSFER_CONFIRM(Menus.TRANSFER_CONFIRM_MENU),
    TRANSFER_DECLINE(Menus.TRANSFER_CONFIRM_MENU),
    NO_RECENT_TRANSACTIONS(Menus.RECENT_TRANSACTIONS_MENU),
    LEADERBOARD_TOP_ONE(Menus.LEADERBOARD_MENU),
    LEADERBOARD_TOP_TWO(Menus.LEADERBOARD_MENU),
    LEADERBOARD_TOP_THREE(Menus.LEADERBOARD_MENU),
    LEADERBOARD_SENDER(Menus.LEADERBOARD_MENU),
    LEADERBOARD_SORT(Menus.LEADERBOARD_MENU),
    LEADERBOARD_NO_LEADERS(Menus.LEADERBOARD_MENU);


    private final String path;

    MenuItemNamesPaths(Menus menu) {
        this.path = "menu." + menu.name().toLowerCase() + ".item." + name().toLowerCase() + ".name";
    }
    MenuItemNamesPaths() {
        this.path = "menu.item." + name().toLowerCase() + ".name";
    }

    @Override
    public String path() {
        return path;
    }

}
