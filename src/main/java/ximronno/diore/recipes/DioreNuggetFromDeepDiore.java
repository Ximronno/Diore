package ximronno.diore.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import ximronno.diore.items.Items;

public class DioreNuggetFromDeepDiore extends ShapelessRecipe {
    public DioreNuggetFromDeepDiore(@NotNull NamespacedKey key) {
        super(key, Items.getDiamondOreNugget(10));

        addIngredient(Material.DEEPSLATE_DIAMOND_ORE);

    }
}
