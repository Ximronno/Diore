package ximronno.bukkit.menu;

public enum Menus {

    ACCOUNT_MENU,
    LOCALE_SETTER_MENU,
    MAIN_MENU,
    PRIVACY_SETTER_MENU,
    TRANSACTIONS_MENU,
    DEPOSIT_MENU,
    WITHDRAW_MENU,
    TRANSFER_SELECTOR_MENU,
    TRANSFER_SUM_SELECTOR_MENU,
    TRANSFER_CONFIRM_MENU,
    RECENT_TRANSACTIONS_MENU,;

    public String getName() {
        return name().toLowerCase();
    }

}
