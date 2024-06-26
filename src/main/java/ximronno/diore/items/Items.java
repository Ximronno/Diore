package ximronno.diore.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ximronno.diore.Diore;

public class Items {

    private static final Material diore_nugget_material = Material.TUBE_CORAL;
    private static final NamespacedKey diore_items_key = new NamespacedKey(Diore.getInstance(), "diore_items_key");

    public static ItemStack getDiamondOreNugget(int amount) {

        ItemStack item = new ItemStack(diore_nugget_material, amount);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Diore.getInstance().getConfigManager().getFormattedString("diamond-nugget-name"));

        List<String> lore = new ArrayList<>();
        Diore.getInstance().getConfig().getStringList("diamond-nugget-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(diore_items_key, PersistentDataType.STRING, "diore_nugget");

        item.setItemMeta(meta);

        return item;
    }

    public static NamespacedKey getDioreItemsKey() {
        return diore_items_key;
    }



}
