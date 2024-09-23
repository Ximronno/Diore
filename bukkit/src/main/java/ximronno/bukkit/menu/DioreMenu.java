package ximronno.bukkit.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;
import ximronno.diore.api.menu.MenuSizes;
import ximronno.diore.api.message.MessageManager;

import java.util.Locale;

public abstract class DioreMenu extends Menu {

    protected final DioreAPI api = DioreAPI.getInstance();

    protected final MessageManager messageManager = api.getMessageManager();

    protected int BACK_BUTTON_SLOT = -1;

    @Override
    public MenuSizes getSize() {
        return MenuSizes.THREE_ROWS;
    }

    @Override
    public String getTitle() {
        return getTitle(messageManager.getMessageProvider().getDefaultLanguage());
    }

    public abstract String getTitle(Locale locale);

    public void openPreviousMenu(Player player) {
        close(player);
        if(getPreviousMenu() != null) {
            getPreviousMenu().open(player);
        }
    }

    @Override
    public void open(Player p) {
        Account acc = api.getAccount(p.getUniqueId());
        if(acc == null) return;

        Locale locale = acc.getLocale();

        inventory = Bukkit.createInventory(this, getSize().size(), getTitle(locale));

        setContents(p);

        p.openInventory(inventory);
    }

    @Override
    public void setContents(Player p) {
        Account acc = api.getAccount(p.getUniqueId());
        if (acc == null) return;

        Locale locale = acc.getLocale();

        setContents(p, acc, locale);
    }
    public abstract void setContents(Player p, Account acc, Locale locale);
    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();

        Account acc = api.getAccount(p.getUniqueId());
        if(acc == null) return;

        int slot = e.getSlot();
        if(handleSlot(p, slot)) {
            return;
        }

        handleMenu(p, acc, acc.getLocale(), e, slot);
    }

    protected boolean handleSlot(Player p, int slot) {
        if(slot == BACK_BUTTON_SLOT) {
            openPreviousMenu(p);
            return true;
        }
        return false;
    }

    protected void addBorder(Locale locale, boolean addBackButton) {
        ItemStack item1 = ItemBuilder.builder()
                .setMaterial(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .build();

        ItemStack item2 = ItemBuilder.builder()
                .setMaterial(Material.BLUE_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .build();

        int slot = 0;

        switch (getSize()) {
            case THREE_ROWS:
                addThreeRowBorder(item1, item2);
                slot = 24;
                break;
            case FOUR_ROWS:
                addFourRowBorder(item1, item2);
                slot = 33;
                break;
            case SIX_ROWS:
                addSixRowBorder(item1, item2);
                slot = 51;
                break;
        }
        if (addBackButton) {
            BACK_BUTTON_SLOT = slot;
            inventory.setItem(BACK_BUTTON_SLOT, getBackButton(locale));
        }
    }

    protected ItemStack getBackButton(Locale locale) {
        return ItemBuilder.builder()
                .setMaterial(Material.BARRIER)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.BACK_BUTTON, locale, true))
                .build();
    }

    private void addThreeRowBorder(ItemStack item1, ItemStack item2) {
        int[] item1Slots = {0, 1, 7, 8, 9, 17, 18, 19, 25, 26};
        int[] item2Slots = {2, 3, 4, 5, 6, 10, 11, 12, 13, 14, 15, 16, 20, 21, 22, 23, 24};

        for (int slot : item1Slots) {
            inventory.setItem(slot, item1);
        }

        for (int slot : item2Slots) {
            inventory.setItem(slot, item2);
        }

    }

    private void addFourRowBorder(ItemStack item1, ItemStack item2) {
        int[] item1Slots = {0, 1, 7, 8, 9, 17, 18, 26, 27, 28, 34, 35};
        int[] item2Slots = {2, 3, 4, 5, 6, 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 29, 30, 31, 32, 33};

        for (int slot : item1Slots) {
            inventory.setItem(slot, item1);
        }

        for (int slot : item2Slots) {
            inventory.setItem(slot, item2);
        }
    }

    private void addSixRowBorder(ItemStack item1, ItemStack item2) {
        int[] item1Slots = {0, 1, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 52, 53};
        int[] item2Slots = {2, 3, 4, 5, 6, 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43, 47, 48, 49, 50, 51};

        for (int slot : item1Slots) {
            inventory.setItem(slot, item1);
        }

        for (int slot : item2Slots) {
            inventory.setItem(slot, item2);
        }
    }

    public abstract void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot);
}
