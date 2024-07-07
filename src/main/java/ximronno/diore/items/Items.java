package ximronno.diore.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import ximronno.api.item.ItemBuilder;
import ximronno.diore.Diore;

public class Items {
    private static final Diore plugin = Diore.getInstance();
    private static final NamespacedKey diore_items_key = new NamespacedKey(plugin, "diore_items_key");

    public static ItemStack getDiamondOreNugget(int amount) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.TUBE_CORAL)
                .setAmount(amount)
                .setDisplayName(plugin.getConfigManager().getFormattedString("diamond-nugget-name"))
                .setLore(plugin.getConfigManager().getFormattedList("diamond-nugget-lore"))
                .build();

        ItemBuilder.addPersistentData(item, diore_items_key, "diore_nugget");

        return item;
    }

    public static NamespacedKey getDioreItemsKey() {
        return diore_items_key;
    }



}
