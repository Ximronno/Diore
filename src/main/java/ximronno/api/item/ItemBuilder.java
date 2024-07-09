package ximronno.api.item;

import org.apache.commons.lang.Validate;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemBuilder {

    Material material;
    Integer amount = 1;
    String displayName;
    List<String> lore;
    PlayerProfile profile;
    Map<NamespacedKey, String> persistentData;
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
    public ItemBuilder addPersistentData(NamespacedKey key, String value) {
        Validate.notNull(key, "Key cannot be null");
        Validate.notNull(value, "Value cannot be null");

        if(persistentData == null) {
            persistentData = new HashMap<>();
        }

        persistentData.put(key, value);

        return this;
    }

    public ItemStack build() {
        Validate.notNull(material, "Material cannot be null");
        ItemStack item = new ItemStack(material, amount);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        if(displayName != null) meta.setDisplayName(displayName);

        meta.setLore(lore);

        if(meta instanceof SkullMeta skullMeta) {

            skullMeta.setOwnerProfile(profile);

        }

        if(persistentData != null) {
            persistentData.forEach((key, value) -> meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value));
        }

        item.setItemMeta(meta);

        return item;
    }
    @Deprecated
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
