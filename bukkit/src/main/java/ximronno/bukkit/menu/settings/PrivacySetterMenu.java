package ximronno.bukkit.menu.settings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import ximronno.bukkit.menu.DioreMenu;
import ximronno.bukkit.menu.yap.AccountMenu;
import ximronno.bukkit.message.type.menu.MenuItemLorePaths;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;

import java.util.Locale;

public class PrivacySetterMenu extends DioreMenu {

    private final int TRUE_SLOT = 12;

    private final int FALSE_SLOT = 14;

    @Override
    public String getTitle(Locale locale) {
        return messageManager.getMessage(MenuNamesPaths.PUBLIC_SETTER_MENU_NAME, locale, true);
    }

    @Override
    public Menu getPreviousMenu() {
        return new AccountMenu();
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, true);

        inventory.setItem(TRUE_SLOT, ItemBuilder.builder()
                .setMaterial(Material.GREEN_TERRACOTTA)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.PRIVACY_SETTER_TRUE, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.PRIVACY_SETTER_TRUE, locale, true))
                .build());

        inventory.setItem(FALSE_SLOT, ItemBuilder.builder()
                .setMaterial(Material.RED_TERRACOTTA)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.PRIVACY_SETTER_FALSE, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.PRIVACY_SETTER_FALSE, locale, true))
                .build());
    }

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot) {

        switch (slot) {
            case TRUE_SLOT:
                api.getAccountController().setPrivateBalance(p, acc, locale, true);
                openPreviousMenu(p);
                break;
            case FALSE_SLOT:
                api.getAccountController().setPrivateBalance(p, acc, locale, false);
                openPreviousMenu(p);
                break;
        }
    }

}
