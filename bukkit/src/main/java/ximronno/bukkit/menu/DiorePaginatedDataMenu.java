package ximronno.bukkit.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import ximronno.diore.api.account.Account;

import java.util.Locale;

public abstract class DiorePaginatedDataMenu extends DiorePaginatedMenu {

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot) {
        handleMenu(p, acc, locale, e, slot, DioreDataMenu.getContainer(e));
    }

    public abstract void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot, PersistentDataContainer container);

}
