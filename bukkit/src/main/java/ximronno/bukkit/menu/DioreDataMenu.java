package ximronno.bukkit.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import ximronno.diore.api.account.Account;

import java.util.Locale;

public abstract class DioreDataMenu extends DioreMenu {

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot) {
        PersistentDataContainer container = getContainer(e);
        if(container == null) {
            return;
        }
        handleMenu(p, acc, locale, e, slot, container);
    }

    public abstract void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot, PersistentDataContainer container);

    public static PersistentDataContainer getContainer(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        if(clickedItem == null) return null;
        ItemMeta clickedItemMeta = clickedItem.getItemMeta();
        if(clickedItemMeta == null) return null;

        return clickedItemMeta.getPersistentDataContainer();
    }

}
