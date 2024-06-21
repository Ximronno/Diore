package ximronno.diore.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import ximronno.diore.items.Items;

public class DioreNuggetFromDiore extends ShapelessRecipe {
    public DioreNuggetFromDiore(@NotNull NamespacedKey key) {
        super(key, Items.getDiamondOreNugget(10));

        addIngredient(Material.DIAMOND_ORE);

    }
}
