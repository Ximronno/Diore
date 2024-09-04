package ximronno.diore.api.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.*;

public class ItemBuilder {

    record PersistentDataWrapper(PersistentDataType type, Object value) {
    }
    Material material;

    Integer amount;

    String displayName;

    List<String> lore;

    PlayerProfile profile;

    Map<NamespacedKey, PersistentDataWrapper> persistentData;

    Map<Enchantment, Integer> enchantments;

    boolean addEnchantGlint;

    public ItemBuilder() {
        material = Material.BARRIER;
        amount = 1;
    }
    public ItemBuilder setMaterial(@NotNull Material material) {
        this.material = material;
        return this;
    }
    public ItemBuilder materialSelection(Material trueMaterial, Material falseMaterial, boolean condition) {
        if(condition) material = trueMaterial;
        else material = falseMaterial;
        return this;
    }
    public ItemBuilder setAmount(int amount) {
        if(amount < 0) amount = 0;
        this.amount = amount;
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder displayNameSelection(String trueDisplayName, String falseDisplayName, boolean condition) {
        if(condition) this.displayName = trueDisplayName;
        else this.displayName = falseDisplayName;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder loreSelection(List<String> trueLore, List<String> falseLore, boolean condition) {
        if(condition) this.lore = trueLore;
        else this.lore = falseLore;
        return this;
    }

    public ItemBuilder setProfile(PlayerProfile profile) {
        this.profile = profile;
        return this;
    }
    public ItemBuilder setProfileFromURL(URL url) {
        if(url == null) return this;
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        textures.setSkin(url);
        profile.setTextures(textures);

        this.profile = profile;
        return this;
    }

    public ItemBuilder setProfileFromURL(String url) {
        URL urlObject;
        try {
            urlObject = new URL(url);
        } catch (Exception e) {
            return this;
        }

        return setProfileFromURL(urlObject);
    }

    public ItemBuilder addPersistentData(@NotNull NamespacedKey key, @NotNull String value) {
        if(persistentData == null) {
            persistentData = new HashMap<>();
        }

        persistentData.put(key, new PersistentDataWrapper(PersistentDataType.STRING, value));

        return this;
    }

    public ItemBuilder addPersistentData(@NotNull NamespacedKey key, @NotNull PersistentDataType type, Object value) {
        if(persistentData == null) {
            persistentData = new HashMap<>();
        }

        persistentData.put(key, new PersistentDataWrapper(type, value));

        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        if(enchantments == null) {
            enchantments = new HashMap<>();
        }

        enchantments.put(enchantment, level);

        return this;
    }

    public ItemBuilder setEnchantingGlint(boolean glint) {
        addEnchantGlint = glint;
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        if(displayName != null) meta.setDisplayName(displayName);

        meta.setLore(lore);

        if(meta instanceof SkullMeta skullMeta) {

            if(profile != null) {
                skullMeta.setOwnerProfile(profile);
            }

        }

        if(persistentData != null) {
            persistentData.forEach((k, v) -> meta.getPersistentDataContainer().set(k, v.type, v.value));
        }

        if(enchantments != null) {
            item.addEnchantments(enchantments);
        }


        if(addEnchantGlint) {
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);

        return item;
    }
    public static ItemBuilder builder() {
        return new ItemBuilder();
    }



}
