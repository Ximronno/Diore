package ximronno.diore.guis;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public abstract class DiorePaginatedMenu extends DioreMenu {

    protected int currentPage = 0;
    protected int maxItemPerPage = 28;
    protected int index = 0;
    public DiorePaginatedMenu(NamespacedKey key) {
        super(key);
    }
    protected void addMenuBorder(FileConfiguration config){
        inventory.setItem(48, getButtonLeft(config));

        inventory.setItem(49, getMenuBack(config));

        inventory.setItem(50, getButtonRight(config));

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, getMenuBlank());
            }
        }

        inventory.setItem(17, getMenuBlank());
        inventory.setItem(18, getMenuBlank());
        inventory.setItem(26, getMenuBlank());
        inventory.setItem(27, getMenuBlank());
        inventory.setItem(35, getMenuBlank());
        inventory.setItem(36, getMenuBlank());

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, getMenuBlank());
            }
        }
    }
    private ItemStack getButtonLeft(FileConfiguration config) {

        ItemStack item = new ItemStack(Material.DARK_OAK_BUTTON);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("paginated-menu-button-left", config));

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "left");

        item.setItemMeta(meta);

        return item;

    }
    private ItemStack getButtonRight(FileConfiguration config) {

        ItemStack item = new ItemStack(Material.DARK_OAK_BUTTON);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("paginated-menu-button-right", config));

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "right");

        item.setItemMeta(meta);

        return item;

    }
    public int getMaxItemPerPage() {
        return maxItemPerPage;
    }
}
