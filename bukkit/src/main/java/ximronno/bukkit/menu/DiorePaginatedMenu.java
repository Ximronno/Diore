package ximronno.bukkit.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.MenuSizes;

import java.util.Locale;

public abstract class DiorePaginatedMenu extends DioreMenu {

    protected final int NEXT_PAGE_BUTTON_SLOT = 51;

    protected final int PREVIOUS_PAGE_BUTTON_SLOT = 47;

    protected int currentPage = 0;

    protected int index = 0;

    protected boolean hasNextPage = false;

    @Override
    public MenuSizes getSize() {
       return MenuSizes.SIX_ROWS;
    }

    public void addButtons(Locale locale, boolean addBackButton) {

        BACK_BUTTON_SLOT = 49;

        if(currentPage > 0) {
            inventory.setItem(PREVIOUS_PAGE_BUTTON_SLOT, ItemBuilder.builder()
                    .setMaterial(Material.DARK_OAK_BUTTON)
                    .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.PREVIOUS_PAGE_BUTTON, locale, true))
                    .build());
        }

        if(addBackButton) {
            inventory.setItem(BACK_BUTTON_SLOT, getBackButton(locale));
        }

        if(hasNextPage) {
            inventory.setItem(NEXT_PAGE_BUTTON_SLOT, ItemBuilder.builder()
                    .setMaterial(Material.DARK_OAK_BUTTON)
                    .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.NEXT_PAGE_BUTTON, locale, true))
                    .build());
        }

    }

    @Override
    protected boolean handleSlot(Player p, int slot) {
        if(slot == BACK_BUTTON_SLOT) {
            openPreviousMenu(p);
            return true;
        }
        switch (slot) {
            case NEXT_PAGE_BUTTON_SLOT:
                if(hasNextPage) {
                    currentPage++;
                }
                update(p);
                return true;
            case PREVIOUS_PAGE_BUTTON_SLOT:
                if(currentPage > 0) {
                    currentPage--;
                }
                update(p);
                return true;
        }
        return false;
    }

    public abstract int getMaxItemsPerPage();

}
