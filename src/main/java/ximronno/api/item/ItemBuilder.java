package ximronno.api.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    Material material;
    Integer amount = 1;
    String displayName;
    List<String> lore;
    PlayerProfile profile;
    public ItemBuilder() {
    }
    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }
    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }
    public ItemBuilder setProfile(PlayerProfile profile) {
        this.profile = profile;
        return this;
    }
    public ItemBuilder setProfileFromURL(URL url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        textures.setSkin(url);
        profile.setTextures(textures);

        this.profile = profile;
        return this;
    }
    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(displayName);

        meta.setLore(lore);

        if(meta instanceof SkullMeta skullMeta) {

            skullMeta.setOwnerProfile(profile);

        }

        item.setItemMeta(meta);

        return item;
    }
    public static void addPersistentData(ItemStack item, NamespacedKey key, String value) {

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);

        item.setItemMeta(meta);

    }
    public static ItemBuilder builder() {
        return new ItemBuilder();
    }



}
