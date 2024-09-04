package ximronno.bukkit.menu.yap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import ximronno.bukkit.menu.DioreMenu;
import ximronno.bukkit.menu.settings.LocaleSetterMenu;
import ximronno.bukkit.menu.settings.PrivacySetterMenu;
import ximronno.bukkit.menu.transactions.TransactionsMenu;
import ximronno.bukkit.message.type.LanguagePath;
import ximronno.bukkit.message.type.menu.MenuItemLorePaths;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;

import java.util.Locale;
import java.util.Map;

public class AccountMenu extends DioreMenu {

    private final int TRANSACTIONS_SLOT = 10;

    private final int LOCALE_SETTER_SLOT = 13;

    private final int PUBLIC_BALANCE_SETTER_SLOT = 16;

    @Override
    public Menu getPreviousMenu() {
        return new MainMenu();
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, true);

        inventory.setItem(TRANSACTIONS_SLOT, ItemBuilder.builder()
                .setMaterial(Material.DEEPSLATE_DIAMOND_ORE)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.TRANSACTIONS, locale, true, Map.of("{balance}", api.getAccountInfoFormatter().getFormattedBalance(acc, locale))))
                .setLore(messageManager.getList(MenuItemLorePaths.TRANSACTIONS, locale, true))
                .build());

        inventory.setItem(LOCALE_SETTER_SLOT, ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.LOCALE_SETTER, locale, true, Map.of("{lang_name}", messageManager.getMessage(LanguagePath.NAME, locale, true))))
                .setLore(messageManager.getList(MenuItemLorePaths.LOCALE_SETTER, locale, true, Map.of("{lang_name}", messageManager.getMessage(LanguagePath.NAME, locale, true))))
                .setProfileFromURL(api.getMessageManager().getTexturesURL(locale))
                .build());

        inventory.setItem(PUBLIC_BALANCE_SETTER_SLOT, ItemBuilder.builder()
                .materialSelection(Material.GREEN_TERRACOTTA, Material.RED_TERRACOTTA, acc.isPrivateBalance())
                .displayNameSelection(messageManager.getMessage(MenuItemNamesPaths.PRIVACY_SETTER_TRUE, locale, true),
                        messageManager.getMessage(MenuItemNamesPaths.PRIVACY_SETTER_FALSE, locale, true),
                        acc.isPrivateBalance())
                .setLore(messageManager.getList(MenuItemLorePaths.PRIVACY_SETTER, locale, true))
                .build());
    }

    @Override
    public String getTitle(Locale locale) {
        return messageManager.getMessage(MenuNamesPaths.ACCOUNT_MENU_NAME, locale, true);
    }

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot) {

        switch (slot) {
            case TRANSACTIONS_SLOT:
                close(p);
                new TransactionsMenu().open(p);
                break;
            case LOCALE_SETTER_SLOT:
                close(p);
                new LocaleSetterMenu().open(p);
                break;
            case PUBLIC_BALANCE_SETTER_SLOT:
                close(p);
                new PrivacySetterMenu().open(p);
                break;
        }

    }
}
