package ximronno.diore.items;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ximronno.diore.Diore;

public class Items {

    private static final Material diore_nugget_material = Material.GOLD_NUGGET;

    private static final NamespacedKey diamond_ore_nugget_key = new NamespacedKey(Diore.getInstance(), "diamond_ore_nugget");

    public static ItemStack getDiamondOreNugget(int amount) {

        ItemStack item = new ItemStack(diore_nugget_material, amount);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Diamond Ore Nugget");

        meta.setLore(List.of("A 1/10 of a diamond ore."));

        meta.getPersistentDataContainer().set(diamond_ore_nugget_key, PersistentDataType.STRING, "diamond_ore_nugget");

        item.setItemMeta(meta);

        return item;
    }






}
