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

        inventory.setItem(17, menuBlank);
        inventory.setItem(18, menuBlank);
        inventory.setItem(26, menuBlank);
        inventory.setItem(27, menuBlank);
        inventory.setItem(35, menuBlank);
        inventory.setItem(36, menuBlank);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, menuBlank);
            }
        }
    }
    private ItemStack getButtonLeft(FileConfiguration config) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.DARK_OAK_BUTTON)
                .setDisplayName(configManager.getFormattedString(config, "paginated-menu-button-left"))
                .build();

        ItemBuilder.addPersistentData(item, key,"left");

        return item;

    }
    private ItemStack getButtonRight(FileConfiguration config) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.DARK_OAK_BUTTON)
                .setDisplayName(configManager.getFormattedString(config, "paginated-menu-button-right"))
                .build();

        ItemBuilder.addPersistentData(item, key,"right");

        return item;

    }
    public int getMaxItemPerPage() {
        return maxItemPerPage;
    }
}
