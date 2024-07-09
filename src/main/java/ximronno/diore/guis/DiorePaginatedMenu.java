package ximronno.diore.guis;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import ximronno.api.item.ItemBuilder;

public abstract class DiorePaginatedMenu extends DioreMenu {

    protected int currentPage = 0;
    protected final int maxItemPerPage = 28;
    protected int index = 0;
    public DiorePaginatedMenu(NamespacedKey key) {
        super(key);
    }
    protected void addMenuBorder(FileConfiguration config){
        inventory.setItem(48, getButtonLeft(config));

        inventory.setItem(49, getMenuBack(config));

        inventory.setItem(50, getButtonRight(config));

        ItemStack menuBlank = getMenuBlank();

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, menuBlank);
            }
        }
        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, menuBlank);
            }
        }

        int[] specificSlots = {17, 18, 26, 27, 35, 36};
        for (int slot : specificSlots) {
            inventory.setItem(slot, menuBlank);
        }
    }
    private ItemStack getButtonLeft(FileConfiguration config) {
        return ItemBuilder.builder()
                .setMaterial(Material.DARK_OAK_BUTTON)
                .setDisplayName(configManager.getFormattedString(config, "paginated-menu-button-left"))
                .addPersistentData(key, "left")
                .build();
    }
    private ItemStack getButtonRight(FileConfiguration config) {
        return ItemBuilder.builder()
                .setMaterial(Material.DARK_OAK_BUTTON)
                .setDisplayName(configManager.getFormattedString(config, "paginated-menu-button-right"))
                .addPersistentData(key, "right")
                .build();
    }
    public int getMaxItemPerPage() {
        return maxItemPerPage;
    }
}
